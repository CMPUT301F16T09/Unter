package com.cmput301f16t09.unter;

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

        UserListOnlineController.SearchUserListsTask searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
        searchUserListsTask.execute("username", input_index.get(0));
        try {
            ArrayList<User> userlist = searchUserListsTask.get();
            if (userlist.isEmpty()) {
                searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
                searchUserListsTask.execute("email", input_index.get(3));
                userlist = searchUserListsTask.get();
                if (userlist.isEmpty()) {
                    if (input_index.get(5).equals(input_index.get(4))) {
                        User new_user = new User(input_index.get(0), input_index.get(1), input_index.get(2), input_index.get(3), input_index.get(4));
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
