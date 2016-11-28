package com.cmput301f16t09.unter;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * This is a test to check for notifications for the user..
 * Note: The Provide a Ride list must be empty for this test,
 * and CreateNewUserUIActivityTest must be ran before
 */

public class NotificationsActivityTest extends ActivityInstrumentationTestCase2<MainGUIActivity> {
    private Solo solo;

    public NotificationsActivityTest() {
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
    public void testNotifications() {
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("My Notifications");
        solo.assertCurrentActivity("Wrong Activity", NotificationsActivity.class);
        solo.goBack();

        // Create a Request
        solo.clickOnButton("Request\nA Ride");
        solo.assertCurrentActivity("Wrong Activity", RequestARideUIActivity.class);

        solo.enterText((EditText) solo.getView(R.id.RequestRideStartLocation), "University LRT Station");
        assertTrue(solo.waitForText("University LRT Station"));
        solo.clickOnButton("Find\nStart");
        solo.clickOnMenuItem("University LRT Station\nEdmonton, AB T6G 2P8");

        solo.enterText((EditText) solo.getView(R.id.RequestRideEndLocation), "Corona Station");
        assertTrue(solo.waitForText("Corona Station"));
        solo.clickOnButton("Find\nEnd");
        solo.clickOnMenuItem("Corona Station\nEdmonton, AB T5J");
        solo.clickOnButton("Confirm");

        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity", MyRideRequestsUIActivity.class);
        solo.goBack();

        solo.clickOnMenuItem("Log Out");

        //Login as someone else
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss2");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");

        solo.clickOnButton("Login");
        solo.clickOnButton("Provide\nA Ride");
        solo.clickInList(0);
        solo.clickOnButton("Offer Ride");
        solo.goBack();
        solo.clickOnMenuItem("Log Out");

        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("My Notifications");
        solo.clickInList(0);
        solo.goBack();
        solo.goBack();
        solo.goBack();

    }
}
