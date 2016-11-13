package com.cmput301f16t09.unter;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by alvin on 11/9/2016.
 */
public class MainGUIActivityTest extends ActivityInstrumentationTestCase2<MainGUIActivity> {

    private Solo solo;
    public MainGUIActivityTest() {
        super(com.cmput301f16t09.unter.MainGUIActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception {
        Log.d("TAG2","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void testLogin() {

        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);

        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        assertTrue(solo.waitForText("KappaRoss"));
        solo.clearEditText((EditText) solo.getView(R.id.mainScreenUsername));
        assertTrue(solo.waitForText(""));
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        assertTrue(solo.waitForText("KappaRoss"));


        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        assertTrue(solo.waitForText("123"));

        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity, should be CreateNewUser", MainScreenUIActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
    }

    public void testCreateUserButton() {
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);

        solo.clickOnButton("Create\nNew User");
        solo.assertCurrentActivity("Wrong Activity, should be CreateNewUserUIActivity", CreateNewUserUIActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
    }

}
