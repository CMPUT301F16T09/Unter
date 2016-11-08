package com.cmput301f16t09.unter;

import junit.framework.TestCase;

/**
 * The type Test location.
 */
public class TestLocation extends TestCase {

    /**
     * Test valid location.
     */
// US 01.01.01
    // US 01.01.02
    // US 10.01.01
    public void testValidLocation() {
        // Start CustomLocation at Computer Science Center (Open City Maps)
        CustomLocation startCustomLocation = new CustomLocation("53.52676", "-113.52715");
        assertTrue(startCustomLocation.checkValidLocation());

        // End CustomLocation at City Hall (Open City Maps)
        CustomLocation endCustomLocation = new CustomLocation("53.54565", "-113.49026");
        assertTrue(endCustomLocation.checkValidLocation());

        // Coordinates of China
        startCustomLocation.setLatitude("35.8617");
        startCustomLocation.setLatitude("104.1954");
        assertFalse(startCustomLocation.getLatitude().equals("53.52676"));
        assertFalse(startCustomLocation.getLongitude().equals("104.1954"));
        assertFalse(startCustomLocation.getLatitude().equals("35.8617"));
        assertFalse(startCustomLocation.getLongitude().equals("104.1954"));
        assertFalse(startCustomLocation.checkValidLocation());
    }

    /**
     * Test real location.
     */
// US 10.01.01
    public void testRealLocation() {
        // May or may not need °N, °E, °S, °W in the string
        CustomLocation startCustomLocation = new CustomLocation("53.52676", "-113.52715");
        assertTrue(startCustomLocation.latlongToAddress().equals("Computer Science Center"));

        CustomLocation endCustomLocation = new CustomLocation("53.54565", "-113.49026");
        assertTrue(endCustomLocation.latlongToAddress().equals("City Hall"));

        startCustomLocation.setLatitude("35.8617");
        startCustomLocation.setLatitude("1004.1954");
        assertTrue(startCustomLocation.latlongToAddress().equals("Invalid Address"));
    }
}
