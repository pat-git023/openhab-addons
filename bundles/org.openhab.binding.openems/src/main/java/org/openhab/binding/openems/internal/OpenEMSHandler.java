/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.openems.internal;

import static org.openhab.binding.openems.internal.OpenEMSBindingConstants.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The {@link OpenEMSHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Patrick Gell - Initial contribution
 */
@NonNullByDefault
public class OpenEMSHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(OpenEMSHandler.class);

    private @Nullable OpenEMSConfiguration config;
    private @Nullable ScheduledFuture<?> refreshTask;

    private final HashMap<String, OpenEMSData> currentStateData = new HashMap<>();
    private @NonNullByDefault String hostname = "";
    private @NonNullByDefault String username = "";
    private @NonNullByDefault String password = "";
    private long refreshTime;

    public OpenEMSHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            HashMap<String, OpenEMSData> data = fetchData();
            updateDataIfChanged(data);

            publishChannelIfLinked(channelUID);
        } else {
            logger.debug("Command {} is not supported for channel: {}. Supported command: REFRESH", command,
                    channelUID.getId());
        }
    }

    @Override
    public void initialize() {
        config = getConfigAs(OpenEMSConfiguration.class);
        if (checkConfiguration()) {
            updateStatus(ThingStatus.UNKNOWN);
            startAutomaticRefresh();
        }
    }

    @Override
    public void dispose() {
        if (refreshTask != null) {
            refreshTask.cancel(true);
        }
    }

    private void startAutomaticRefresh() {
        refreshTask = scheduler.scheduleWithFixedDelay(this::refreshState, 0, refreshTime, TimeUnit.SECONDS);
        logger.debug("Start automatic refresh at {} minutes!", refreshTime);
    }

    private void refreshState() {
        HashMap<String, OpenEMSData> data = fetchData();
        boolean dataUpdated = updateDataIfChanged(data);
        if (dataUpdated) {
            getThing().getChannels().forEach(channel -> publishChannelIfLinked(channel.getUID()));
        }
    }

    private void publishChannelIfLinked(ChannelUID channelUID) {
        String channelID = channelUID.getId();

        HashMap<String, OpenEMSData> dataState = currentStateData;
        if (dataState.isEmpty()) {
            logger.debug("Cannot update channel with ID {}; no data has been downloaded", channelID);
            return;
        }

        if (!isLinked(channelUID)) {
            logger.debug("Cannot update channel with ID {}; not linked!", channelID);
            return;
        }

        try {
            State state = null;
            var dataValue = dataState.get(channelID);
            if (dataValue == null || isValueBlank(channelID, dataValue.getValue())) {
                return;
            }
            logger.debug("try to parse channel {} and value {}", channelID, dataValue.getValue());
            switch (channelID) {
                case META_VERSION -> state = new StringType(dataValue.getValue());
                case SUM_GRID_MODE, SUM_STATE -> {

                    state = new DecimalType(dataValue.getValue());
                }
                case SUM_ESS_SOC, SUM_ESS_ACTIVE_POWER_L1, SUM_ESS_ACTIVE_POWER_L2, SUM_ESS_ACTIVE_POWER_L3, SUM_GRID_ACTIVE_POWER, SUM_GRID_ACTIVE_POWER_L1, SUM_GRID_ACTIVE_POWER_L2, SUM_GRID_ACTIVE_POWER_L3, SUM_PRODUCTION_ACTIVE_POWER, SUM_PRODUCTION_DC_ACTUAL_POWER, SUM_CONSUMPTION_ACTIVE_POWER, SUM_CONSUMPTION_ACTIVE_POWER_L1, SUM_CONSUMPTION_ACTIVE_POWER_L2, SUM_CONSUMPTION_ACTIVE_POWER_L3, SUM_ESS_ACTIVE_POWER, SUM_ESS_ACTIVE_CHARGE_ENERGY, SUM_ESS_ACTIVE_DISCHARGE_ENERGY, SUM_GRID_BUY_ACTIVE_ENERGY, SUM_GRID_SELL_ACTIVE_ENERGY, SUM_PRODUCTION_AC_ACTIVE_ENERGY, SUM_PRODUCTION_DC_ACTIVE_ENERGY, SUM_CONSUMPTION_ACTIVE_ENERGY, SUM_ESS_APPARENT_POWER, SUM_GRID_MAX_ACTIVE_POWER, SUM_ESS_CAPACITY, SUM_CONSUMPTION_MAX_ACTIVE_POWER, SUM_GRID_MIN_ACTIVE_POWER, SUM_ESS_DC_DISCHARGE_ENERGY, SUM_PRODUCTION_MAX_DC_ACTUAL_POWER, SUM_ESS_DISCHARGE_POWER, SUM_ESS_DC_CHARGE_ENERGY, SUM_PRODUCTION_MAX_ACTIVE_POWER, CHARGER0_ACTUAL_ENERGY, CHARGER1_ACTUAL_ENERGY, SUM_PRODUCTION_ACTIVE_ENERGY ->
                        state = new QuantityType<>(String.format("%s %s", dataValue.getValue(), dataValue.getUnit()));
                default -> logger.debug("Unknown channel: {}", channelID);
            }
            if (state != null) {
                logger.debug("update channel {} with {}", channelID, state);
                updateState(channelID, state);
            }
        } catch (IllegalArgumentException e) {
            logger.debug("Could not parse value '{}' for channel '{}'", dataState.get(channelID).getValue(), channelID);
        }
    }

    private boolean isValueBlank(String value, String channelID) {
        if (value.isBlank() || "null".equals(value)) {
            logger.debug("no update of channel {} because value is empty", channelID);
            return true;
        }
        return false;
    }

    private boolean updateDataIfChanged(HashMap<String, OpenEMSData> data) {
        AtomicBoolean dataChanged = new AtomicBoolean(false);
        data.forEach((key, value) -> {
            if (currentStateData.containsKey(key)) {
                if (!currentStateData.get(key).equals(value)) {
                    currentStateData.put(key, value);
                    dataChanged.set(true);
                }
            } else {
                currentStateData.put(key, value);
                dataChanged.set(true);
            }
        });
        return dataChanged.get();
    }

    private boolean checkConfiguration() {
        if (Objects.isNull(config)) {
            return false;
        }
        hostname = config.hostname;
        username = config.username;
        password = config.password;
        refreshTime = config.refreshInterval;
        return true;
    }

    private HashMap<String, OpenEMSData> fetchData() {
        HashMap<String, OpenEMSData> data = new HashMap<>();
        try {
            URL url = getDeviceURL();
            URLConnection connection = url.openConnection();
            var auth = "Basic " + Base64.getEncoder().encodeToString((this.username + ":" + this.password).getBytes());
            connection.setRequestProperty("Authorization", auth);
            OpenEMSData[] elements = parseDataFromStream(connection.getInputStream());
            for (OpenEMSData element : elements) {
                data.put(parseAddressToChannelId(element.getAddress()), element);
            }

            if (this.thing.getStatus() != ThingStatus.ONLINE) {
                updateStatus(ThingStatus.ONLINE);
            }
        } catch (Exception e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR, e.getMessage());
        }
        return data;
    }

    public OpenEMSData[] parseDataFromStream(InputStream stream) {
        OpenEMSData[] data = null;
        try {
            data =  new Gson().fromJson(new InputStreamReader(stream), OpenEMSData[].class);
        } catch (Exception e) {
            logger.warn("Could not read json", e);
        }

        return data!=null? data : new OpenEMSData[]{};
    }

    public String parseAddressToChannelId(String address) {
        return address.replaceAll("_", "").replaceAll("/", "-");
    }

    private URL getDeviceURL() throws MalformedURLException {
        String url;
        String ASSET_PATH = "rest/channel/.*/.*";
        if (hostname.startsWith("http")) {
            url = String.format("%s/%s", hostname, ASSET_PATH);
        } else {
            url = String.format("http://%s/%s", hostname, ASSET_PATH);
        }
        return new URL(url);
    }
}
