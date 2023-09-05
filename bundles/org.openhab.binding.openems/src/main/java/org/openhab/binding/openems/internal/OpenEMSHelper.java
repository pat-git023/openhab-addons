package org.openhab.binding.openems.internal;

import java.util.Objects;

public class OpenEMSHelper {
    private static final OpenEMSHelper instance = new OpenEMSHelper();

    private OpenEMSHelper(){
        // private constructor
    }

    public static OpenEMSHelper of(){
        return instance;
    }

    public String normalizeUnits(final String inputUnit){
        if(Objects.nonNull(inputUnit) && inputUnit.contains("_Σ")) {
            return inputUnit.replaceAll("_Σ", "");
        }
        return inputUnit;
    }

}
