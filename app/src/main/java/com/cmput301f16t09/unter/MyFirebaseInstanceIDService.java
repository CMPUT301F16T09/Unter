package com.cmput301f16t09.unter;

/**
 * Copyright 2016 Google Inc. All Rights Reserved

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.core.Index;

/**
 * The type My firebase instance id service.
 * Generates a token for each user and sends it to elasticsearch server
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG = "MyFirebaseInsIDService";


    /**
     * Logs the new token
     */
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //Get updated token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "New Token: " + refreshedToken);

        //You can save the token into third party server to do anything you want
        //sendRegistrationToServer(refreshedToken);
    }

    /**
     * Sends a generated token to elasticsearch
     * @param refreshedToken
     */
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