package com.cmput301f16t09.unter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RidersRequestDetailsPostUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private ListView currentPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riders_request_details_post_ui);

        TextView tvCurrentStatus = (TextView) findViewById(R.id.RideRequestDetailsPostCurrentStatus);
        String currentStatus = CurrentUser.getPost().getStatus().toString();
        tvCurrentStatus.setText(currentStatus);

        TextView tvStartLocation = (TextView) findViewById(R.id.RideRequestDetailsPostStartLocationName);
        String startLocation = CurrentUser.getPost().getStartLocation().toString();
        tvStartLocation.setText(startLocation);

        TextView tvEndLocation = (TextView) findViewById(R.id.RideRequestDetailsPostEndLocationName);
        String endLocation = CurrentUser.getPost().getEndLocation().toString();
        tvEndLocation.setText(endLocation);

        TextView tvOfferedFare = (TextView) findViewById(R.id.RideRequestDetailsPostOfferedFare);
        String offeredFare = CurrentUser.getPost().getFare().toString();
        tvOfferedFare.setText(offeredFare);

        TextView tvDriverName = (TextView) findViewById(R.id.RideRequestDetailsPostDriverName);
        String driverName = CurrentUser.getPost().getDriver().toString();
        tvDriverName.setText(driverName);

    }


}
