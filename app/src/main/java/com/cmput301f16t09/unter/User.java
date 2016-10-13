package com.cmput301f16t09.unter;


public class User {
    private String name;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private Rider rider;
    private Driver driver;

    /**
     * Instantiates a new User.
     *
     * @param name        the name
     * @param username    the username
     * @param email       the email
     * @param phoneNumber the phone number
     * @param password    the password
     */
    public User(String name, String username, String email, String phoneNumber, String password) {
        this.name = name.toLowerCase();
        this.username = username.toLowerCase();
        this.email = email.toLowerCase();
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.toLowerCase();
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
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
