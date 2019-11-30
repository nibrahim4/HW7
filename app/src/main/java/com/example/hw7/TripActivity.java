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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TripActivity extends AppCompatActivity {

    // Access a Cloud Firestore instance from your Activity
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ImageView iv_chatIcon;
    public Bundle extrasFromViewTrips;
    public TextView tv_title_singleTrip;
    public TextView tv_description_singleTrip;
    public TextView tv_location_singleTrip;
    public TextView tv_date_singleTrip;
    public Button btn_joinTrip;
    public ArrayList<User> friends = new ArrayList<User>();
    public FirebaseAuth mAuth;
    public String userId;

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
        btn_joinTrip = findViewById(R.id.btn_join);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

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

        btn_joinTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friends = selectedTrip.getFriends();

//                db.collection("users").document(userId)
//
//                friends.add()
//                selectedTrip.setFriends();
//                db.collection("trips").document(selectedTrip.getTripId())
//                        .update("_friends", )
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
