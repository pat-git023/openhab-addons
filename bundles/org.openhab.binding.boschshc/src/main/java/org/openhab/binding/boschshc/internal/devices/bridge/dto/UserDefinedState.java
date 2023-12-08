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
package org.openhab.binding.boschshc.internal.devices.bridge.dto;

import org.openhab.binding.boschshc.internal.services.dto.BoschSHCServiceState;

/**
 * Represents a single user-defined state defined on the Bosch Smart Home Controller.
 *
 * Example from Json:
 *
 * {
 * "@type": "userDefinedState",
 * "id": "23d34fa6-382a-444d-8aae-89c706e22158",
 * "name": "atHome",
 * "state": false
 * }
 *
 * @author Patrick Gell - Initial contribution
 */
public class UserDefinedState extends BoschSHCServiceState {

    public String id;
    public String name;
    public boolean state;

    public UserDefinedState() {
        super("UserDefinedState");
    }

    @Override
    public String toString() {
        return "UserDefinedState{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", state=" + state + ", type='"
                + type + '\'' + '}';
    }

    public static Boolean isValid(UserDefinedState obj) {
        return obj != null && obj.id != null;
    }
}
