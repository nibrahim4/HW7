package com.example.hw7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewTripsActivity extends AppCompatActivity {

    public ListView lv_trips;
    // Access a Cloud Firestore instance from your Activity
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String TAG = "demo";
    public ArrayList<Trip> trips = new ArrayList<Trip>();
    public FirebaseAuth mAuth;
    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trips);
        setTitle("Your Trips");


        lv_trips = findViewById(R.id.lv_trips);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        db.collection("trips").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        ArrayList<User> friends = (ArrayList<User>) document.get("_friends");
                        for (int i=0; i<friends.size(); i++){
                            if(document.toObject(Trip.class).getFriends().get(i).getUserId().equals(userId)){
                                Trip trip = new Trip(document.getData());
                                trips.add(trip);
                            }
                           // Log.d(TAG, document.getId() + " => " + document.toObject(Trip.class).getFriends().get(i).getUserId());
                        }

                    }

                    final TripAdapter ad = new TripAdapter(ViewTripsActivity.this,
                            android.R.layout.simple_list_item_1, trips);

                    // give adapter to ListView UI element to render
                    lv_trips.setAdapter(ad);

                    lv_trips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intentToViewTrip = new Intent(ViewTripsActivity.this, TripActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("selectedTrip", trips.get(i));
                            intentToViewTrip.putExtra("bundleData", bundle);
                            startActivity(intentToViewTrip);
                        }
                    });

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
