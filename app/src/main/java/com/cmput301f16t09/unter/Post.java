package com.cmput301f16t09.unter;

import java.util.ArrayList;

/**
 * a Post is a request created by riders, it contains the information that is inputted by
 * the rider to specify details of his/her request, this class contains the getter and setter
 * for all the information
 */
public class Post {
    private Location startLocation;
    private Location endLocation;
    private String status;
    private String fare;
    private ArrayList<String> driver_OfferList;
    private String id;

    /**
     * Instantiates a new Post.
     *
     * @param startLocation the start location
     * @param endLocation   the end location
     * @param fare          the fare
     */
    public Post(Location startLocation, Location endLocation, String fare) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.status = "Pending Offer";
        this.fare = fare;
        this.driver_OfferList = new ArrayList();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets start location.
     *
     * @return the start location
     */
    public Location getStartLocation() {
        return startLocation;
    }

    /**
     * Sets start location.
     *
     * @param startLocation the start location
     */
    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    /**
     * Gets end location.
     *
     * @return the end location
     */
    public Location getEndLocation() {
        return endLocation;
    }

    /**
     * Sets end location.
     *
     * @param endLocation the end location
     */
    public void setEndLocation(Location endLocation) {
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
     * Add driver offer.
     *
     * @param driverName the driver name
     */
    public void addDriverOffer(String driverName) {
        this.driver_OfferList.add(driverName);
    }

    /**
     * Gets drive offers.
     *
     * @return the drive offers
     */
    public ArrayList getdriverOffers() {
        return this.driver_OfferList;
    }

    /**
     * Sets offers.
     *
     * @param driveroffers the driveroffers
     */
    public void setdriverOffers(ArrayList<String> driveroffers) {
        this.driver_OfferList = driveroffers;
    }

    /**
     * Pick driver.
     *
     * @param driver the driver
     */
    public void pick_driver(String driver) {
        this.driver_OfferList.clear();
        this.driver_OfferList.add(driver);
    }
}
