/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
 * <p>
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 * <p>
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX_License-Identifier: EPL-2.0
 */
package org.openhab.binding.openems.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link OpenEMSBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Patrick Gell - Initial contribution
 */
@NonNullByDefault
public class OpenEMSBindingConstants {

    private static final String BINDING_ID = "openems";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_SAMPLE = new ThingTypeUID(BINDING_ID, "openems");

    // List of all Channel ids
    public static final String SUM_PROPERTY_CYCLE_TIME = "sum-PropertyCycleTime";
    public static final String SUM_GRID_SELL_ACTIVE_ENERGY = "sum-GridSellActiveEnergy";
    public static final String SUM_CONSUMPTION_ACTIVE_ENERGY = "sum-ConsumptionActiveEnergy";
    public static final String SUM_ESS_APPARENT_POWER = "sum-EssMaxApparentPower";
    public static final String SUM_PRODUCTION_DC_ACTIVE_ENERGY = "sum-ProductionDcActiveEnergy";
    public static final String SUM_CONSUMPTION_ACTIVE_POWER = "sum-ConsumptionActivePower";
    public static final String SUM_GRID_MAX_ACTIVE_POWER = "sum-GridMaxActivePower";
    public static final String SUM_ESS_CAPACITY = "sum-EssCapacity";
    public static final String SUM_CONSUMPTION_MAX_ACTIVE_POWER = "sum-ConsumptionMaxActivePower";
    public static final String SUM_ESS_ACTIVE_CHARGE_ENERGY = "sum-EssActiveChargeEnergy";
    public static final String SUM_GRID_ACTIVE_POWER_L2 = "sum-GridActivePowerL2";
    public static final String SUM_GRID_ACTIVE_POWER_L1 = "sum-GridActivePowerL1";
    public static final String SUM_PRODUCTION_DC_ACTUAL_POWER = "sum-ProductionDcActualPower";
    public static final String SUM_ESS_ACTIVE_DISCHARGE_ENERGY = "sum-EssActiveDischargeEnergy";
    public static final String SUM_PRODUCTION_MAX_AC_ACTIVE_POWER = "sum-ProductionMaxAcActivePower";
    public static final String SUM_ESS_ACTIVE_POWER = "sum-EssActivePower";
    public static final String SUM_GRID_MIN_ACTIVE_POWER = "sum-GridMinActivePower";
    public static final String SUM_PRODUCTION_ACTIVE_ENERGY = "sum-ProductionActiveEnergy";
    public static final String SUM_ESS_SOC = "sum-EssSoc";
    public static final String SUM_ESS_DC_DISCHARGE_ENERGY = "sum-EssDcDischargeEnergy";
    public static final String SUM_GRID_BUY_ACTIVE_ENERGY = "sum-GridBuyActiveEnergy";
    public static final String SUM_PROPERTY_IGNORE_STATE_COMPONENTS = "sum-PropertyIgnoreStateComponents";
    public static final String SUM_STATE = "sum-State";
    public static final String SUM_ESS_REACTIVE_POWER = "sum-EssReactivePower";
    public static final String SUM_PRODUCTION_MAX_DC_ACTUAL_POWER = "sum-ProductionMaxDcActualPower";
    public static final String SUM_PRODUCTION_ACTIVE_POWER = "sum-ProductionActivePower";
    public static final String SUM_ESS_DISCHARGE_POWER = "sum-EssDischargePower";
    public static final String SUM_CONSUMPTION_ACTIVE_POWER_L2 = "sum-ConsumptionActivePowerL2";
    public static final String SUM_CONSUMPTION_ACTIVE_POWER_L3 = "sum-ConsumptionActivePowerL3";
    public static final String SUM_CONSUMPTION_ACTIVE_POWER_L1 = "sum-ConsumptionActivePowerL1";
    public static final String SUM_ESS_ACTIVE_POWER_L3 = "sum-EssActivePowerL3";
    public static final String SUM_ESS_DC_CHARGE_ENERGY = "sum-EssDcChargeEnergy";
    public static final String SUM_ESS_ACTIVE_POWER_L2 = "sum-EssActivePowerL2";
    public static final String SUM_PRODUCTION_MAX_ACTIVE_POWER = "sum-ProductionMaxActivePower";
    public static final String SUM_PRODUCTION_AC_ACTIVE_ENERGY = "sum-ProductionAcActiveEnergy";
    public static final String SUM_HAS_IGNORED_COMPONENT_STATES = "sum-HasIgnoredComponentStates";
    public static final String SUM_GRID_ACTIVE_POWER_L3 = "sum-GridActivePowerL3";
    public static final String SUM_GRID_MODE = "sum-GridMode";
    public static final String SUM_ESS_ACTIVE_POWER_L1 = "sum-EssActivePowerL1";
    public static final String SUM_GRID_ACTIVE_POWER = "sum-GridActivePower";
    public static final String CHARGER0_ACTUAL_ENERGY = "charger0-ActualEnergy";
    public static final String CHARGER1_ACTUAL_ENERGY = "charger1-ActualEnergy";
    public static final String META_VERSION = "meta-Version";

}
