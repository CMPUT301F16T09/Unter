package com.cmput301f16t09.unter;

import java.util.ArrayList;

/**
 * Created by Daniel on 2016-10-12.
 */
public class Post {
    private String startLocation;
    private String endLocation;
    private String status;
    private String fare;
    private Rider rider;
    private ArrayList<String> driverOffers;

    public Post(String startLocation, String endLocation, String fare, Rider rider) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.status = "Pending Offer";
        this.fare = fare;
        this.rider = rider;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<String> getDriveOffers() {
        return driverOffers;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider riderPost) {
        this.rider = riderPost;
    }

    public void addDriverOffer(String driverName) {
        this.driverOffers.add(driverName);
    }
}
