package com.example.hw7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ChatRoomActivity extends AppCompatActivity {

    DatabaseReference reference;
    public EditText et_message;
    public ListView lv_messages;
    ArrayList<Message> messages = new ArrayList<>();
    ChatRoomAdapter adapter;
    public Button btn_sendMessage;
    public FirebaseAuth mAuth;
    public String TAG = "demo";
    public String userEmail;
    public Button btn_cancel;
    public Button btn_uploadImage;
    private static int GET_FROM_GALLERY = 3;
    public Bundle extrasFromMyTrips;
    public String userId;

    // Access a Cloud Firestore instance from your Activity
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Trip selectedTrip;
    public FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public StorageReference storageReference = firebaseStorage.getReference();
    public String imgMsgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setTitle("Chat Room");

        et_message = findViewById(R.id.et_message);
        lv_messages = findViewById(R.id.lv_messages);
        btn_sendMessage = findViewById(R.id.btn_send);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_uploadImage = findViewById(R.id.btn_uploadImage);
        adapter = new ChatRoomAdapter(this, R.layout.chat_item, messages);
        lv_messages.setAdapter(adapter);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userEmail = user.getEmail();
        userId = user.getUid();

        Log.d(TAG, "user " + user.getEmail());

        extrasFromMyTrips = getIntent().getExtras().getBundle("bundleData");

        selectedTrip = (Trip) extrasFromMyTrips.getSerializable("selectedTrip");

        db.collection("chats").orderBy("date", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qd : queryDocumentSnapshots) {
                            if (selectedTrip.getTripId().equals(qd.toObject(Message.class).tripId)) {
                                messages.add(qd.toObject(Message.class));
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }
                });

        lv_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final CollectionReference itemsRef = db.collection("chats");

                Query query = itemsRef.whereEqualTo("messageId", messages.get(i).messageId);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                itemsRef.document(document.getId()).delete();

                                if(messages.get(i).imageUrl != null && ! messages.get(i).imageUrl.equals("")){
                                    Log.d(TAG, "messages.get(i).messageId: "+ messages.get(i).messageId);
                                    storageReference.child("chat").child(messages.get(i).messageId + ".png").delete();
                                }

                                messages.remove(i);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });

        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_chatroom();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToDashboard = new Intent(ChatRoomActivity.this, DashboardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                intentToDashboard.putExtra("bundleData", bundle);
                startActivity(intentToDashboard);
            }
        });

        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

    }

    public void create_chatroom() {
        HashMap<String, Object> hashMap = new HashMap<>();
        Message message = new Message();
        message.messageId = UUID.randomUUID().toString();
        message.message = et_message.getText().toString();
        message.email = userEmail;
        message.tripId = selectedTrip.getTripId();
        Date date = new Date();
        message.date = date.toString();
        messages.add(message);

        //hashMap.put(message.messageId, messages);
        db.collection("chats").add(message);

        adapter.notifyDataSetChanged();
        et_message.setText(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                uploadImage(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //UPLOAD IMAGE TO CLOUD
    private void uploadImage(Bitmap photoBitmap) {

        final Message imgMsg = new Message();

        imgMsg.email = userEmail;
        imgMsg.messageId = UUID.randomUUID().toString();
        imgMsg.tripId = selectedTrip.getTripId();
        Date imgDate = new Date();
        imgMsg.date = imgDate.toString();

        imgMsgId = imgMsg.messageId;



        final StorageReference avatarRepo = storageReference.child("chat/" + imgMsgId + ".png");

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

                    imgMsg.imageUrl = task.getResult().toString();
                    db.collection("chats").add(imgMsg);
                    messages.add(imgMsg);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
}
