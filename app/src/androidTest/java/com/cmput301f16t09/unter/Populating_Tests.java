package com.cmput301f16t09.unter;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by Kevin on 11/28/2016.
 */
public class Populating_Tests extends ActivityInstrumentationTestCase2<MainGUIActivity> {
    private Solo solo;
    /**
     * Instantiates a new Main gui activity test.
     */
    public Populating_Tests() {
        super(com.cmput301f16t09.unter.MainGUIActivity.class);
    }
    /**
     * Test start.
     *
     * @throws Exception the exception
     */

    public void setUp() throws Exception {
        Log.d("TAG2", "setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }
    public void testPopulating() throws Exception {
        // Create User #1
        solo.clickOnButton("Create\nNew User");
        solo.enterText((EditText) solo.getView(R.id.SignUpUsername), "KappaRoss1");
        solo.enterText((EditText) solo.getView(R.id.SignUpName), "Johnny Jim");
        solo.enterText((EditText) solo.getView(R.id.SignUpPhoneNumber), "123-456-7890");
        solo.enterText((EditText) solo.getView(R.id.SignUpEmail), "kappa@ross.ca");
        solo.enterText((EditText) solo.getView(R.id.SignUpVehicleInfo), "BMX");
        solo.enterText((EditText) solo.getView(R.id.SignUpPassword), "123");
        solo.enterText((EditText) solo.getView(R.id.SignUpConfirmPassword), "123");
        solo.clickOnButton("Confirm");

        // Create User #2
        solo.clickOnButton("Create\nNew User");
        solo.enterText((EditText) solo.getView(R.id.SignUpUsername), "joker");
        solo.enterText((EditText) solo.getView(R.id.SignUpName), "Calvin Smith");
        solo.enterText((EditText) solo.getView(R.id.SignUpPhoneNumber), "329-446-7770");
        solo.enterText((EditText) solo.getView(R.id.SignUpEmail), "csmith@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.SignUpVehicleInfo), "Lambo");
        solo.enterText((EditText) solo.getView(R.id.SignUpPassword), "hello");
        solo.enterText((EditText) solo.getView(R.id.SignUpConfirmPassword), "hello");
        solo.clickOnButton("Confirm");

        // Create User #3
        solo.clickOnButton("Create\nNew User");
        solo.enterText((EditText) solo.getView(R.id.SignUpUsername), "brody");
        solo.enterText((EditText) solo.getView(R.id.SignUpName), "Brody Ace");
        solo.enterText((EditText) solo.getView(R.id.SignUpPhoneNumber), "196-181-8890");
        solo.enterText((EditText) solo.getView(R.id.SignUpEmail), "brody@yahoo.ca");
        solo.enterText((EditText) solo.getView(R.id.SignUpVehicleInfo), "");
        solo.enterText((EditText) solo.getView(R.id.SignUpPassword), "password");
        solo.enterText((EditText) solo.getView(R.id.SignUpConfirmPassword), "password");
        solo.clickOnButton("Confirm");

        // Create User #4
        solo.clickOnButton("Create\nNew User");
        solo.enterText((EditText) solo.getView(R.id.SignUpUsername), "jelly");
        solo.enterText((EditText) solo.getView(R.id.SignUpName), "kelly chin");
        solo.enterText((EditText) solo.getView(R.id.SignUpPhoneNumber), "310-101-0101");
        solo.enterText((EditText) solo.getView(R.id.SignUpEmail), "peanut@butter.ca");
        solo.enterText((EditText) solo.getView(R.id.SignUpVehicleInfo), "");
        solo.enterText((EditText) solo.getView(R.id.SignUpPassword), "apples23");
        solo.enterText((EditText) solo.getView(R.id.SignUpConfirmPassword), "apples23");
        solo.clickOnButton("Confirm");

        // Create User #5
        solo.clickOnButton("Create\nNew User");
        solo.enterText((EditText) solo.getView(R.id.SignUpUsername), "terminator_23");
        solo.enterText((EditText) solo.getView(R.id.SignUpName), "Mark Ham");
        solo.enterText((EditText) solo.getView(R.id.SignUpPhoneNumber), "783-433-7340");
        solo.enterText((EditText) solo.getView(R.id.SignUpEmail), "terminatorisawesome@gmail.ca");
        solo.enterText((EditText) solo.getView(R.id.SignUpVehicleInfo), "limo");
        solo.enterText((EditText) solo.getView(R.id.SignUpPassword), "swag123");
        solo.enterText((EditText) solo.getView(R.id.SignUpConfirmPassword), "swag123");
        solo.clickOnButton("Confirm");
    }
}
