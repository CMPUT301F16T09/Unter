package com.cmput301f16t09.unter;

import junit.framework.TestCase;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;


/**
 * The type Test post.
 */
public class TestPost extends TestCase {

    /**
     * Test get start location.
     */
    public void testGetStartLocation() {

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        assertTrue(post.getStartLocation() == start);
    }

    /**
     * Test set start location.
     */
    public void testSetStartLocation() {

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        GeoPoint start2 = new GeoPoint(400.0, 200.0);
        post.setStartLocation(start2);

        assertTrue((post.getStartLocation() == start2));
    }

    /**
     * Test get end location.
     */
    public void testGetEndLocation() {

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        assertTrue(post.getEndLocation() == end);
    }

    /**
     * Test set end location.
     */
    public void testSetEndLocation() {

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        GeoPoint end2 = new GeoPoint(200.0, 400.0);
        post.setEndLocation(end2);

        assertTrue((post.getStartLocation() == end2));
    }

    /**
     * Test set status.
     */
    public void testSetStatus() {

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        post.setStatus("I'm ready");

        assertTrue((post.getStatus().equals("I'm ready")));
    }

    /**
     * Test get Status
     */
    public void testGetStatus() {

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        post.setStatus("I'm ready");

        assertTrue((post.getStatus().equals("I'm ready")));
    }

    /**
     * Test get fare.
     */
    public void testGetFare() {

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        assertTrue((post.getFare().equals("$4")));
    }

    /**
     * Test set fare.
     */
    public void testSetFare() {

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        post.setFare(5.0);

        assertTrue((post.getFare().equals(5.0)));
    }

    /**
     * test get driver offer
     */
    public void testSetDriverOffers(){

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        ArrayList driverOffers = new ArrayList<>();
        driverOffers.add("Joe");
        driverOffers.add("Jim");

        post.setdriverOffers(driverOffers);

        assertTrue((post.getDriverOffers() == driverOffers));
    }

    /**
     * Test get driver offers.
     */
    public void testGetDriverOffers() {

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        ArrayList driverOffers = new ArrayList<>();
        driverOffers.add("Joe");
        driverOffers.add("Jim");

        post.setdriverOffers(driverOffers);

        assertTrue((post.getDriverOffers() == driverOffers));
    }

    /**
     * Test add driver offer.
     */
    public void testAddDriverOffer() {

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        assertTrue((post.getDriverOffers()).isEmpty());
        post.addDriverOffer("Joe");

        assertTrue((post.getDriverOffers()).size() == 1);
        assertTrue((post.getDriverOffers()).get(0) == "Joe");

    }

    /**
     * Test pick driver.
     */
    public void testPickDriver() {

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password", "");

        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post post = new Post(start, end, "1", "2", 4.0, 1.2, rider.getUsername());

        post.addDriverOffer("Joe");
        post.addDriverOffer("Jim");
        post.addDriverOffer("Jon");

        post.pickDriver("Jim");

        assertTrue((post.getDriverOffers()).size() == 1);
        assertTrue((post.getDriverOffers()).get(0) == "Jim");
    }
}