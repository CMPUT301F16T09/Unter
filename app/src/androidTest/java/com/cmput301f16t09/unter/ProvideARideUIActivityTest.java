package com.cmput301f16t09.unter;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestRunner;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

/**
 * Created by alvin on 11/12/2016.
 */
public class ProvideARideUIActivityTest extends ActivityInstrumentationTestCase2<MainGUIActivity> {
    private Solo solo;

    /**
     * Instantiates a new Provide a ride ui activity test.
     */
    public ProvideARideUIActivityTest() {
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
        Log.d("TAG6","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

    /**
     * Test provide a ride buttons.
     */
    public void testProvideARideButtons() {
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("Request\nA Ride");
        solo.enterText((EditText) solo.getView(R.id.RequestRideStartLocation), "University LRT Station");
        solo.enterText((EditText) solo.getView(R.id.RequestRideEndLocation), "Corona Station");
        solo.clickOnButton("CONFIRM");

        solo.clickOnButton("My Ride\nRequests");
        solo.goBack();

        solo.goBack();
        solo.clearEditText((EditText) solo.getView(R.id.mainScreenUsername));
        solo.clearEditText((EditText) solo.getView(R.id.mainScreenPassword));
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "noblesse");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("Provide\nA Ride");
        solo.assertCurrentActivity("Wrong Activity",ProvideARideUIActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", RequestDetailsUIActivity.class);

        solo.clickOnText("kappaross");
        solo.goBack();
        solo.clickOnButton("Offer Ride");
        solo.waitForText("Successfully sent the offer!");

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);
        solo.clickOnButton("My Ride\nOffers");
        solo.assertCurrentActivity("Wrong Activity", MyRideOffersUIActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
    }

    public void testSearchKeyword() throws Exception {
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("Provide\nA Ride");
        solo.assertCurrentActivity("Wrong Activity",ProvideARideUIActivity.class);

        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Keyword");
        solo.waitForText("Search by Keyword");
        solo.clickOnButton("Confirm");
        solo.waitForText("Invalid search");
        solo.clickOnButton("Cancel");
        solo.waitForText("Search cancelled");

        solo.clickOnText("Keyword");
        solo.enterText((EditText) solo.getView(R.id.searchKeyword),"Corona");
        solo.clickOnButton("Confirm");
        solo.waitForText("Searching...");
    }

    public void testSearchGeolocation() throws Exception {
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("Provide\nA Ride");
        solo.assertCurrentActivity("Wrong Activity",ProvideARideUIActivity.class);

        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Geolocation");
        solo.waitForText("Search by Geolocation");
        solo.clickOnButton("Confirm");
        solo.waitForText("Invalid search");
        solo.clickOnButton("Cancel");
        solo.waitForText("Search cancelled");

        solo.clickOnText("Geolocation");
        solo.enterText((EditText) solo.getView(R.id.searchGeolocationLat),"Corona");
        solo.enterText((EditText) solo.getView(R.id.searchGeolocationLong),"");
        solo.enterText((EditText) solo.getView(R.id.searchGeolocationRadius),"5000");

        solo.clickOnButton("Confirm");
        solo.waitForText("Searching...");
    }

    public void testSearchFare() throws Exception {
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("Provide\nA Ride");
        solo.assertCurrentActivity("Wrong Activity",ProvideARideUIActivity.class);

        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Fare");
        solo.waitForText("Search by Fare");
        solo.clickOnButton("Confirm");
        solo.waitForText("Invalid search, no fields entered");
        solo.clickOnButton("Cancel");
        solo.waitForText("Search cancelled");

        solo.clickOnText("Fare");
        solo.enterText((EditText) solo.getView(R.id.searchMinFare),"2");
        solo.enterText((EditText) solo.getView(R.id.searchMaxFare),"1");

        solo.clickOnButton("Confirm");
        solo.waitForText("Invalid search");

        solo.clearEditText((EditText) solo.getView(R.id.searchMaxFare));
        solo.enterText((EditText) solo.getView(R.id.searchMaxFare),"50");
        solo.clickOnButton("Confirm");
        solo.waitForText("Searching...");
    }

    public void testSearchFareKM() throws Exception {
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("Provide\nA Ride");
        solo.assertCurrentActivity("Wrong Activity", ProvideARideUIActivity.class);

        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Fare/km");
        solo.waitForText("Search by Fare");
        solo.clickOnButton("Confirm");
        solo.waitForText("Invalid search, no fields entered");
        solo.clickOnButton("Cancel");
        solo.waitForText("Search cancelled");

        solo.clickOnText("Fare/km");
        solo.enterText((EditText) solo.getView(R.id.searchMinFareKM), "2");
        solo.enterText((EditText) solo.getView(R.id.searchMaxFareKM), "1");

        solo.clickOnButton("Confirm");
        solo.waitForText("Invalid search");

        solo.clearEditText((EditText) solo.getView(R.id.searchMaxFareKM));
        solo.enterText((EditText) solo.getView(R.id.searchMaxFareKM), "3");
        solo.clickOnButton("Confirm");
        solo.waitForText("Searching...");
    }

    public void testSearchAddress() throws Exception {
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("Provide\nA Ride");
        solo.assertCurrentActivity("Wrong Activity",ProvideARideUIActivity.class);

        solo.clickOnActionBarHomeButton();
        solo.clickOnText("Address");
        solo.waitForText("Search by Address");
        solo.clickOnButton("Confirm");
        solo.waitForText("Invalid search");
        solo.clickOnButton("Cancel");
        solo.waitForText("Search cancelled");

        solo.clickOnText("Address");
        solo.enterText((EditText) solo.getView(R.id.searchAddress),"Corona Station");
        solo.enterText((EditText) solo.getView(R.id.searchAddressRadius),"5000");

        solo.clickOnButton("Confirm");
        solo.waitForText("Searching...");
    }

}
