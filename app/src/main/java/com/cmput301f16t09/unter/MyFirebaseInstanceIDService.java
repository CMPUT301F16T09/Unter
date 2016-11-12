package com.cmput301f16t09.unter;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.core.Index;

/**
 * Created by Salim Simon Akl on 2016-11-09.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = "MyFirebaseInsIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //Get updated token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "New Token: " + refreshedToken);

        //You can save the token into third party server to do anything you want

        //sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        //TODO send token to elasticsearch
        JestDroidClient client;

        DroidClientConfig.Builder builder = new DroidClientConfig.Builder
                ("http://cmput301.softwareprocess.es:8080");
        DroidClientConfig config = builder.build();

        JestClientFactory factory = new JestClientFactory();
        factory.setDroidClientConfig(config);
        client = (JestDroidClient) factory.getObject();

        Index index = new Index.Builder(refreshedToken).index("t09").type("token").build();

        try{
            client.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}