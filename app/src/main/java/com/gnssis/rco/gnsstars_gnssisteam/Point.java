package com.gnssis.rco.gnsstars_gnssisteam;

public class Point {

    public String latitude;
    public String longitude;

    public Point(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public Point() {
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
