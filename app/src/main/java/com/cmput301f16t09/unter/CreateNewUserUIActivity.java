package com.cmput301f16t09.unter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This is a "Create A New User" Activity.
 * An Activity for new users to create a account for unter.
 */
public class CreateNewUserUIActivity extends AppCompatActivity {

    // Variables used to obtain/hold inputs from user.
    private EditText username;
    private EditText name;
    private EditText phone_number;
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private ArrayList<String> input_index = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user_ui);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Creates user if passes checks of not existing username or email.
     *
     * @param v the View
     */
    public void create_user(View v) {

        // Obtaining user inputs
        username = (EditText) findViewById(R.id.SignUpUsername);
        input_index.add(0, username.getText().toString());

        name = (EditText) findViewById(R.id.SignUpName);
        input_index.add(1, name.getText().toString());

        phone_number = (EditText) findViewById(R.id.SignUpPhoneNumber);
        input_index.add(2, phone_number.getText().toString().replaceAll("\\D",""));

        email = (EditText) findViewById(R.id.SignUpEmail);
        input_index.add(3, email.getText().toString());

        password = (EditText) findViewById(R.id.SignUpPassword);
        input_index.add(4, password.getText().toString());

        confirm_password = (EditText) findViewById(R.id.SignUpConfirmPassword);
        input_index.add(5, confirm_password.getText().toString());

        try {
            // Trying to search for same username
            UserListOnlineController.SearchUserListsTask searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
            searchUserListsTask.execute("username", input_index.get(0));
            ArrayList<User> userlist = searchUserListsTask.get();

            // If no user with same username exists, search for same email
            if (userlist.isEmpty()) {
                searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
                searchUserListsTask.execute("email", input_index.get(3));
                userlist = searchUserListsTask.get();

                // If no user with same email exists, create user with
                if (userlist.isEmpty()) {
                    if (input_index.get(5).equals(input_index.get(4))) {
                        User new_user = new User(input_index.get(1), input_index.get(0), input_index.get(3), input_index.get(2), input_index.get(4));
                        UserListOnlineController.AddUsersTask addUserTask = new UserListOnlineController.AddUsersTask();
                        addUserTask.execute(new_user);
                        addUserTask.get();
//                        FirebaseMessaging.getInstance().subscribeToTopic("user_"+input_index.get(0));
                        finish();
                    }
                    else {
                        Toast.makeText(CreateNewUserUIActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(CreateNewUserUIActivity.this, "Email already exists!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(CreateNewUserUIActivity.this, "Username already exist!", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            Log.i("Error", "Something went wrong");
        }
    }
}
