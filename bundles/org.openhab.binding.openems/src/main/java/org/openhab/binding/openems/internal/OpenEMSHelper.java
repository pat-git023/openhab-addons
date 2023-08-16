package org.openhab.binding.openems.internal;

import java.util.Objects;

public class OpenEMSHelper {

    private static final OpenEMSHelper instance = new OpenEMSHelper();

    private OpenEMSHelper(){
        // private only constructor
    }

    public static OpenEMSHelper of(){
        return instance;
    }

    public String normalizeUnit(final String inputUnit) {
        if(Objects.nonNull(inputUnit) && inputUnit.contains("_Σ")) {
            return inputUnit.replaceAll("_Σ", "");
        }
        return inputUnit;
    }
}
