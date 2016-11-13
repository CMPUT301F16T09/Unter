package com.cmput301f16t09.unter;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by alvin on 11/10/2016.
 */
public class MyRideRequestsUIActivityTest extends ActivityInstrumentationTestCase2<MainGUIActivity> {
    private Solo solo;

    public MyRideRequestsUIActivityTest() {
//        super(com.cmput301f16t09.unter.RequestARideUIActivity.class);
        super(com.cmput301f16t09.unter.MainGUIActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception {
        Log.d("TAG5","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void testButtons() {
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity",MyRideRequestsUIActivity.class);
        solo.goBack();

        solo.clickOnButton("Request\nA Ride");
        solo.enterText((EditText) solo.getView(R.id.RequestRideStartLocation), "University LRT Station");
        solo.enterText((EditText) solo.getView(R.id.RequestRideEndLocation), "Corona Station");
        solo.clickOnButton("Get\nEstimate");
        solo.clickOnButton("CONFIRM");

        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity",MyRideRequestsUIActivity.class);

        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity",RidersRequestDetailsPreUIActivity.class);
        solo.clickOnButton("Cancel Request");
        solo.waitForText("Successfully deleted the offer!");

        solo.goBack();
        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity",MyRideRequestsUIActivity.class);

        solo.goBack();
        solo.goBack();
    }

    public void testCompleteRequest() {
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);

        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("Request\nA Ride");
        solo.enterText((EditText) solo.getView(R.id.RequestRideStartLocation), "University LRT Station");
        solo.enterText((EditText) solo.getView(R.id.RequestRideEndLocation), "Corona Station");
        solo.clickOnButton("Get\nEstimate");
        solo.clickOnButton("CONFIRM");

        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity",MyRideRequestsUIActivity.class);
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
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", RideOfferDetailsUIActivity.class);
        solo.clickOnText("kappaross");
        solo.goBack();

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MyRideOffersUIActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);

        solo.clearEditText((EditText) solo.getView(R.id.mainScreenUsername));
        solo.clearEditText((EditText) solo.getView(R.id.mainScreenPassword));
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");

        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity", MyRideRequestsUIActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", RidersRequestDetailsPreUIActivity.class);

        solo.clickLongInList(0);
        solo.clickOnText("View Profile");
        solo.assertCurrentActivity("Wrong Activity", ViewProfileUIActivity.class);
        solo.goBack();

        solo.clickLongInList(0);
        solo.clickOnText("Choose this Driver");
        solo.waitForText("OK");
        solo.assertCurrentActivity("Wrong Activity", RidersRequestDetailsPostUIActivity.class);

        solo.clickOnText("noblesse");
        solo.assertCurrentActivity("Wrong Activity", ViewProfileUIActivity.class);
        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MyRideRequestsUIActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", RidersRequestDetailsPostUIActivity.class);

        solo.clickOnButton("Complete Request");
        solo.clickOnText("Complete Transaction");
        solo.waitForText("Completed request");
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
    }
}
