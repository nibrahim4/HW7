package com.example.hw7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ViewMyTripActivity extends AppCompatActivity {

    public TextView tv_title_view_myTrip;
    public TextView tv_description_view_myTrip;
    public TextView tv_date_view_myTrip;
    public TextView tv_location_view_myTrip;
    public ImageView iv_deleteTrip;
    public ImageView iv_chatTrip;
    public ImageView iv_coverPhoto_view_myTrip;
    public Bundle extrasFromMyTrips;
    // Access a Cloud Firestore instance from your Activity
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_trip);
        setTitle("My Trip");

        tv_title_view_myTrip = findViewById(R.id.tv_title_view_myTrip);
        tv_description_view_myTrip = findViewById(R.id.tv_description_view_myTrip);
        iv_deleteTrip = findViewById(R.id.iv_delete_myTrip);
        iv_chatTrip = findViewById(R.id.iv_chat_myTrip);
        iv_coverPhoto_view_myTrip = findViewById(R.id.imageView_view_myTrip);
        tv_date_view_myTrip = findViewById(R.id.tv_date_view_myTrip);
        tv_location_view_myTrip = findViewById(R.id.tv_location_view_myTrip);

        extrasFromMyTrips = getIntent().getExtras().getBundle("bundleData");

        final Trip selectedTrip = (Trip) extrasFromMyTrips.getSerializable("selectedTrip");

        addFragment(new SingleMapFragment(selectedTrip,R.layout.fragment_single_map, R.id.single_frg), false, "one");

        tv_title_view_myTrip.setText(selectedTrip.getTitle());
        tv_description_view_myTrip.setText(selectedTrip.getDescription());
        tv_date_view_myTrip.setText(selectedTrip.get_date());
        tv_location_view_myTrip.setText(selectedTrip._city);

        Picasso.get().load(selectedTrip.get_url()).into(iv_coverPhoto_view_myTrip);

        iv_deleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("trips").document(selectedTrip.getTripId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        setResult(ViewMyTripActivity.RESULT_OK);
                        finish();
                    }
                });
            }
        });

        iv_chatTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToChat = new Intent(ViewMyTripActivity.this, ChatRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedTrip", selectedTrip);
                intentToChat.putExtra("bundleData", bundle);
                startActivity(intentToChat);
            }
        });
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.container_frame_view_myTrip, fragment, tag);
        ft.commitAllowingStateLoss();
    }
}
