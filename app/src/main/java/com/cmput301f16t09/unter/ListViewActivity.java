package com.cmput301f16t09.unter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Kelly on 2016-11-04.
 */
public class ListViewActivity extends AppCompatActivity {

    private ListView currUserList;
    private ArrayAdapter<User> adapter;
    UserList listOfUsers = new UserList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_requests);

 //       Bundle extras = getIntent().getExtras();

//        if (!extras.isEmpty()) {
//            //check if the bundle contains the correct keys first
//            String tryUsername = extras.getString("LoginUsername");
//            String tryPassword = extras.getString("LoginPassword");
//
//            Toast.makeText(ListViewActivity.this,tryUsername,Toast.LENGTH_SHORT).show();
//            Toast.makeText(ListViewActivity.this,tryPassword,Toast.LENGTH_SHORT).show();

//            UserListOnlineController ctrl = new UserListOnlineController();
//            boolean validLogin = ctrl.verifyLogin(tryUsername, tryPassword);
//
//            if (validLogin){
//                Toast.makeText(ListViewActivity.this, "Valid Login!!" , Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Toast.makeText(ListViewActivity.this, "Invalid Login." , Toast.LENGTH_SHORT).show();
//            }
       //}
//
        UserList listOfUsers = UserListOnlineController.getUserList();
        currUserList = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<User>(this,R.layout.list_item, listOfUsers.getUserList());
        currUserList.setAdapter(adapter);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
////        UserList listOfUsers = UserListOnlineController.getUserList();
////        adapter.notifyDataSetChanged();
//    }
}


