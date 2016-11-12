package com.cmput301f16t09.unter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateNewUserUIActivity extends AppCompatActivity {

    // Can probably change this to two EditText?
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

    public void create_user(View v) {

        Boolean foundEmail = false;
        // Fix up later if needed
        username = (EditText) findViewById(R.id.editTextSignUpUsername);
        name = (EditText) findViewById(R.id.editTextSignUpName);
        phone_number = (EditText) findViewById(R.id.editTextSignUpPhoneNumber);
        email = (EditText) findViewById(R.id.editTextSignUpEmail);
        password = (EditText) findViewById(R.id.editTextSignUpPassword);
        confirm_password = (EditText) findViewById(R.id.editTextSignUpConfirmPassword);
        input_index.add(0, username.getText().toString());
        input_index.add(1, name.getText().toString());
        input_index.add(2, phone_number.getText().toString());
        input_index.add(3, email.getText().toString());
        input_index.add(4, password.getText().toString());
        input_index.add(5, confirm_password.getText().toString());

        try {
            UserListOnlineController.SearchUserListsTask searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
            searchUserListsTask.execute("username", input_index.get(0));
            ArrayList<User> userlist = searchUserListsTask.get();
            if (userlist.isEmpty()) {
                searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
                searchUserListsTask.execute("email", input_index.get(3));
                userlist = searchUserListsTask.get();
                // Searching emails requires regex to get the correct results, so looping through the results is an easy way out.
                // if (!userlist.contains(input_index.get(3))) {
                // Userlist is a array of users, can't use contains with string?
                // Use this possibily if desired, but userlist.isEmpty() is better (probably)
                // if (!userlist.get(0).getEmail().equals(input_index.get(3).toLowerCase())) {
                if (userlist.isEmpty()) {
                    if (input_index.get(5).equals(input_index.get(4))) {
                        User new_user = new User(input_index.get(1), input_index.get(0), input_index.get(3), input_index.get(2), input_index.get(4));
                        UserListOnlineController.AddUsersTask addUserTask = new UserListOnlineController.AddUsersTask();
                        addUserTask.execute(new_user);
                        addUserTask.get();
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
