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
import com.squareup.picasso.Picasso;

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

        TextView tv_description_trips = convertView.findViewById(R.id.tv_description_trips);
        tv_description_trips.setText(trip.getDescription());

        TextView tv_date_trips = convertView.findViewById(R.id.tv_date_trips);
        tv_date_trips.setText(trip.get_date());

        TextView tv_location_trip = convertView.findViewById(R.id.tv_address_trips);
        tv_location_trip.setText(trip._city);

        ImageView iv_coverPhoto_trips = convertView.findViewById(R.id.iv_coverPhoto_trips);
        Log.d(TAG, "trip url: " + trip.get_url());
        Picasso.get().load(trip.get_url()).into(iv_coverPhoto_trips);

        return convertView;
    }
}
