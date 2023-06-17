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

import java.util.Objects;

/**
 * The {@link OpenEMSData} class contains fields mapping values from OpenEMS response.
 *
 * @author Patrick Gell - Initial contribution
 */
public class OpenEMSData {
    private String address;
    private String type;
    private String accessMode;
    private String text;
    private String unit;
    private String value;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OpenEMSData that = (OpenEMSData) o;
        return Objects.equals(address, that.address) && Objects.equals(type, that.type)
                && Objects.equals(accessMode, that.accessMode) && Objects.equals(text, that.text)
                && Objects.equals(unit, that.unit) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, type, accessMode, text, unit, value);
    }
}
