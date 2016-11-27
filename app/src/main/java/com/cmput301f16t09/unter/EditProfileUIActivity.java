package com.cmput301f16t09.unter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;

/**
 * This activity is a 'View' used to display the current user info
 * and allow edits to the current user's information.
 * Note: Usernames cannot be changed.
 * It can be navigated to via the 'hamburger' buttons on the top right.
 */
public class EditProfileUIActivity extends AppCompatActivity {

    /**
     * An android widget displaying the Name field of the current user.
     * Names are of type String.
     * @see CurrentUser
     * @see User
    */
    private EditText editName;
    /**
     * An android widget displaying the phone number of the current user.
     * Phone numbers are of type String.
     * @see CurrentUser
     * @see User
     */
    private EditText editPhoneNumber;
    /**
     * An android widget displaying the email field of the current user.
     * Emails are of type String.
     * @see CurrentUser
     * @see User
     */
    private EditText editEmail;
    /**
     * An android widget displaying the Password field of the current user.
     * The 'password' has been set as inputType and will appear symbolically (.)
     * @see CurrentUser
     * @see User
     */
    private EditText editPassword;
    /**
     * An android widget displaying a blank to be filled in by the user.
     */
    private EditText editConfirmPassword;

    private EditText editVehicle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_ui);
        this.setTitle("Edit your Profile");
        Toast.makeText(EditProfileUIActivity.this, CurrentUser.getCurrentUser().getName(), Toast.LENGTH_SHORT).show();

        //grabs the respective data from the android widgets and stores them in their respective object representations.
        editName = (EditText) findViewById(R.id.EditProfileNameField);
        editName.setText(CurrentUser.getCurrentUser().getName());
        editPhoneNumber = (EditText) findViewById(R.id.EditProfilePhoneNumberField);
        editPhoneNumber.setText(CurrentUser.getCurrentUser().getPhoneNumber());
        editEmail = (EditText) findViewById(R.id.EditProfileEmailField);
        editEmail.setText(CurrentUser.getCurrentUser().getEmail());
        editPassword = (EditText) findViewById(R.id.EditProfilePasswordField);
        editPassword.setText(CurrentUser.getCurrentUser().getPassword());
        editConfirmPassword = (EditText) findViewById(R.id.EditProfileConfirmPasswordField);
        editVehicle = (EditText) findViewById(R.id.EditProfileVehicleInfoField);
        editVehicle.setText(CurrentUser.getCurrentUser().getVehicle());
    }


    /**
     * Update profile using Elastic search.
     *
     * @param v the view to be updated
     * @see UserListOnlineController
     */
    public void updateProfile(View v) {
        UserListOnlineController uc = new UserListOnlineController();
        String newName = editName.getText().toString();
        String newNumber = editPhoneNumber.getText().toString();
        String newEmail = editEmail.getText().toString();
        String newPassword = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        String newVehicle = editVehicle.getText().toString();

        if (newPassword.equals(confirmPassword)){
            CurrentUser.getCurrentUser().setName(newName);
            CurrentUser.getCurrentUser().setEmail(newEmail);
            CurrentUser.getCurrentUser().setPhoneNumber(newNumber);
            CurrentUser.getCurrentUser().setPassword(newPassword);
            CurrentUser.getCurrentUser().setVehicle(newVehicle);
            try {
                UserListOnlineController.UpdateUsersTask updateUserListsTask = new UserListOnlineController.UpdateUsersTask();
                updateUserListsTask.execute(CurrentUser.getCurrentUser());
                updateUserListsTask.get();


               // Toast.makeText(EditProfileUIActivity.this, "Your information was updated!" , Toast.LENGTH_SHORT).show();


            } catch(Exception e) {
                Log.i("Error", "Could not update");
                Toast.makeText(EditProfileUIActivity.this, "Could not update!" , Toast.LENGTH_SHORT).show();
            }
        }

        else {
            Toast.makeText(EditProfileUIActivity.this, "Your passwords did not confirm!" , Toast.LENGTH_SHORT).show();
        }
    }
}
