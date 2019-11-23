package com.example.hw7;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

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
public class MapFragment extends Fragment  {

    public ArrayList<LatLng> latLngArrayList = new ArrayList<>();
    public LatLng selectedLatLng;
    public String selectedTripLocation;

    public MapFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                // San Francisco, CA
                LatLng sanFranCoor = new LatLng(37.7749, -122.4194);
                latLngArrayList.add(sanFranCoor);
                mMap.addMarker(new MarkerOptions()
                        .position(sanFranCoor)
                       // .icon(bitmapDescriptorFromVector(getActivity(),R.drawable.common_google_signin_btn_icon_dark))
                        .title("San Francisco, CA"))
                        .setTag("San Francisco");

                // New York, NY
                LatLng newYorkCoor = new LatLng(40.7128, -74.0060);
                latLngArrayList.add(newYorkCoor);
                mMap.addMarker(new MarkerOptions()
                        .position(newYorkCoor)
                        .title("New York, NY"))
                        .setTag("New York");

                // Atlanta, GA
                LatLng atlCoor = new LatLng(33.7490, -84.3880);
                latLngArrayList.add(newYorkCoor);
                mMap.addMarker(new MarkerOptions()
                        .position(atlCoor)
                        .title("Atlanta, GA"))
                        .setTag("Atlanta");

                // Charlotte, NC
                LatLng charCoor = new LatLng( 35.2271,-80.8431);
                latLngArrayList.add(charCoor);
                mMap.addMarker(new MarkerOptions()
                        .position(charCoor)
                        .title("Charlotte, NC"))
                        .setTag("Charlotte");

                // Los Angeles, CA
                LatLng laxCoor = new LatLng( 34.0522,-118.2437);
                latLngArrayList.add(charCoor);
                mMap.addMarker(new MarkerOptions()
                        .position(laxCoor)
                        .title("Los Angeles, CA"))
                        .setTag("Los Angeles");

                // Chicago, IL
                LatLng chiCoor = new LatLng( 41.8781,-87.6298);
                latLngArrayList.add(chiCoor);
                mMap.addMarker(new MarkerOptions()
                        .position(chiCoor)
                        .title("Chicago, IL"))
                        .setTag("Chicago");

                // Miami, FL
                LatLng miamiCoor = new LatLng( 25.7617,-80.1918);
                latLngArrayList.add(miamiCoor);
                mMap.addMarker(new MarkerOptions()
                        .position(miamiCoor)
                        .title("Miami, FL"))
                        .setTag("Miami");

                LatLngBounds.Builder latlngbuilder = new LatLngBounds.Builder();
                for (LatLng latLng : latLngArrayList) {
                    latlngbuilder.include(latLng);
                }

                LatLngBounds bounds = latlngbuilder.build();
                mMap.setLatLngBoundsForCameraTarget(bounds);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,20));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        switch (marker.getTag().toString()){
                            case "Atlanta":
                                selectedTripLocation = "Atlanta, GA";
                                break;
                            case "New York":
                                selectedTripLocation = "New York, NY";
                                break;
                            case "Chicago":
                                selectedTripLocation = "Chicago, IL";
                                break;
                            case "Miami":
                                selectedTripLocation = "Miami, FL";
                                break;
                            case "Charlotte":
                                selectedTripLocation = "Charlotte, NC";
                                break;
                            case "Los Angeles":
                                selectedTripLocation = "Los Angeles, LA";
                                break;
                            case "San Francisco":
                                selectedTripLocation = "San Francisco, CA";
                                break;
                            default:
                                break;
                        }

                        selectedLatLng = marker.getPosition();
                        return false;
                    }
                });
            }
        });


        return rootView;
    }
}
