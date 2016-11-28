package com.cmput301f16t09.unter;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by alvin on 11/12/2016.
 */
public class EditProfileUIActivityTest extends ActivityInstrumentationTestCase2<MainGUIActivity>{
    private Solo solo;
    public EditProfileUIActivityTest() {
        super(com.cmput301f16t09.unter.MainGUIActivity.class);
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void setUp() throws Exception {
        Log.d("TAG9","setUp()");
        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void testEditProfile() {
        solo.enterText((EditText) solo.getView(R.id.mainScreenUsername), "KappaRoss");
        solo.enterText((EditText) solo.getView(R.id.mainScreenPassword), "123");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.clickOnMenuItem("Edit Profile");
        solo.assertCurrentActivity("Wrong Activity", EditProfileUIActivity.class);

        solo.clearEditText((EditText) solo.getView(R.id.EditProfileNameField));
        solo.enterText((EditText) solo.getView(R.id.EditProfileNameField), "Bob Ross");
        solo.waitForText("Bob Ross");

        solo.clearEditText((EditText) solo.getView(R.id.EditProfilePhoneNumberField));
        solo.enterText((EditText) solo.getView(R.id.EditProfilePhoneNumberField), "2345678901");
        solo.waitForText("2345678901");

        solo.clearEditText((EditText) solo.getView(R.id.EditProfileEmailField));
        solo.enterText((EditText) solo.getView(R.id.EditProfileEmailField), "Kappa@Ross.com");
        solo.waitForText("Kappa@Ross.com");

        solo.clearEditText((EditText) solo.getView(R.id.EditProfileVehicleInfoField));
        solo.enterText((EditText) solo.getView(R.id.EditProfileVehicleInfoField), "Permobile");
        solo.waitForText("Permobile");

        solo.clearEditText((EditText) solo.getView(R.id.EditProfilePasswordField));
        solo.enterText((EditText) solo.getView(R.id.EditProfilePasswordField), "123");
        solo.waitForText("123");

        solo.clickOnButton("Confirm");
        solo.waitForText("Your password did not confirm!");

        solo.clearEditText((EditText) solo.getView(R.id.EditProfileConfirmPasswordField));
        solo.enterText((EditText) solo.getView(R.id.EditProfileConfirmPasswordField), "123");
        solo.waitForText("123");

        solo.clickOnButton("Confirm");
        solo.waitForText("Your information was updated!");
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainScreenUIActivity.class);

        solo.clickOnMenuItem("Edit Profile");
        solo.assertCurrentActivity("Wrong Activity", EditProfileUIActivity.class);

        solo.goBack();
        solo.goBack();
    }
}
