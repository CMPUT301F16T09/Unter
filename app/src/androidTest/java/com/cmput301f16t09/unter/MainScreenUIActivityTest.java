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

    public MainScreenUIActivityTest() {
        super(com.cmput301f16t09.unter.MainGUIActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception {
        Log.d("TAG3","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

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

    public void testMyRideRequestButton() {

        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.clickOnButton("My Ride\nRequest");
        solo.assertCurrentActivity("Wrong Activity, should be MyRideRequestUIActivity", MyRideRequestsUIActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);
        solo.goBack();
    }

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
