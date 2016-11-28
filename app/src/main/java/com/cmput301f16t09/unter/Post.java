package com.cmput301f16t09.unter;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Objects;

import io.searchbox.annotations.JestId;

/**
 * The type Post.
 */
public class Post {
    private GeoPoint startLocation;
    private GeoPoint endLocation;
    private String startAddress;
    private String endAddress;
    private String status;
    private Double fare;
    private Double fareKM;
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
     * @param startAddress  the start address
     * @param endAddress    the end address
     * @param fare          the fare
     * @param rider         the rider
     */
    public Post(GeoPoint startLocation, GeoPoint endLocation, String startAddress, String endAddress,
                Double fare, Double fareKM, String rider) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.status = "Pending Approval";
        this.fare = fare;
        this.driver_OfferList = new ArrayList<String>();
        this.user = rider;
        this.driver = null;
        this.fareKM = fareKM;
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
     * Gets start address.
     *
     * @return the start address
     */
    public String getStartAddress() {return startAddress;}

    /**
     * Sets start address.
     *
     * @param address the address
     */
    public void setStartAddress(String address) {this.startAddress = address;}

    /**
     * Gets end address.
     *
     * @return the end address
     */
    public String getEndAddress() {return endAddress;}

    /**
     * Sets end address.
     *
     * @param address the address
     */
    public void setEndAddress(String address) {this.endAddress = address;}

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
    public Double getFare() {
        return fare;
    }

    /**
     * Sets fare.
     *
     * @param fare the fare
     */
    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Double getFareKM() {
        return fareKM;
    }

    public void setFareKM(Double fareKM) {
        this.fareKM = fareKM;
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
     * Gets driver offers.
     *
     * @return the driver offers
     */
    public ArrayList<String> getDriverOffers() {
        return this.driver_OfferList;
    }

    /**
     * Gets driver.
     *
     * @return the driver
     */
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

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return this.user;
    }

//    public String toString(){
//        return "Rider: "+ getUsername()+ "Driver:";
//    }

}
