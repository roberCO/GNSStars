package com.gnssis.rco.gnsstars_gnssisteam;

public class LocalPoint {

    public String latitude;
    public String longitude;

    public LocalPoint(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public LocalPoint() {
        this.latitude = "none";
        this.longitude = "none";
    }

    @Override
    public String toString() {
        return "Point{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
