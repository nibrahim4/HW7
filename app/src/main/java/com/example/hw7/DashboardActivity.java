package com.example.hw7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {

    public Button btn_listOfUsers;
    public Button btn_addTrip;
    public Button btn_viewTrips;
    public Button btn_editProfile;
    public Bundle extrasFromMain;
    public String TAG = "demo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setTitle("Home Page");


        extrasFromMain = getIntent().getExtras().getBundle("bundleData");

        final String userId = (String) extrasFromMain.getSerializable("userId");

        btn_listOfUsers = findViewById(R.id.btn_ListOfUsers);
        btn_listOfUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToUsers = new Intent(DashboardActivity.this, UsersActivity.class);
                startActivity(intentToUsers);
            }
        });

        btn_addTrip = findViewById(R.id.btn_addTrip);
        btn_addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToAddTrip = new Intent(DashboardActivity.this, AddTripActivity.class);
                startActivity(intentToAddTrip);
            }
        });

        btn_viewTrips = findViewById(R.id.btn_trips);
        btn_viewTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToViewTrip = new Intent(DashboardActivity.this, ViewTripsActivity.class);
                startActivity(intentToViewTrip);
            }
        });

        btn_editProfile = findViewById(R.id.btn_editProfile);
        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToEditProfile = new Intent(DashboardActivity.this, EditProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                intentToEditProfile.putExtra("bundleData", bundle);
                startActivity(intentToEditProfile);
            }
        });
    }
}
