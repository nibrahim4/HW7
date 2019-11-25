package com.example.hw7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    public String email;
    public String password;
    public View v_avatar;
    public ImageView iv_selectAvatar;
    public EditText et_newUser_email;
    public EditText et_newUser_password;
    public EditText et_firstName;
    public EditText et_lastName;
    public RadioButton rb_female;
    public RadioButton rb_male;
    public RadioGroup rg_gender;
    public Button btn_signUp;
    public FirebaseAuth mAuth;
    public String TAG = "demo";
    public String userId;
    // Access a Cloud Firestore instance from your Activity
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final int REQ_CODE = 5;
    public Bundle extrasFromSelectAvatar;
    public static final String SIGNUP_KEY = "avatar";
    public String selectedGender;
    public FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public StorageReference storageReference = firebaseStorage.getReference();
    public String selectedAvatarFileName;
    public User newUser = new User();
    public String url ;
    public String selectedAvatarTagName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        et_newUser_email = findViewById(R.id.et_newUser_email);
        et_newUser_password = findViewById(R.id.et_newUser_password);
        et_firstName = findViewById(R.id.et_firstName);
        et_lastName = findViewById(R.id.et_lastName);
        rg_gender = findViewById(R.id.radioGroup);
        rb_female = findViewById(R.id.rb_female);
        rb_male = findViewById(R.id.rb_male);
        btn_signUp = findViewById(R.id.btn_edit);
        iv_selectAvatar =findViewById(R.id.iv_selectAvatar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        iv_selectAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentToSelectAvatar = new Intent(SignUpActivity.this, SelectAvatarActivity.class);
                startActivityForResult(intentToSelectAvatar,REQ_CODE);
            }

        });

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_female:
                        selectedGender = "female";
                        Log.d(TAG, "clicked gender: " + selectedGender);
                        break;
                    case R.id.rb_male:
                        selectedGender = "male";
                        Log.d(TAG, "clicked gender: " + selectedGender);
                        break;
                    default:
                        break;
                }
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_newUser_email.getText().toString();
                password = et_newUser_password.getText().toString();
                final String firstName = et_firstName.getText().toString();
                final String lastName = et_lastName.getText().toString();


                Log.d(TAG, "email " + email);
                Log.d(TAG, "password " + password);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    userId = user.getUid();
                                    Toast.makeText(SignUpActivity.this, "Sign up was successful.",
                                            Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "gender: " + selectedGender);
                                    User newUser = new User(userId, firstName, lastName,email, selectedGender, null,null );
                                    addUserToDb(newUser);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });

    }

    public void addUserToDb(User newUser){
        Bitmap bMap = null;
        switch(iv_selectAvatar.getTag().toString()){
            case "avatar1":
                bMap = BitmapFactory.decodeResource(getResources(),R.drawable.avatar_f_3 );
                uploadImage(newUser, bMap);
                break;
            case "avatar2":
                bMap = BitmapFactory.decodeResource(getResources(),R.drawable.avatar_f_2);
                uploadImage(newUser, bMap);
                break;
            case "avatar3":
                bMap = BitmapFactory.decodeResource(getResources(),R.drawable.avatar_f_1);
                uploadImage(newUser, bMap);
                break;
            case "avatar4":
                bMap = BitmapFactory.decodeResource(getResources(),R.drawable.avatar_m_1);
                uploadImage(newUser, bMap);
                break;
            case "avatar5":
                bMap = BitmapFactory.decodeResource(getResources(),R.drawable.avatar_m_2);
                uploadImage(newUser, bMap);
                break;
            case "avatar6":
                bMap = BitmapFactory.decodeResource(getResources(),R.drawable.avatar_m_3);
                uploadImage(newUser, bMap);
                break;

        }


        Intent intentToDashboard = new Intent(SignUpActivity.this, DashboardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", newUser.userId);
        intentToDashboard.putExtra("bundleData", bundle);
        startActivity(intentToDashboard);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK) {
                extrasFromSelectAvatar = data.getExtras().getBundle(SIGNUP_KEY);

                selectedAvatarTagName = (String) extrasFromSelectAvatar.getSerializable("avatar");
                Log.d(TAG, "onActivityResult: " + data.getExtras().getSerializable("avatar"));
                if (selectedAvatarTagName != null) {
                    if (selectedAvatarTagName.equals("avatar1")) {
                        iv_selectAvatar.setImageResource(R.drawable.avatar_f_3);
                    } else if (selectedAvatarTagName.equals("avatar2")) {
                        iv_selectAvatar.setImageResource(R.drawable.avatar_f_2);
                    } else if (selectedAvatarTagName.equals("avatar3")) {
                        iv_selectAvatar.setImageResource(R.drawable.avatar_f_1);
                    } else if (selectedAvatarTagName.equals("avatar4")) {
                        iv_selectAvatar.setImageResource(R.drawable.avatar_m_1);
                    } else if (selectedAvatarTagName.equals("avatar5")) {
                        iv_selectAvatar.setImageResource(R.drawable.avatar_m_2);
                    } else if (selectedAvatarTagName.equals("avatar6")) {
                        iv_selectAvatar.setImageResource(R.drawable.avatar_m_3);
                    }

                }else{
                    Toast.makeText(this, "No avatar was selected!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    //UPLOAD IMAGE TO CLOUD
    private void uploadImage(final User newUser, Bitmap photoBitmap) {

        final StorageReference avatarRepo = storageReference.child("avatars/" + userId +".png");

        newUser.storagePath = avatarRepo.getPath();

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

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("userId", newUser.userId);
                    userMap.put("firstName", newUser.firstName);
                    userMap.put("lastName", newUser.lastName);
                    userMap.put("email", newUser.emailAddress);
                    userMap.put("gender", newUser.gender);
                    userMap.put("url", task.getResult().toString());

                    url = task.getResult().toString();
                    Log.d(TAG, "Image Download URL" +  url);

                    db.collection("users").document(userId)
                            .set(userMap)
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
            }
        });

    }
}
