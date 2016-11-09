package com.cmput301f16t09.unter;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * a Post is a request created by riders, it contains the information that is inputted by
 * the rider to specify details of his/her request, this class contains the getter and setter
 * for all the information
 */
public class Post {
    private GeoPoint startCustomLocation;
    private GeoPoint endCustomLocation;
    private String status;
    private String fare;
    private ArrayList<String> driver_OfferList;
    private String id;
    private User user;


    /**
     * Instantiates a new Post.
     *
     * @param startCustomLocation the start location
     * @param endCustomLocation   the end location
     * @param fare          the fare
     */
    public Post(GeoPoint startCustomLocation, GeoPoint endCustomLocation, String fare, User rider) {
        this.startCustomLocation = startCustomLocation;
        this.endCustomLocation = endCustomLocation;
        this.status = "Pending Offer";
        this.fare = fare;
        this.driver_OfferList = new ArrayList();
        this.user = rider;
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
    public GeoPoint getStartCustomLocation() {
        return startCustomLocation;
    }

    /**
     * Sets start location.
     *
     * @param startCustomLocation the start location
     */
    public void setStartCustomLocation(GeoPoint startCustomLocation) {
        this.startCustomLocation = startCustomLocation;
    }

    /**
     * Gets end location.
     *
     * @return the end location
     */
    public GeoPoint getEndCustomLocation() {
        return endCustomLocation;
    }

    /**
     * Sets end location.
     *
     * @param endCustomLocation the end location
     */
    public void setEndCustomLocation(GeoPoint endCustomLocation) {
        this.endCustomLocation = endCustomLocation;
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
    public ArrayList getDriverOffers() {
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
    public void pickDriver(String driver) {
        this.driver_OfferList.clear();
        this.driver_OfferList.add(driver);
    }
}
