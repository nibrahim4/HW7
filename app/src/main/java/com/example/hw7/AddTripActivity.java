package com.example.hw7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.util.Util;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddTripActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener{
    public Button btn_AddFriends;
    public ArrayList<User> users = new ArrayList<User>();
    public String TAG = "demo";
    // Access a Cloud Firestore instance from your Activity
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseMessaging fm;
    public ArrayList<User> selectedUsers = new ArrayList<>();
    protected CharSequence[] _users;
    public EditText et_title;
    public EditText et_description;
    public Button btn_submit;
    public EditText et_date;
    public ImageView iv_coverPhoto;
    public FirebaseAuth mAuth;
    public String userId;
    public String tripId;
    public int REQ_CODE = 5;
    public String selectedCity;
    public double latitude;
    public double longitude;
    public FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public StorageReference storageReference = firebaseStorage.getReference();
    public String coverPhotoUrl;
    public int selectedCoverPhoto;
    public Bitmap bMap;
    public boolean isCoverPhotoUpload = false;

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
        et_date = findViewById(R.id.et_date);
        iv_coverPhoto = findViewById(R.id.iv_coverPhoto);


        iv_coverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToCoverPhoto = new Intent(AddTripActivity.this, CoverPhotoLibrary.class);
                startActivityForResult(intentToCoverPhoto, REQ_CODE);
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        tripId = UUID.randomUUID().toString();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateValue = et_date.getText().toString();
                boolean isErrorThrown = false;
                boolean incorrectDate = false;
                try {
                    new SimpleDateFormat("MM/dd/yyyy").parse(dateValue);
                     incorrectDate = false;
                } catch (ParseException e) {
                    incorrectDate = true;
                }

                Log.d(TAG, "coverPhotoUrl: " + coverPhotoUrl);
                Log.d(TAG, "et_date: " + et_date.getText());
                if(et_title.getText().toString().equals("")){
                    et_title.setError("Please enter a valid title!");
                    isErrorThrown = true;
                }else if(et_description.getText().toString().equals("")) {
                    et_description.setError("Please enter a valid description!");
                    isErrorThrown = true;
                }else if(selectedCity == null) {
                    isErrorThrown = true;
                    Toast.makeText(AddTripActivity.this, "Please select a valid city" +
                            " for you trip!", Toast.LENGTH_SHORT).show();
                }else if(et_date.getText().toString().equals("") || incorrectDate) {
                    isErrorThrown = true;
                    et_date.setError("Please enter a valid date MM/DD/YYYY");
                } else if(!isCoverPhotoUpload) {
                   Toast.makeText(AddTripActivity.this, "Please select a cover photo.",
                           Toast.LENGTH_SHORT).show();
                }
                //{

                if(isCoverPhotoUpload && !isErrorThrown) {
                    Trip trip = new Trip(userId,
                            tripId,
                            et_title.getText().toString(),
                            et_description.getText().toString(),
                            selectedUsers,
                            dateValue,
                            selectedCity,
                            String.valueOf(latitude),
                            String.valueOf(longitude),
                            coverPhotoUrl);

                    db.collection("trips").document(tripId).set(trip);

                    finish();
                    //  }
                }
            }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + data);
        if (requestCode == REQ_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (data.getExtras().getSerializable("coverPhoto").equals("alaska")) {
                    selectedCoverPhoto = R.drawable.alaska;
                    iv_coverPhoto.setImageResource(R.drawable.alaska);
                } else if (data.getExtras().getSerializable("coverPhoto").equals("borabora")) {
                    selectedCoverPhoto = R.drawable.borabora;
                    iv_coverPhoto.setImageResource(R.drawable.borabora);
                } else if (data.getExtras().getSerializable("coverPhoto").equals("cappadocia")) {
                    selectedCoverPhoto = R.drawable.cappadocia;
                    iv_coverPhoto.setImageResource(R.drawable.cappadocia);
                } else if (data.getExtras().getSerializable("coverPhoto").equals("cavin")) {
                    selectedCoverPhoto = R.drawable.cavin;
                    iv_coverPhoto.setImageResource(R.drawable.cavin);
                } else if (data.getExtras().getSerializable("coverPhoto").equals("colombia")) {
                    selectedCoverPhoto = R.drawable.colombia;
                    iv_coverPhoto.setImageResource(R.drawable.colombia);
                } else if (data.getExtras().getSerializable("coverPhoto").equals("grandCanyon")) {
                    selectedCoverPhoto = R.drawable.grandcanyonofthecoloradoar;
                    iv_coverPhoto.setImageResource(R.drawable.grandcanyonofthecoloradoar);
                } else if (data.getExtras().getSerializable("coverPhoto").equals("snowboard")) {
                    selectedCoverPhoto = R.drawable.snowboard;
                    iv_coverPhoto.setImageResource(R.drawable.snowboard);
                }

                bMap = BitmapFactory.decodeResource(getResources(), selectedCoverPhoto);
                uploadImage(bMap);
            }
        }
    }

    @Override
    public void goToPreviousFragment(String selectedLocation, double latitude, double longitude) {
        this.selectedCity = selectedLocation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //UPLOAD IMAGE TO CLOUD
    private void uploadImage( Bitmap photoBitmap) {

        final StorageReference avatarRepo = storageReference.child("coverPhotos/" + tripId +".png");

//        Converting the Bitmap into a bytearrayOutputstream....
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = avatarRepo.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: " + "Image Uploaded!!!");
            }
        });


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                return null;
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return avatarRepo.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "task.getResult().toString(): " + task.getResult().toString());
                    coverPhotoUrl = task.getResult().toString();
                    isCoverPhotoUpload = true;
                }else{
                    Toast.makeText(AddTripActivity.this, "Please upload a cover photo!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
