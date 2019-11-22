package com.example.hw7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddTripActivity extends AppCompatActivity {


//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_trip);

//

//
////        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
////                .findFragmentById(R.id.map);
////        mapFragment.getMapAsync((OnMapReadyCallback) this);
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }

    public Button btn_AddFriends;
    public ArrayList<User> users = new ArrayList<User>();
    public String TAG = "demo";
    // Access a Cloud Firestore instance from your Activity
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ArrayList<User> selectedUsers = new ArrayList<>();
    protected CharSequence[] _users;
    public EditText et_title;
    public EditText et_description;
    public Button btn_submit;

    public FirebaseAuth mAuth;
    public String userId;
    public String tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setTitle("Add Trip");

        btn_AddFriends = findViewById(R.id.btn_addFriends);
        getListOfUsers();

        addFragment(new MapFragment(), false, "one");

        btn_AddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean[] checkedUsers = new boolean[users.size()];
                int count = users.size();

                for (int i = 0; i < count; i++) {
                    checkedUsers[i] = selectedUsers.contains(_users[i]);
                }

                DialogInterface.OnMultiChoiceClickListener receiversDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            Log.d(TAG, "Added User " + users.get(which));
                            selectedUsers.add(users.get(which));
                        } else {
                            Log.d(TAG, "Removed User " + users.get(which));
                            selectedUsers.remove(users.get(which));
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AddTripActivity.this);
                builder
                        .setTitle("Select Friends")
                        .setMultiChoiceItems(_users, checkedUsers, receiversDialogListener)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        et_title = findViewById(R.id.et_title);
        et_description = findViewById(R.id.et_descrption);
        btn_submit = findViewById(R.id.btn_submit);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        tripId = UUID.randomUUID().toString();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> tripMap = new HashMap<>();
                tripMap.put("userId", userId);
                tripMap.put("tripId", tripId);
                tripMap.put("title", et_title.getText().toString());
                tripMap.put("description", et_description.getText().toString());
                tripMap.put("friends", Arrays.asList(selectedUsers));
//                for (int i=0; i<selectedUsers.size(); i++){
//                    tripMap.put("friends", (User) selectedUsers.get(i));
               // }
                db.collection("trips").document(tripId)
                        .set(tripMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                }
            //}
        });

    }

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.container_frame_back, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    public void getListOfUsers() {
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = new User(document.getData());
                        users.add(user);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }

                    _users = new CharSequence[users.size()];
                    for (int i = 0; i < users.size(); i++) {
                        _users[i] = users.get(i).firstName + " " + users.get(i).lastName;
                    }
                }
            }
        });
    }
}
