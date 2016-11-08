package com.cmput301f16t09.unter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * The type Main gui activity.
 */
public class MainGUIActivity extends AppCompatActivity {

    private Activity activity = this;
    private EditText mainScreenUsername;
    private EditText mainScreenPassword;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gui);
        Button saveButton = (Button) findViewById(R.id.tester);

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
//                User new_user = new User("Henry", "Popcorn_chicken3232323", "KFC_lover@barnyard.com", "123-456-7890", "password");
//                UserListOnlineController.AddUsersTask addUserTask = new UserListOnlineController.AddUsersTask();
//                addUserTask.execute(new_user);

                mainScreenUsername = (EditText) findViewById(R.id.mainScreenUsername);
                mainScreenPassword = (EditText) findViewById(R.id.mainScreenPassword);
                String tryUserName = mainScreenUsername.getText().toString();
                String tryPassword = mainScreenPassword.getText().toString();

                //verify login here

                Intent intent = new Intent(activity, ListViewActivity.class);
//                intent.putExtra("LoginUsername",tryUserName);
//                intent.putExtra("LoginPassword",tryPassword);
                startActivity(intent);
            }
        });
    }
}
