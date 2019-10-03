package com.example.ibank;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location currentLocation ;
    private Marker mark1 , mark2 , mark3;
    private Double distance ;
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_map,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        if (mMapView != null) {

            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        //GPS
        final LocationManager manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Ask the user to enable GPS
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Location Manager");
            builder.setMessage("Would you like to enable GPS?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Launch settings, allowing user to make a change
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //No location service, no Activity
                    getActivity().finish();
                }
            });
            builder.create().show();
        }
        else{
            getDeviceLocation(googleMap);
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.equals(mark1) || marker.equals(mark2) || marker.equals(mark3))
                {
                    if(marker.equals(mark1))
                    {
                        mark1.showInfoWindow();
                        LatLng from = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                        LatLng to = new LatLng(mark1.getPosition().latitude,mark1.getPosition().longitude);
                        distance = SphericalUtil.computeDistanceBetween(from, to);
                        //Toast.makeText(getActivity(), String.valueOf(df2.format(distance/1000d)+" Km"), Toast.LENGTH_SHORT).show();
                    }
                    else if (marker.equals(mark2))
                    {
                        mark2.showInfoWindow();
                        LatLng from = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                        LatLng to = new LatLng(mark2.getPosition().latitude,mark2.getPosition().longitude);
                        distance = SphericalUtil.computeDistanceBetween(from, to);
                        //Toast.makeText(getActivity(), String.valueOf(df2.format(distance/1000d)+" Km"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mark3.showInfoWindow();
                        LatLng from = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                        LatLng to = new LatLng(mark3.getPosition().latitude,mark3.getPosition().longitude);
                        distance = SphericalUtil.computeDistanceBetween(from, to);
                        //Toast.makeText(getActivity(), String.valueOf(df2.format(distance/1000d)+" Km"), Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    int fragint = (int) (distance/100);
                    intent.putExtra("res",fragint);
                    startActivity(intent);

                    return true ;
                }
                return false;
            }
        });

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if(currentLocation==null)
                {
                    getDeviceLocation(googleMap);
                }
                return true;
            }
        });

    }

    private void getDeviceLocation(final GoogleMap googleMap) {
        mFusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(getActivity());
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final Task location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    currentLocation = (Location) task.getResult();
                    CameraPosition position = CameraPosition.builder().target(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude())).zoom(16).bearing(0).tilt(45).build();
                    mark1 = googleMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude()+0.01,currentLocation.getLongitude()-0.01 )).title("Bank"));
                    mark2 = googleMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude()+0.001,currentLocation.getLongitude()-0.01 )).title("Bank"));
                    mark3 = googleMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude()+0.01,currentLocation.getLongitude()-0.0001 )).title("Bank"));
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                }else{
                    Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
