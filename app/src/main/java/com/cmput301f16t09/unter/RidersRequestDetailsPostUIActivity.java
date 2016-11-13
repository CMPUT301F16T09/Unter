package com.cmput301f16t09.unter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RidersRequestDetailsPostUIActivity extends AppCompatActivity {

    private PostList postList = new PostList();
    private ListView currentPostList;
    private Button complete_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riders_request_details_post_ui);

        TextView tvCurrentStatus = (TextView) findViewById(R.id.RideRequestDetailsPostCurrentStatus);
        String currentStatus = CurrentUser.getCurrentPost().getStatus().toString();
        tvCurrentStatus.setText(currentStatus);

        TextView tvStartLocation = (TextView) findViewById(R.id.RideRequestDetailsPostStartLocationName);
        String startLocation = CurrentUser.getCurrentPost().getStartLocation().toString();
        tvStartLocation.setText(startLocation);

        TextView tvEndLocation = (TextView) findViewById(R.id.RideRequestDetailsPostEndLocationName);
        String endLocation = CurrentUser.getCurrentPost().getEndLocation().toString();
        tvEndLocation.setText(endLocation);

        TextView tvOfferedFare = (TextView) findViewById(R.id.RideRequestDetailsPostOfferedFare);
        String offeredFare = CurrentUser.getCurrentPost().getFare().toString();
        tvOfferedFare.setText(offeredFare);

        TextView tvDriverName = (TextView) findViewById(R.id.RideRequestDetailsPostDriverName);
        final String driverName = CurrentUser.getCurrentPost().getDriver().toString();
        tvDriverName.setText(driverName);

        tvDriverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RidersRequestDetailsPostUIActivity.this,ViewProfileUIActivity.class);
                intent.putExtra("User",driverName);
                startActivity(intent);
            }
        });

        complete_request = (Button) findViewById(R.id.RideRequestDetailsCompleteRequestButton);
        complete_request.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder paymentDialog = new AlertDialog.Builder(RidersRequestDetailsPostUIActivity.this);
                paymentDialog.setTitle("Payment Options");
                paymentDialog.setMessage("$"+ "  " + CurrentUser.getCurrentPost().getFare().toString()); //make this to two decimal places
                paymentDialog.setCancelable(false);

                paymentDialog.setPositiveButton("Complete Transaction", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //int index = CurrentUser.getCurrentUser().getMyRequests().getPosts().indexOf(CurrentUser.getCurrentPost());
                        //CurrentUser.getCurrentUser().getMyRequests().getPosts().get(index).setStatus("Completed");
                       // CurrentUser.getCurrentUser().getMyRequests().getPosts().remove(CurrentUser.getCurrentPost());
                        CurrentUser.getCurrentPost().setStatus("Completed");

                        try {
                            PostListOnlineController.UpdatePostsTask upt = new PostListOnlineController.UpdatePostsTask();
                            upt.execute(CurrentUser.getCurrentPost());
                            upt.get();


                            UserListOnlineController.UpdateUsersTask uut = new UserListOnlineController.UpdateUsersTask();
                            uut.execute(CurrentUser.getCurrentUser());
                            uut.get();

                            Toast.makeText(RidersRequestDetailsPostUIActivity.this, "Completed request!!", Toast.LENGTH_SHORT).show();
                            //adapter.notifyDataSetChanged();

                            Intent intent = new Intent(RidersRequestDetailsPostUIActivity.this,MainScreenUIActivity.class);
                            startActivity(intent);

                        }
                        catch (Exception e) {
                            Log.i("Error", "Unable to Update Post/User Information");
                        }
                    }
                });
                paymentDialog.show();
            }
        });
    }
}
