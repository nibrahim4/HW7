package com.example.hw7;

import android.content.Context;
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

import java.util.List;

public class TripAdapter extends ArrayAdapter<Trip> {

    public String TAG = "demo";
    public FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public StorageReference storageReference = firebaseStorage.getReference();

    public TripAdapter(@NonNull Context context, int resource, @NonNull List<Trip> trips) {
        super(context, resource, trips);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Trip trip = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trip_item, parent, false);
        }

        TextView tv_title_trips = convertView.findViewById(R.id.tv_title_trips);
        tv_title_trips.setText(trip.getTitle());
//
//        TextView tv_lastName = convertView.findViewById(R.id.tv_user_lastName);
//        tv_lastName.setText(user.lastName);
//
//        TextView tv_gender = convertView.findViewById(R.id.tv_user_gender);
//        tv_gender.setText(user.gender);
//
//        TextView tv_email = convertView.findViewById(R.id.tv_user_email);
//        tv_email.setText(user.emailAddress);
//
//        ImageView iv_avatar = convertView.findViewById(R.id.iv_user_avatar);
//
//        Log.d(TAG, "getView: " + user.url);
//        StorageReference sf = storageReference.child("avatars").child(user.userId + ".png");

        return convertView;
    }
}
