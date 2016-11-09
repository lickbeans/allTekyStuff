package com.aaroncampbell.peoplemon.Views;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aaroncampbell.peoplemon.MainActivity;
import com.aaroncampbell.peoplemon.Models.User;
import com.aaroncampbell.peoplemon.Network.RestClient;
import com.aaroncampbell.peoplemon.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aaroncampbell on 11/7/16.
 */

public class MapPageView extends RelativeLayout implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener {
    private Context context;
    public static final String TAG = MapPageView.class.getSimpleName();

    @Bind(R.id.map)
    MapView mapView;

    GoogleMap map;
    Place currentPlace;
    Integer BUILDING_LEVEL = 16;
    LatLng STHPNT = new LatLng(38.451, -82.593);
    private double getLat, getLng;
    LocationRequest locationRequest;
    Location mLastLocation;
    LocationListener locationListener;
    GoogleApiClient apiClient;
    final Handler handler =new Handler();


    public MapPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        mapView.onCreate(((MainActivity) context).savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);   // Needs to go in onFinishInflate()
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        apiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        apiClient.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Knows where you are
        handler.post(locationCheck);
//        locationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
//                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng coordinate = new LatLng(getLat, getLng);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 9);
        map.animateCamera(yourLocation);

        map.setMinZoomPreference(6.0f);
        map.setMaxZoomPreference(18.0f);

        map.addMarker(new MarkerOptions().position(coordinate).title("Marker in South Point").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)).snippet("Current Place"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(STHPNT, BUILDING_LEVEL));
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);

    }

    Runnable locationCheck = new Runnable() {
        @Override
        public void run() {
            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
                handleNewLocation(mLastLocation);
            } catch (SecurityException e) {
                e.printStackTrace();
            } finally {
                handler.postDelayed(this, 2000);
            }
        }
    };

    private void handleNewLocation(Location location) {
//        double currentLatitude = location.getLatitude();
//        double currentLongitude = location.getLongitude();
//        final LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(STHPNT)
                .title("Current Location");
        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLng(STHPNT));
        User user = new User(getLng, getLat);
        RestClient restClient = new RestClient();
        restClient.getApiService().checkIn(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "You have checked in at " + STHPNT, Toast.LENGTH_LONG).show();
                    Log.d("**********", STHPNT.toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
//
//    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
//        @Override
//        public void onMyLocationChange(Location location) {
//            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//            Marker marker = map.addMarker(new MarkerOptions().position(loc));
//            if(map != null){
//                map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//            }
//        }
//    };
}
