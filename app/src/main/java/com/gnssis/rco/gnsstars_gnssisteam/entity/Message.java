package com.gnssis.rco.gnsstars_gnssisteam.entity;

public class Message {

    private String description;
    private String latitude;
    private String longitude;
    private String objectName;
    private String objectType;
    private String mediaURL;
    private String title;

    public Message(String description, String latitude, String longitude, String objectName, String objectType, String mediaURL, String title) {
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.objectName = objectName;
        this.objectType = objectType;
        this.mediaURL = mediaURL;
        this.title = title;
    }

    public Message() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Message{" +
                "description='" + description + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", objectName='" + objectName + '\'' +
                ", objectType='" + objectType + '\'' +
                ", mediaURL='" + mediaURL + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
