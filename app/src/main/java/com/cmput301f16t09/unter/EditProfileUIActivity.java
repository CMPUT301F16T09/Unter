package com.cmput301f16t09.unter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;

public class EditProfileUIActivity extends AppCompatActivity {

    EditText editName;
    EditText editPhoneNumber;
    EditText editEmail;
    EditText editPassword;
    EditText editConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_ui);
        Toast.makeText(EditProfileUIActivity.this, CurrentUser.getCurrentUser().getName(), Toast.LENGTH_SHORT).show();

        editName = (EditText) findViewById(R.id.editTextEditProfileName);
        editName.setText(CurrentUser.getCurrentUser().getName());
        editPhoneNumber = (EditText) findViewById(R.id.editTextEditProfilePhoneNumber);
        editPhoneNumber.setText(CurrentUser.getCurrentUser().getPhoneNumber());
        editEmail = (EditText) findViewById(R.id.editTextEditProfileEmail);
        editEmail.setText(CurrentUser.getCurrentUser().getEmail());
        editPassword = (EditText) findViewById(R.id.editTextEditProfilePassword);
        editPassword.setText(CurrentUser.getCurrentUser().getPassword());
        editConfirmPassword = (EditText) findViewById(R.id.editTextEditProfileConfirmPassword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void updateProfile() {
        UserListOnlineController uc = new UserListOnlineController();
        String newName = editName.getText().toString();
        String newNumber = editPhoneNumber.getText().toString();
        String newEmail = editEmail.getText().toString();
        String newPassword = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();

        if (newPassword.equals(confirmPassword)){
            Toast.makeText(EditProfileUIActivity.this, "Your information was updated!" , Toast.LENGTH_SHORT).show();
//                    uc.editUser(newName, newNumber, newEmail, newPassword);
        }

        else {
            Toast.makeText(EditProfileUIActivity.this, "Your password did not confirm!" , Toast.LENGTH_SHORT).show();
        }
    }
}
