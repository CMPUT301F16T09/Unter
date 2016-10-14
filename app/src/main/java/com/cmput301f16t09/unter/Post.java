package com.cmput301f16t09.unter;

import java.util.ArrayList;

/**
 * a Post is a request created by riders, it contains the information that is inputted by
 * the rider to specify details of his/her request, this class contains the getter and setter
 * for all the information
 */

public class Post {
    private String startLocation;
    private String endLocation;
    private String status;
    private String fare;
    private Rider rider;
    private ArrayList<String> driverOffers;

    /**
     * Instantiates a new Post.
     *
     * @param startLocation the start location
     * @param endLocation   the end location
     * @param fare          the fare
     * @param rider         the rider
     */
    public Post(String startLocation, String endLocation, String fare, Rider rider) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.status = "Pending Offer";
        this.fare = fare;
        this.rider = rider;
        this.driverOffers = new ArrayList<String>();
    }

    /**
     * Gets start location.
     *
     * @return the start location
     */
    public String getStartLocation() {
        return startLocation;
    }

    /**
     * Sets start location.
     *
     * @param startLocation the start location
     */
    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    /**
     * Gets end location.
     *
     * @return the end location
     */
    public String getEndLocation() {
        return endLocation;
    }

    /**
     * Sets end location.
     *
     * @param endLocation the end location
     */
    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets drive offers.
     *
     * @return the drive offers
     */
    public ArrayList<String> getDriveOffers() {
        return driverOffers;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets fare.
     *
     * @return the fare
     */
    public String getFare() {
        return fare;
    }

    /**
     * Sets fare.
     *
     * @param fare the fare
     */
    public void setFare(String fare) {
        this.fare = fare;
    }

    /**
     * Gets rider.
     *
     * @return the rider
     */
    public Rider getRider() {
        return rider;
    }

    /**
     * Sets rider.
     *
     * @param riderPost the rider post
     */
    public void setRider(Rider riderPost) {
        this.rider = riderPost;
    }

    /**
     * Add driver offer.
     *
     * @param driverName the driver name
     */
    public void addDriverOffer(String driverName) {
        this.driverOffers.add(driverName);
    }
}
