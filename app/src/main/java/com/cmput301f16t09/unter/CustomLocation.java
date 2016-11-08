package com.cmput301f16t09.unter;

/**
 * The type CustomLocation.
 */
public class CustomLocation {
    /**
     * The Latitude.
     */
    String latitude;
    /**
     * The Longitude.
     */
    String longitude;

    /**
     * Instantiates a new CustomLocation.
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     */
    public CustomLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Check valid location boolean.
     *
     * @return the boolean
     */
    public Boolean checkValidLocation() {
        return true;
    }

    /**
     * Latlong to address string.
     *
     * @return the string
     */
    public String latlongToAddress() {
        return "";
    }
}
