package com.example.hw7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TripActivity extends AppCompatActivity {

    // Access a Cloud Firestore instance from your Activity
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ImageView iv_chatIcon;
    public Bundle extrasFromViewTrips;
    public TextView tv_title_singleTrip;
    public TextView tv_description_singleTrip;
    public TextView tv_location_singleTrip;
    public TextView tv_date_singleTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        setTitle("Join Trip");

        extrasFromViewTrips = getIntent().getExtras().getBundle("bundleData");

        final Trip selectedTrip = (Trip) extrasFromViewTrips.getSerializable("selectedTrip");

        addFragment(new SingleMapFragment(selectedTrip), false, "one");

        tv_title_singleTrip = findViewById(R.id.tv_title_singleTrip);
        tv_description_singleTrip = findViewById(R.id.tv_description_join);
        tv_location_singleTrip = findViewById(R.id.tv_location_join);
        tv_date_singleTrip = findViewById(R.id.tv_date_join);

        db.collection("trips").document(selectedTrip.getTripId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                tv_title_singleTrip.setText(selectedTrip.getTitle());
                tv_description_singleTrip.setText(selectedTrip.getDescription());
                tv_location_singleTrip.setText(selectedTrip._city);
                tv_date_singleTrip.setText(selectedTrip.get_date());

            }
        });
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.container_frame_join, fragment, tag);
        ft.commitAllowingStateLoss();
    }
}
