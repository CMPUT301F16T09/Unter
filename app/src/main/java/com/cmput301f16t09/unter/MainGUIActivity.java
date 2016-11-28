package com.cmput301f16t09.unter;

import android.content.Intent;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * A view that asks the user to input an existing users information
 * or allow them to create a new user.
 */
public class MainGUIActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gui);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Clear CurrentUser data
        CurrentUser.setCurrentUser(null);
        CurrentUser.setCurrentPost(null);
    }


    /**
     * Verify login by using the UserListOnlineController class to query elastic search.
     *
     * @param v the current android view
     * @see com.cmput301f16t09.unter.UserListOnlineController.SearchUserListsTask
     */
    public void verifyLogin(View v) {
        EditText usernameInput = (EditText) findViewById(R.id.mainScreenUsername);
        String username = usernameInput.getText().toString();

        EditText passwordInput = (EditText) findViewById(R.id.mainScreenPassword);
        String password = passwordInput.getText().toString();

        UserListOnlineController.SearchUserListsTask checkLogin = new UserListOnlineController.SearchUserListsTask();

        // Verify with UserListOnlineController before changing intent
        ArrayList<User> currentUser;

        checkLogin.execute("username", username);
        try {
            currentUser = checkLogin.get();
            if (password.equals(currentUser.get(0).getPassword())){
                Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show();
                CurrentUser.setCurrentUser(currentUser.get(0));

                HandlerThread thread = new HandlerThread("Poll Notifications");
                thread.start();
                if (thread.isAlive()) {Log.i("Polling","Polling Thread is working");}

                Intent intent = new Intent(MainGUIActivity.this, MainScreenUIActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show();
            Log.i("Error", "Loading failed");
        }
    }

    /**
     * Create new user brings the user to another activity where they can
     * create a new user.
     *
     * @param v the current android view
     * @see CreateNewUserUIActivity
     */
    public void createNewUser(View v) {
        Intent intent = new Intent(MainGUIActivity.this, CreateNewUserUIActivity.class);
        startActivity(intent);
    }

}
