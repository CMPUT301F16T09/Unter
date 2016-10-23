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
        // Start Location at Computer Science Center (Open City Maps)
        Location startLocation = new Location("53.52676", "-113.52715");
        assertTrue(startLocation.checkValidLocation());

        // End Location at City Hall (Open City Maps)
        Location endLocation = new Location("53.54565", "-113.49026");
        assertTrue(endLocation.checkValidLocation());

        // Coordinates of China
        startLocation.setLatitude("35.8617");
        startLocation.setLatitude("104.1954");
        assertFalse(startLocation.getLatitude().equals("53.52676"));
        assertFalse(startLocation.getLongitude().equals("104.1954"));
        assertFalse(startLocation.getLatitude().equals("35.8617"));
        assertFalse(startLocation.getLongitude().equals("104.1954"));
        assertFalse(startLocation.checkValidLocation());
    }

    /**
     * Test real location.
     */
// US 10.01.01
    public void testRealLocation() {
        // May or may not need °N, °E, °S, °W in the string
        Location startLocation = new Location("53.52676", "-113.52715");
        assertTrue(startLocation.latlongToAddress().equals("Computer Science Center"));

        Location endLocation = new Location("53.54565", "-113.49026");
        assertTrue(endLocation.latlongToAddress().equals("City Hall"));

        startLocation.setLatitude("35.8617");
        startLocation.setLatitude("1004.1954");
        assertTrue(startLocation.latlongToAddress().equals("Invalid Address"));
    }
}
