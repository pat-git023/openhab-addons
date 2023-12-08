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
package org.openhab.binding.boschshc.internal.services.userstate.dto;

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.binding.boschshc.internal.services.dto.BoschSHCServiceState;
import org.openhab.core.library.types.OnOffType;

/**
 * Represents the state of a user-defined state
 *
 * @author Patrick Gell - Initial contribution
 */
public class UserStateServiceState extends BoschSHCServiceState {

    public UserStateServiceState() {
        super("userdefinedstates");
    }

    /**
     * Current state
     */
    public boolean state;

    public @NonNull OnOffType toOnOffType() {
        return OnOffType.from(state);
    }

    @Override
    public String toString() {
        return "UserStateServiceState{" + "state=" + state + ", type='" + type + '\'' + '}';
    }
}
