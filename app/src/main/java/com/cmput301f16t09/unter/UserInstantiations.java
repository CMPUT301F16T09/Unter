package com.cmput301f16t09.unter;

/**
 * Created by Daniel on 2016-10-12.
 */
public class UserInstantiations {
    private Rider rider;
    private Driver driver;

    public UserInstantiations() {
    }

    public UserInstantiations(Rider rider, Driver driver) {
        this.rider = rider;
        this.driver = driver;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public Rider getRider() {
        return rider;
    }
}
