package com.example.hw7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    // Access a Cloud Firestore instance from your Activity
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String TAG = "demo";
    public FirebaseAuth mAuth;
    public EditText et_firstName_edit;
    public EditText et_lastName_edit;
    public RadioButton rb_female;
    public RadioButton rb_male;
    public EditText et_email_edit;
    public EditText et_password_edit;
    public Bundle extrasFromDashboard;
    public ImageView iv_selectAvatar_edit;
    public Button btn_updateProfile;
    public int REQCODE =5;
    public static final String EDIT_KEY = "avatar";
    public Bundle extrasFromSelectAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");

        extrasFromDashboard = getIntent().getExtras().getBundle("bundleData");

        final String userId = (String) extrasFromDashboard.getSerializable("userId");

        et_firstName_edit = findViewById(R.id.et_firstName_edit);
        et_lastName_edit = findViewById(R.id.et_lastName_edit);
        et_email_edit = findViewById(R.id.et_newUser_email_edit);
        et_password_edit = findViewById(R.id.et_newUser_password_edit);
        rb_female = findViewById(R.id.rb_female_edit);
        rb_male = findViewById(R.id.rb_male_edit);
        iv_selectAvatar_edit = findViewById(R.id.iv_selectAvatar_edit);
        btn_updateProfile = findViewById(R.id.btn_editProfile);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        iv_selectAvatar_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToSelectAvatar = new Intent(EditProfileActivity.this, SelectAvatarActivity.class);
                startActivityForResult(intentToSelectAvatar,REQCODE);
            }
        });
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = new User(document.getData());
                        et_firstName_edit.setText(user.getFirstName());
                        et_lastName_edit.setText(user.getLastName());
                        et_email_edit.setText(user.getEmailAddress());

                        switch (user.getGender()) {
                            case "female":
                                rb_female.setChecked(true);
                                break;
                            case "male":
                                rb_male.setChecked(true);
                                break;
                            default:
                                rb_male.setChecked(false);
                                rb_female.setChecked(false);
                                break;
                        }
                        Log.d(TAG, "url: " + user.getUrl());
                        Picasso.get().load(user.getUrl()).into(iv_selectAvatar_edit);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        btn_updateProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG, "current user: " +  userId);
                if(!et_email_edit.getText().toString().equals("") || et_email_edit.getText() != null) {
                    final Task updateEmailTask = FirebaseAuth.getInstance().getCurrentUser().updateEmail(et_email_edit.getText().toString());
                    updateEmailTask.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "email task: " + updateEmailTask.getResult());
                                db.collection("users").document(userId).update("email", et_email_edit.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Documnetsnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error updating document", e);
                                            }
                                        });
                                ;
                            }
                        }
                    });
                }
                Log.d(TAG, "password: " + et_password_edit.getText().toString());
                if(!et_password_edit.getText().toString().equals("") && et_password_edit.getText() != null){
                    Task updatePasswordTask = FirebaseAuth.getInstance().getCurrentUser().updatePassword(et_password_edit.getText().toString());
                    updatePasswordTask.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "password task: " + task.getResult());
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQCODE) {
            if (resultCode == RESULT_OK) {
                extrasFromSelectAvatar = data.getExtras().getBundle(EDIT_KEY);

                String selectedAvatarTagName = (String) extrasFromSelectAvatar.getSerializable("avatar");
                Log.d(TAG, "onActivityResult: " + data.getExtras().getSerializable("avatar"));
                if (selectedAvatarTagName != null) {
                    if (selectedAvatarTagName.equals("avatar1")) {
                        iv_selectAvatar_edit.setImageResource(R.drawable.avatar_f_3);
                    } else if (selectedAvatarTagName.equals("avatar2")) {
                        iv_selectAvatar_edit.setImageResource(R.drawable.avatar_f_2);
                    } else if (selectedAvatarTagName.equals("avatar3")) {
                        iv_selectAvatar_edit.setImageResource(R.drawable.avatar_f_1);
                    } else if (selectedAvatarTagName.equals("avatar4")) {
                        iv_selectAvatar_edit.setImageResource(R.drawable.avatar_m_1);
                    } else if (selectedAvatarTagName.equals("avatar5")) {
                        iv_selectAvatar_edit.setImageResource(R.drawable.avatar_m_2);
                    } else if (selectedAvatarTagName.equals("avatar6")) {
                        iv_selectAvatar_edit.setImageResource(R.drawable.avatar_m_3);
                    }

                }else{
                    Toast.makeText(this, "No avatar was selected!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
