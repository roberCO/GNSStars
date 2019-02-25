package com.gnssis.rco.gnsstars_gnssisteam.constants;

public enum ObjectType {
    PLANET("Planet"),
    STAR("Star");

    private final String objectType;

    ObjectType(final String s) {
        objectType = s;
    }

    public String toString() {
        return objectType;
    }
    
}
