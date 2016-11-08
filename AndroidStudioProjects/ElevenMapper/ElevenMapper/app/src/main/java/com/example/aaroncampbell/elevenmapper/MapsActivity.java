package com.example.aaroncampbell.elevenmapper;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.media.CamcorderProfile.get;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int STREET_LEVEL = 13;
    int BUILDING_LEVEL = 20;
    LatLng STHPNT = new LatLng(38.451, -82.593);
    Place currentPlace = null;
    String serverKey = "AIzaSyByvWvgjO88QBPOCn10BD5qI2AGBBwVw1M";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Add a Place Selection Listener
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // Handle the event
                Toast.makeText(MapsActivity.this, "Place: " + place.getAddress(), Toast.LENGTH_LONG).show();
                currentPlace = place;

                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                mMap.addMarker(new MarkerOptions().position(currentPlace.getLatLng()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace.getLatLng(), BUILDING_LEVEL));
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MapsActivity.this, "Error: ", Toast.LENGTH_LONG).show();
                Log.d("ElevenMapper", "Error: " + status);
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(14.0f);

        mMap.animateCamera(CameraUpdateFactory.zoomIn());

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "Custom Marker Click Event!", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "Custom Info Window Click Event", Toast.LENGTH_LONG).show();

                if (currentPlace != null) {
                    GoogleDirection.withServerKey(serverKey)
                            .from(currentPlace.getLatLng())
                                    .to(STHPNT).execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {

                            String status = direction.getStatus();
                            if (status.equals(RequestResult.OK)) {
                                Route route = direction.getRouteList().get(0);
                                Leg leg = route.getLegList().get(0);
                                List<Step> stepList = leg.getStepList();

                                String strDir = null;

                                for (int i = 0; i < stepList.size(); i++) {
                                    Log.d("ELEVENMAPPER-", stepList.get(i).getHtmlInstruction());
                                    strDir += stepList.get(i).getHtmlInstruction();
                                }
                                Intent intent = new Intent(MapsActivity.this, DirectionsActivity.class);
                                intent.putExtra("Directions", strDir);
                                startActivity(intent);
                            } else if (status.equals(RequestResult.NOT_FOUND)) {
                                Toast.makeText(MapsActivity.this, "Error: Directions not found!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MapsActivity.this, "Error: Cannot acquire directions.", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            Toast.makeText(MapsActivity.this, "Error: Cannot acquire directions.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        // Add a marker in SP and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(STHPNT).title("Marker in South Point").icon(BitmapDescriptorFactory.fromResource(R.mipmap.dff)).snippet("#One DFF Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(STHPNT, STREET_LEVEL));

        GoogleMap.InfoWindowAdapter infoWinAdapter = new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.layout_info_window, null);

                TextView tvAddress = (TextView)v.findViewById(R.id.tv_address);
                tvAddress.setText(getAddress());
                return v;
            }
        };

        mMap.setInfoWindowAdapter(infoWinAdapter);
    }


    public String getAddress() {
        String retVal = "Unknown";
        Geocoder geocoder;
        List<Address> addresses = null;

        geocoder = new Geocoder(this, Locale.getDefault());

        if (currentPlace == null) {

            try {
                addresses = geocoder.getFromLocation(STHPNT.latitude, STHPNT.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String knownName = addresses.get(0).getFeatureName();

            retVal = knownName + "\n" + address + "\n" + city + "\n" + state;

        } else {
            Log.d("ElevenMapper", currentPlace.getAddress().toString());
            retVal = (currentPlace.getAddress().toString());
            retVal = retVal.replace(",", "\n");
        }
        return retVal;
    }
}
