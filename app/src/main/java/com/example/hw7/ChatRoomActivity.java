package com.example.hw7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChatRoomActivity extends AppCompatActivity {

    DatabaseReference reference;
    public EditText et_message;
    public ListView lv_messages;
    ArrayList<String> messages;
    ArrayAdapter<String> adapter;
    public Button btn_sendMessage;
    public FirebaseAuth mAuth;
    public String TAG = "demo";
    public String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        et_message = findViewById(R.id.et_message);
        lv_messages = findViewById(R.id.lv_messages);
        btn_sendMessage = findViewById(R.id.btn_send);
        messages = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, messages);
        lv_messages.setAdapter(adapter);
        reference = FirebaseDatabase.getInstance().getReference().child("TripId");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userEmail = user.getEmail();
        Log.d(TAG, "user " + user.getEmail());

        reference.child("TripId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> mySet = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    mySet.add(((DataSnapshot) i.next()).getKey());
                }

                messages.clear();
                messages.addAll(mySet);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatRoomActivity.this, "Sorry network issue!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_chatroom();
            }
        });

    }


    public void request(){

    }
    public void create_chatroom(){
        HashMap<String, Object>  hashMap = new HashMap<>();
        String messageWithUserName = et_message.getText().toString() + ": " + userEmail;
        hashMap.put(et_message.getText().toString(),  userEmail);
        reference.updateChildren(hashMap);

        messages.add(messageWithUserName);
        adapter.notifyDataSetChanged();
    }
}
