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

    /**
     * Instantiates a new Main gui activity test.
     */
    public MainGUIActivityTest() {
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
        Log.d("TAG2","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

    /**
     * Test login.
     */
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
        solo.assertCurrentActivity("Wrong Activity, should be MainScreenActivity", MainScreenUIActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
    }

    /**
     * Test create user button.
     */
    public void testCreateUserButton() {
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);

        solo.clickOnButton("Create\nNew User");
        solo.assertCurrentActivity("Wrong Activity, should be CreateNewUserUIActivity", CreateNewUserUIActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
    }
}
