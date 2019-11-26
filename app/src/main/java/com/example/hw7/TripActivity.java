package com.example.hw7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        extrasFromViewTrips = getIntent().getExtras().getBundle("bundleData");

        final Trip selectedTrip = (Trip) extrasFromViewTrips.getSerializable("selectedTrip");

        tv_title_singleTrip = findViewById(R.id.tv_title_singleTrip);

        db.collection("trips").document(selectedTrip.getTripId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                tv_title_singleTrip.setText(selectedTrip.getTitle());

            }
        });

        iv_chatIcon = findViewById(R.id.iv_chatIcon);
        iv_chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToChatRoom = new Intent(TripActivity.this, ChatRoomActivity.class);
                startActivity(intentToChatRoom);
            }
        });



    }
}
