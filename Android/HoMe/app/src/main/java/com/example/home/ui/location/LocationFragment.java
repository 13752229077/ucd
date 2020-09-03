package com.example.home.ui.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.example.home.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

/**
 * Location Fragment is responsible for allowing the user to select a location using Google Maps
 * The location is used to record the sighting of the rough sleeper
 * Note: There is no safe-guard against abuse of the location, with more time we were hoping to restrict the area
 * around the user that can be pinned
 */
public class LocationFragment extends Fragment implements LocationListener {

    private GoogleMap googleMap;
    private MapView mapView;
    private FloatingActionButton btnMyLocation, btnReturn;
    private LocationManager mLocationManager;
    private LatLng location;

    /**
     * Creating the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.location_fragment, container, false);
        mapView = root.findViewById(R.id.map);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //adding current location and return buttons, hiding the return button until pin is placed
        btnMyLocation = root.findViewById(R.id.btnMyLocation);
        btnReturn = root.findViewById(R.id.btnReturn);
        btnReturn.hide();

        //creating the mapView
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //setup the mapView where the user will be able to specify a precise location
        mapView.getMapAsync(mMap -> {
            googleMap = mMap;
            googleMap.setOnMapClickListener(this::drawMarker);
            googleMap.setOnInfoWindowClickListener(Marker::remove);
            googleMap.setMyLocationEnabled(true);
            // For showing a move to my location button
            LatLng ireland = new LatLng(53, -7);
            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(ireland).zoom(6).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });

        //add listeners to our buttons
        btnMyLocation.setOnClickListener(this::onClick);
        btnReturn.setOnClickListener(this::onClick);

        return root;
    }

    /**
     * Method to deal with our two button listeners
     * @param v
     */
    private void onClick(View v) {
        switch(v.getId()) {

            //ensure permissions have been granted when trying to access phone's location, if not, request them
            case R.id.btnMyLocation: {
                if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED && checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                else { //given permissions granted we request phone's location
                    Toast.makeText(getContext(), "Getting your location", Toast.LENGTH_LONG).show();
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 500, this);
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 500, this);
                }
                break;
            }
            //performing a fragment replacement by setting up intents and popping off the fragment manager back stack
            case R.id.btnReturn: {
                Intent intent = new Intent(getContext(), LocationFragment.class);
                DecimalFormat f = new DecimalFormat("##.0000000");
                intent.putExtra("location", f.format(location.latitude) +", "+ f.format(location.longitude));
                getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
                getTargetFragment().getFragmentManager().popBackStack();
                break;
            }
        }
    }

    /**
     * Whenever location is changed we update the CameraPosition
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(20).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Displaying toasts to inform user that their permission choice has been noted
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == 1){
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Placing a pin on the map where the user clicks ensuring previous pins are deleted if existing
     * @param point
     */
    private void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        if(btnReturn.isOrWillBeHidden()){
            btnReturn.show();
        }
        location = point;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title("Person in need of help");
        markerOptions.snippet("Click here to remove marker");
        googleMap.clear();
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
