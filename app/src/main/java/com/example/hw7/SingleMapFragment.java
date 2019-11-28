package com.example.hw7;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.GeoPoint;
import com.google.protobuf.DescriptorProtos;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleMapFragment extends Fragment {

    public ArrayList<LatLng> latLngArrayList = new ArrayList<>();
    public LatLng selectedLatLng;
    public String selectedTripLocation;
    public Trip selectedTrip;
    public String TAG = "demo";

    public SingleMapFragment(Trip trip) {
        selectedTrip = trip;
    }

    public SingleMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_single_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.single_frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                Log.d(TAG, "selectedTrip._latitude,: " + selectedTrip._latitude);
                double latitude = Double.valueOf(selectedTrip._latitude);
                double longitude = Double.valueOf(selectedTrip._longitude);
                LatLng coor = new LatLng( latitude, longitude);
                latLngArrayList.add(coor);
                mMap.addMarker(new MarkerOptions()
                        .position(coor)
                        .title(selectedTrip._city));

                LatLngBounds.Builder latlngbuilder = new LatLngBounds.Builder();
                for (LatLng latLng : latLngArrayList) {
                    latlngbuilder.include(latLng);
                }

                LatLngBounds bounds = latlngbuilder.build();
                mMap.setLatLngBoundsForCameraTarget(bounds);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,10));
            }
        });


        return rootView;
    }
}
