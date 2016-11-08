package com.elevenfifty.elevenmapper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final LatLng ELEVENFIFTY = new LatLng(39.9574445, -86.1755607);
    int STREET_LEVEL = 15;
    int BUILDING_LEVEL = 20;
    Place currentPlace = null;
    //EM5
    String serverKey = "AIzaSyCahNjjAe_PW7WlQeSZvZWCYi3o-MdCT4M";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //EM4 Add a Place Selection Listener
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)  getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(MapsActivity.this, "Place: " + place.getAddress(), Toast.LENGTH_LONG).show();
                currentPlace = place;
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), STREET_LEVEL));
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MapsActivity.this, "Error: " + status, Toast.LENGTH_LONG).show();
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

        // EM2 Marker Click Listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                          @Override
                                          public boolean onMarkerClick(Marker marker) {
                                              Toast.makeText(MapsActivity.this, "Custom Marker Click Event!", Toast.LENGTH_LONG).show();
                                              return false;
                                          }
                                      }
        );
        // EM2 Info Window Click Listener
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "Custom Info Window Click Event!", Toast.LENGTH_SHORT).show();

                //EM 5 Get directions from here to Eleven Fifty Academy
                GoogleDirection.withServerKey(serverKey).from(currentPlace.getLatLng()).to(ELEVENFIFTY).execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        // Do something here
                        String status = direction.getStatus();
                        if(status.equals(RequestResult.OK)) {
                            // Get route, leg and steps
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            List<Step> stepList = leg.getStepList();
                            for (int i = 0; i < stepList.size(); i++)
                            {
                                Log.d("EM", stepList.get(i).getHtmlInstruction());
                            }
                        } else if(status.equals(RequestResult.NOT_FOUND)) {
                            Toast.makeText(MapsActivity.this, "Error: Directions Not Found", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MapsActivity.this, "Error: Cannot acquire directions.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(MapsActivity.this, "Error: Cannot acquire directions.", Toast.LENGTH_LONG).show();
                        Log.e("EM", "Error: " + t.getLocalizedMessage());
                    }
                });
                //EM5 End
            }
        });
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // EM2 mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // EM2 add a marker on the map at Eleven Fifty Academy

        mMap.addMarker(new MarkerOptions().position(ELEVENFIFTY).title("Eleven Fifty Academy").snippet("Please Tweet #EFAImpact and share your story!").icon(BitmapDescriptorFactory.fromResource(R.mipmap.mapmarkerefa)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ELEVENFIFTY, STREET_LEVEL));

        // EM3 Create a custom Info Window
        GoogleMap.InfoWindowAdapter infoWinAdapter = new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Getting view from the layout file
                View v = getLayoutInflater().inflate(R.layout.layout_info_window, null);

                TextView tvAddress = (TextView)v.findViewById(R.id.tv_address);
                tvAddress.setText(getAddress());

                return v;
            }
        };

        mMap.setInfoWindowAdapter(infoWinAdapter);
    }

    public String getAddress()
    {
        String retVal = "Unknown";
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        // if statement is EM4 addition
        if (currentPlace == null) {
            try {
                addresses = geocoder.getFromLocation(ELEVENFIFTY.latitude, ELEVENFIFTY.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String knownName = addresses.get(0).getFeatureName();

            retVal = knownName + "\n" + address + "\n" + city + "," + state;
        }
        //EM4
        else
        {
            retVal = (currentPlace.getAddress().toString()).replace(",","\n");

        }

        return retVal;
    }
}
