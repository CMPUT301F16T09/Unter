package com.cmput301f16t09.unter;

import android.app.Activity;
import android.app.Application;
import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by alvin on 11/9/2016.
 */
public class CreateNewUserUIActivityTest extends ActivityInstrumentationTestCase2<MainGUIActivity> {

    private Solo solo;

    /**
     * Instantiates a new Create new user ui activity test.
     */
    public CreateNewUserUIActivityTest() {
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
        Log.d("TAG1","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

    /**
     * Test data entry.
     */
    public void testDataEntry() {
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
        solo.clickOnButton("Create\nNew User");
        solo.assertCurrentActivity("Wrong Activity, should be CreateNewUserUIActivity", CreateNewUserUIActivity.class);

        solo.enterText((EditText) solo.getView(R.id.SignUpUsername), "KappaRoss");
        assertTrue(solo.waitForText("KappaRoss"));
        solo.clearEditText((EditText) solo.getView(R.id.SignUpUsername));
        assertTrue(solo.waitForText(""));
        solo.enterText((EditText) solo.getView(R.id.SignUpUsername), "KappaRoss");
        assertTrue(solo.waitForText("KappaRoss"));

        solo.enterText((EditText) solo.getView(R.id.SignUpName), "Bob Ross");
        assertTrue(solo.waitForText("Bob Ross"));

        solo.enterText((EditText) solo.getView(R.id.SignUpPhoneNumber), "123 456 7890");
        assertTrue(solo.waitForText("123 456 7890"));

        solo.enterText((EditText) solo.getView(R.id.SignUpEmail), "KappaRoss@Kappa.com");
        assertTrue(solo.waitForText("KappaRoss@Kappa.com"));

        solo.enterText((EditText) solo.getView(R.id.SignUpVehicleInfo), "KappaRossmobile");
        assertTrue(solo.waitForText("KappaRossmobile"));

        solo.enterText((EditText) solo.getView(R.id.SignUpPassword), "123");
        assertTrue(solo.waitForText("123"));

        solo.enterText((EditText) solo.getView(R.id.SignUpConfirmPassword), "123");
        assertTrue(solo.waitForText("123"));

        solo.clickOnButton("Confirm");

        solo.goBack();
    }
}
