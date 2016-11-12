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
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "RossKappa");
        solo.clickOnButton("Login");

        solo.clickOnButton("My Ride\nRequests");
        solo.assertCurrentActivity("Wrong Activity",MyRideRequestsUIActivity.class);

        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity",RidersRequestDetailsPreUIActivity.class);

        solo.goBack();
        solo.goBack();
        solo.goBack();
    }

}
