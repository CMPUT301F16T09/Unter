package com.cmput301f16t09.unter;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * a Post is a request created by riders, it contains the information that is inputted by
 * the rider to specify details of his/her request, this class contains the getter and setter
 * for all the information
 */
public class Post {
    private GeoPoint startLocation;
    private GeoPoint endLocation;
    private String status;
    private String fare;
    private ArrayList<String> driver_OfferList;
    @JestId
    private String documentId;
    private String user;
    private String driver;

    /**
     * Instantiates a new Post.
     *
     * @param startLocation the start location
     * @param endLocation   the end location
     * @param fare          the fare
     */
    public Post(GeoPoint startLocation, GeoPoint endLocation, String fare, String rider) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.status = "Pending Offer";
        this.fare = fare;
        this.driver_OfferList = new ArrayList<String>();
        this.user = rider;
        this.driver = null;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return this.documentId;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.documentId = id;
    }

    /**
     * Gets start location.
     *
     * @return the start location
     */
    public GeoPoint getStartLocation() {
        return startLocation;
    }

    /**
     * Sets start location.
     *
     * @param startLocation the start location
     */
    public void setStartLocation(GeoPoint startLocation) {
        this.startLocation = startLocation;
    }


    /**
     * Gets end location.
     *
     * @return the end location
     */
    public GeoPoint getEndLocation() {
        return endLocation;
    }

    /**
     * Sets end location.
     *
     * @param endLocation the end location
     */
    public void setEndLocation(GeoPoint endLocation) {
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
    public ArrayList getDriverOffers() {
        return this.driver_OfferList;
    }

    public String getDriver() {
        return this.driver;
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
        this.driver = driver;
    }

    public String getUsername() {
        return this.user;
    }

    public String toString(){
        //String output = "Rider: "+ getUsername()+ "Driver:"
        String output= "this is a post";
        return output;
    }
}
