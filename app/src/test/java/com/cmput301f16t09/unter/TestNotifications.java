package com.cmput301f16t09.unter;
import junit.framework.TestCase;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Kelly on 2016-11-27.
 *
 * US 01.03.01  As a rider, I want to be notified if my request is accepted.
 * US 05.04.01  As a driver, I want to be notified if my ride offer was accepted.
 */
public class TestNotifications extends TestCase {

    public void testNotifyRider(){
        User Rider = new User("Rider","rider","rider@example.com","7801238909","r","No vehicle");
        User Driver = new User("Driver","driver","driver@example.com","7802238909","d","rav4");

        Post testReq = new Post(new GeoPoint(1.0, 2.0), new GeoPoint(2.0, 1.0), "A Start Address", "A end Address",
                "3.44", Rider.getUsername());

        String msg = Driver.getUsername() + " wants to be your driver for route " + testReq.getStartAddress() + " -> " + testReq.getEndAddress() +  ", in a " + Driver.getVehicle() + " .";
        Notification notification = new Notification(Rider.getUsername(), msg);
        notification.setPostType("request");

        assertEquals(Rider.getUsername(),notification.getUsername());
        assertEquals("request",notification.getPostType());
    }

    public void testNotifyDriver(){

        User r = new User("Rider","rider","rider@example.com","7801238909","r","No vehicle");
        User d = new User("Driver","driver","driver@example.com","7802238909","d","rav4");

        Post test = new Post(new GeoPoint(1.0, 2.0), new GeoPoint(2.0, 1.0), "A Start Address", "A end Address",
                "3.44", r.getUsername());

        String msg = "You have been selected to drive" + r.getUsername() + " from " + test.getStartAddress() + " to " + test.getEndAddress() + " !";
        Notification notification = new Notification(d.getUsername(), msg);
        notification.setPostType("offer"); //offer

        assertEquals(notification.getUsername(),d.getUsername());
        assertEquals("offer",notification.getPostType());
    }
}
