package com.cmput301f16t09.unter;


public class UserInstantiations {
    private Rider rider;
    private Driver driver;

    /**
     * Instantiates a new User instantiations.
     */
    public UserInstantiations() {
    }

    /**
     * Instantiates a new User instantiations.
     *
     * @param rider  the rider
     * @param driver the driver
     */
    public UserInstantiations(Rider rider, Driver driver) {
        this.rider = rider;
        this.driver = driver;
    }

    /**
     * Sets rider.
     *
     * @param rider the rider
     */
    public void setRider(Rider rider) {
        this.rider = rider;
    }

    /**
     * Sets driver.
     *
     * @param driver the driver
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Gets driver.
     *
     * @return the driver
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Gets rider.
     *
     * @return the rider
     */
    public Rider getRider() {
        return rider;
    }
}
