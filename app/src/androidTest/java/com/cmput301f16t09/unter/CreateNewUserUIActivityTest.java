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
public class CreateNewUserUIActivityTest extends ActivityInstrumentationTestCase2<CreateNewUserUIActivity> {

    private Solo solo;
    public CreateNewUserUIActivityTest() {
        super(com.cmput301f16t09.unter.CreateNewUserUIActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception {
        Log.d("TAG1","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void testDataEntry() {
        solo.assertCurrentActivity("Wrong Activity", CreateNewUserUIActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editTextSignUpUsername), "KappaRoss");
        assertTrue(solo.waitForText("KappaRoss"));
        solo.clearEditText((EditText) solo.getView(R.id.editTextSignUpUsername));
        assertTrue(solo.waitForText(""));
        solo.enterText((EditText) solo.getView(R.id.editTextSignUpUsername), "KappaRoss");
        assertTrue(solo.waitForText("KappaRoss"));

        solo.enterText((EditText) solo.getView(R.id.editTextSignUpName), "Kappa Ross");
        assertTrue(solo.waitForText("Kappa Ross"));

        solo.enterText((EditText) solo.getView(R.id.editTextSignUpPhoneNumber), "123 456 7890");
        assertTrue(solo.waitForText("123 456 7890"));

        solo.enterText((EditText) solo.getView(R.id.editTextSignUpEmail), "KappaRoss@Kappa.com");
        assertTrue(solo.waitForText("KappaRoss@Kappa.com"));

        solo.enterText((EditText) solo.getView(R.id.editTextSignUpPassword), "RossKappa");
        assertTrue(solo.waitForText("RossKappa"));

        solo.enterText((EditText) solo.getView(R.id.editTextSignUpConfirmPassword), "RossKappa");
        assertTrue(solo.waitForText("RossKappa"));

        solo.clickOnButton("C o n f i r m");
    }
}
