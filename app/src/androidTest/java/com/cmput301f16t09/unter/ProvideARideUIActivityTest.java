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

    public ProvideARideUIActivityTest() {
//        super(com.cmput301f16t09.unter.RequestARideUIActivity.class);
        super(com.cmput301f16t09.unter.MainGUIActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception {
        Log.d("TAG6","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

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
}
