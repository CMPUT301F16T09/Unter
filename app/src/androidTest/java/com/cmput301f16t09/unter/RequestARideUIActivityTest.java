package com.cmput301f16t09.unter;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * This is a test for requesting a ride
 */
public class RequestARideUIActivityTest extends ActivityInstrumentationTestCase2<MainGUIActivity>{
    private Solo solo;

    /**
     * Instantiates a new Request a ride ui activity test.
     */
    public RequestARideUIActivityTest() {
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
     * Test creating a user
     */
    public void testRequestARide() {
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity", MyRideRequestsUIActivity.class);
        solo.goBack();

        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.clickOnButton("Request\nA Ride");
        solo.assertCurrentActivity("Wrong Activity", RequestARideUIActivity.class);

        solo.clickOnButton("Confirm");

        solo.enterText((AutoCompleteTextView) solo.getView(R.id.RequestRideStartLocation), "University LRT Station");
        assertTrue(solo.waitForText("University LRT Station"));
        solo.clickOnButton("Find\nStart");
        solo.clickOnMenuItem("University LRT Station\nEdmonton, AB T6G 2P8");
        solo.clickOnButton("Confirm");

        solo.enterText((AutoCompleteTextView) solo.getView(R.id.RequestRideEndLocation), "Corona Station");
        assertTrue(solo.waitForText("Corona Station"));
        solo.clickOnButton("Find\nEnd");
        solo.clickOnMenuItem("Corona Station\nEdmonton, AB T5J");
        solo.clickOnButton("Confirm");

        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity", MyRideRequestsUIActivity.class);
        solo.goBack();
        solo.goBack();
    }
}
