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
    public CreateNewUserUIActivityTest() {
        super(com.cmput301f16t09.unter.MainGUIActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception {
        Log.d("TAG1","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

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

        solo.enterText((EditText) solo.getView(R.id.SignUpName), "Kappa Ross");
        assertTrue(solo.waitForText("Kappa Ross"));

        solo.enterText((EditText) solo.getView(R.id.SignUpPhoneNumber), "123 456 7890");
        assertTrue(solo.waitForText("123 456 7890"));

        solo.enterText((EditText) solo.getView(R.id.SignUpEmail), "KappaRoss@Kappa.com");
        assertTrue(solo.waitForText("KappaRoss@Kappa.com"));

        solo.enterText((EditText) solo.getView(R.id.SignUpPassword), "RossKappa");
        assertTrue(solo.waitForText("RossKappa"));

        solo.enterText((EditText) solo.getView(R.id.SignUpConfirmPassword), "RossKappa");
        assertTrue(solo.waitForText("RossKappa"));

        solo.clickOnButton("C o n f i r m");

        solo.goBack();
    }
}
