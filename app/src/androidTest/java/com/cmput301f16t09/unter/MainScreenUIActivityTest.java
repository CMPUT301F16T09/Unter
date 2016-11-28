package com.cmput301f16t09.unter;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by alvin on 11/9/2016.
 */
public class MainScreenUIActivityTest extends ActivityInstrumentationTestCase2<MainGUIActivity>{
    private Solo solo;

    /**
     * Instantiates a new Main screen ui activity test.
     */
    public MainScreenUIActivityTest() {
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
        Log.d("TAG3","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

    /**
     * Test request a ride button.
     */
    public void testRequestARideButton() {

        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.clickOnButton("Request\nA Ride");
        solo.assertCurrentActivity("Wrong Activity, should be RequestARideUIActivity", RequestARideUIActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);
        solo.goBack();

    }

    /**
     * Test my ride request button.
     */
    public void testMyRideRequestButton() {

        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity, should be MyRideRequestUIActivity", MyRideRequestsUIActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);
        solo.goBack();
    }

    /**
     * Test provide a ride button.
     */
    public void testProvideARideButton() {
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.clickOnButton("Provide\nA Ride");
        solo.assertCurrentActivity("Wrong Activity, should be ProvideARideUIActivity", ProvideARideUIActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);
        solo.goBack();
    }

    /**
     * Test my ride offers button.
     */
    public void testMyRideOffersButton() {
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.clickOnButton("My Ride\nOffers");
        solo.assertCurrentActivity("Wrong Activity, should be MyRideOffersUIActivity", MyRideOffersUIActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);
        solo.goBack();
    }
}

    /**
     * Test my notifications button.
     */
    public void testMyNotificationsButton() {
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.clickOnButton("My Notifications");
        solo.assertCurrentActivity("Wrong Activity, should be NotificationsActivity", NotificationsActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);
        solo.goBack();
    }
}