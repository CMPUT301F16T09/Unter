package com.cmput301f16t09.unter;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by alvin on 11/10/2016.
 */
//public class RequestARideUIActivityTest extends ActivityInstrumentationTestCase2<RequestARideUIActivity>{
public class RequestARideUIActivityTest extends ActivityInstrumentationTestCase2<MainGUIActivity>{
    private Solo solo;

    /**
     * Instantiates a new Request a ride ui activity test.
     */
    public RequestARideUIActivityTest() {
//        super(com.cmput301f16t09.unter.RequestARideUIActivity.class);
        super(com.cmput301f16t09.unter.MainGUIActivity.class);
    }

    /**
     * Test start.
     *
     * @throws Exception the exception
     */
    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception {
        Log.d("TAG4","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

    /**
     * Test buttons.
     */
    public void testButtons() {
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity", MyRideRequestsUIActivity.class);
        solo.goBack();

        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.clickOnButton("Request\nA Ride");

        solo.assertCurrentActivity("Wrong Activity", RequestARideUIActivity.class);
        solo.clickOnButton("Get\nEstimate");
        assertTrue(solo.waitForText("Please fill in start and end locations"));

        solo.enterText((EditText) solo.getView(R.id.RequestRideStartLocation), "University LRT Station");
        assertTrue(solo.waitForText("University LRT Station"));
        solo.clickOnButton("Get\nEstimate");
        assertTrue(solo.waitForText("Please fill in end location"));
        solo.clickOnButton("CONFIRM");
        assertTrue(solo.waitForText("Please fill in end location"));

        solo.clearEditText((EditText) solo.getView(R.id.RequestRideStartLocation));
        assertTrue(solo.waitForText(""));
        solo.clickOnButton("Get\nEstimate");
        assertTrue(solo.waitForText("Please fill in start and end locations"));
        solo.clickOnButton("CONFIRM");
        assertTrue(solo.waitForText("Please fill in start and end locations"));

        solo.enterText((EditText) solo.getView(R.id.RequestRideEndLocation), "Corona Station");
        assertTrue(solo.waitForText("Corona Station"));
        solo.clickOnButton("Get\nEstimate");
        assertTrue(solo.waitForText("Please fill in start location"));
        solo.clickOnButton("CONFIRM");
        assertTrue(solo.waitForText("Please fill in start location"));

        solo.enterText((EditText) solo.getView(R.id.RequestRideStartLocation), "University LRT Station");
        assertTrue(solo.waitForText("University LRT Station"));
        solo.clickOnButton("Get\nEstimate");
        // Fare Estimate is not working atm.
        solo.clickOnButton("CONFIRM");
        assertTrue(solo.waitForText("Request Made"));

        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity", MyRideRequestsUIActivity.class);
        solo.goBack();
        solo.goBack();
        solo.goBack();
    }




}
