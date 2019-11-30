package com.example.hw7;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatRoomAdapter extends ArrayAdapter<Message> {

    public String TAG = "demo";
    public FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public StorageReference storageReference = firebaseStorage.getReference();

    public ChatRoomAdapter(@NonNull Context context, int resource, @NonNull List<Message> messages) {
        super(context, resource, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Message message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_item, parent, false);
        }
        Log.d(TAG, "message.message " + message.message );

        if(message.message != null && !message.message.equals("") ){
            TextView tv_message = convertView.findViewById(R.id.tv_message);
            tv_message.setText(message.message + ": " + message.email + "   Date: " + message.date);
        }

        ImageView iv_coverPhoto_trips = convertView.findViewById(R.id.iv_message_photo);
        Log.d(TAG, "message.imageUrl " + message.imageUrl );

        if(message.imageUrl != null){
            iv_coverPhoto_trips.setVisibility(View.VISIBLE);
            Picasso.get().load(message.imageUrl).into(iv_coverPhoto_trips);
        }else{
            iv_coverPhoto_trips.setVisibility(View.GONE);
        }

        return convertView;
    }
}
