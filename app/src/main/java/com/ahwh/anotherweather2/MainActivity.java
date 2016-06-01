package com.ahwh.anotherweather2;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    //GoogleApiClient googleApiClient;
    //LocationRequest locationRequest;
    //Location lastKnownLocation = null;
    //String latitude = null;
    //String longtitude = null;
    //int locationPermission;
    //final private int Permission_GetKnownLocation = 0;
    //final private int Permission_GetLocation = 1;

    //Creating gesture detector instance
    GestureDetectorCompat gestureDetectorCompat;

    private WeatherFragment fragmentTop$1;
    private WeatherInfo_Fragment fragmentTop$2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Since ActionBar is disabled, ActionBar takes over. For now, it is essentially an ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_mainactivity);
        setSupportActionBar(toolbar);
        fragmentTop$1 = new WeatherFragment();
        fragmentTop$2 = new WeatherInfo_Fragment();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        fragmentTransaction.replace(R.id.weather_fragment, fragmentTop$1);
        fragmentTransaction.commit();

        //locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //Initiate Flow Manager and retrieve data from database
        FlowManager.init(new FlowConfig.Builder(this).build());
        Helper helper = new Helper(MainActivity.this);
        helper.retrieveData();

        //Creating Google Play Services' instance for Fused Location service
        //googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
        //                                                                    .addOnConnectionFailedListener(this)
        //                                                                    .addApi(LocationServices.API).build();
        //locationRequestSetup();

        //initiate gesture detector and hooking swipeListener to listen for swiping movement
        gestureDetectorCompat = new GestureDetectorCompat(this, new swipeListener());

    }

    //Return touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Connect to Google Play Service's API
       // Toast.makeText(this, "GAP is connected", Toast.LENGTH_SHORT).show();
       // googleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       int itemSelected = item.getItemId();

       switch (itemSelected) {
           //When the Refresh button is pressed
           case R.id.Refresh_button: {
               //Get coordinates for use to get weatherdata - not in use for now
               //String coordinates;
               //getLocationWrapper();
               //if(latitude == null) {
               //    Toast.makeText(this, "preventing a crash", Toast.LENGTH_SHORT).show();
               //    return true;
               //}
               //else {
               //    coordinates = latitude + "," + longtitude;
               //    //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
               //    Log.i("", coordinates);
               //}

               //Force first fragment to be present to prevent app crash
               String apikey = "";

               if(!fragmentTop$1.isAdded()) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.weather_fragment, fragmentTop$1);
                    fragmentTransaction.commit();
               }

               //Get unit preference to get the correct weather data (default is "Celsius", hence the else)
               SharedPreferences preferencesFile = PreferenceManager.getDefaultSharedPreferences(this);
               String unitPref = preferencesFile.getString("temp_unit", "");
               String weatherUnit;
               if (unitPref.equals("Fahrenheit")) {
                   weatherUnit = "us";
               } else {
                   weatherUnit = "si";
               }

               //Building retrofit instance
               Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.forecast.io/forecast/")
                                                         .addConverterFactory(LoganSquareConverterFactory.create())
                                                         .build();
               //Intercepting said created retrofit's instance
               WeatherForecast_connector connector = retrofit.create(WeatherForecast_connector.class);
               //Registering call with created instance
               Call<WeatherForecast_Model> call = connector.getWeather(apikey, "1.3149014,103.7769792", weatherUnit, "flags,alerts");
               //Make call to Forecast.io's API asynchronously to get JSON data
               //Automatically parsed by attached LoganSquare's parser through WeatherForecast_Model
               call.enqueue(new Callback<WeatherForecast_Model>() {
                   @Override
                   public void onResponse(Call<WeatherForecast_Model> call, Response<WeatherForecast_Model> response) {
                       WeatherForecast_Model forecastModelObj = response.body();
                       Helper helper = new Helper(MainActivity.this);
                       helper.updateView(forecastModelObj);
                       helper.saveData(forecastModelObj);
                   }
//
                   @Override
                   public void onFailure(Call<WeatherForecast_Model> call, Throwable t) {
                       Toast.makeText(MainActivity.this, "Failure!", Toast.LENGTH_LONG).show();
                   }
               });
               return true;

           }
            //When the Settings button is pressed, switch to Preferences activity
           case R.id.Settings_button: {
               Intent intent = new Intent(this, Preferences.class);
               startActivity(intent);
               return true;
           }

           default: {
               return super.onOptionsItemSelected(item);
           }

       }
   }

    @Override
    protected void onResume() {
        super.onResume();
        //googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        //if(googleApiClient != null) {
        //    googleApiClient.disconnect();
        //}
        super.onStop();
    }

    //=============================================================
    //Codes below are for Location Services' use (disabled for now)
    //=============================================================

    //When Google API client (mGoogleApiClient) is successfully connected
    //@Override
    //public void onConnected(@Nullable Bundle bundle) {
    //    //Retrieving last known location and pairing it to latitude and longitude string for use later
    //    lastLocationWrapper();
    //    if(lastKnownLocation != null) {
    //        latitude = Double.toString(lastKnownLocation.getLatitude());
    //        longtitude = Double.toString(lastKnownLocation.getLongitude());
    //        Toast.makeText(this, "Gotten location from last known location", Toast.LENGTH_SHORT).show();
    //    }
//
    //    //Check for Location Settings before requesting for location
    //    else {
    //        getLocationWrapper();
    //    }
    //}
//
    ////Do something once location is retrieved
    //@Override
    //public void onLocationChanged(Location location) {
//
    //    latitude = Double.toString(location.getLatitude());
    //    longtitude = Double.toString(location.getLongitude());
    //    Toast.makeText(this, "Location changed!", Toast.LENGTH_SHORT).show();
    //}
//
    ////When Google API client connection is suspended
    //@Override
    //public void onConnectionSuspended(int i) {
    //    if(i == CAUSE_SERVICE_DISCONNECTED) {
    //        Toast.makeText(this, "Network disconnected! Please re-connect.", Toast.LENGTH_SHORT).show();
    //    }
    //    if(i == CAUSE_NETWORK_LOST) {
    //        Toast.makeText(this, "No Network! Please connect.", Toast.LENGTH_SHORT).show();
    //    }
    //}
//
    ////When Google API client fails to connect
    //@Override
    //public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    //    Toast.makeText(this, "Failed to connect to Google Play Services.", Toast.LENGTH_SHORT).show();
    //}
//
    //public void locationRequestSetup() {
    //    locationRequest = new LocationRequest();
    //    locationRequest.setInterval(1800000);
    //    locationRequest.setFastestInterval(900000);
    //    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    //}

    //public void locationSettingsCheck() {
    //    //Checking user's location settings first
    //    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
    //    builder.addLocationRequest(locationRequest);
    //    PendingResult<LocationSettingsResult> locSettingsResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
    //    //Upon getting results
    //    locSettingsResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
    //        @Override
    //        public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
    //            final Status status = locationSettingsResult.getStatus();
    //            final LocationSettingsStates locationState = locationSettingsResult.getLocationSettingsStates();
    //            switch (status.getStatusCode()) {
    //                case LocationSettingsStatusCodes.SUCCESS: {
    //                    MainActivityPermissionsDispatcher.getLocationWithCheck(MainActivity.this);
    //                }
    //                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: {
    //                    Toast.makeText(MainActivity.this, "Please change your location settings to \"High Accuracy\" in Settings", Toast.LENGTH_SHORT).show();
    //                    break;
    //                }
    //                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: {
    //                    Toast.makeText(MainActivity.this, "Location Settings not present", Toast.LENGTH_SHORT).show();
    //                    break;
    //                }
    //            }
    //        }
    //    });
    //}

    //public void lastLocationWrapper() {
    //    if(locationPermission != PackageManager.PERMISSION_GRANTED) {
    //        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Permission_GetKnownLocation);
    //    }
    //    else {
    //        lastLocation();
    //    }
    //}
//
    //public void lastLocation() {
    //    lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    //}
//
    //public void getLocationWrapper() {
    //    if(locationPermission != PackageManager.PERMISSION_GRANTED) {
    //        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Permission_GetLocation);
    //    } else {
    //        getLocation();
    //    }
    //}
//
    //public void getLocation() {
    //    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    //}
//
    //@Override
    //public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //    switch(requestCode) {
    //        case Permission_GetLocation: {
    //            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    //                getLocation();
    //                break;
    //            }
    //        }
    //        case Permission_GetKnownLocation: {
    //            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    //                lastLocation();
    //                break;
    //            }
    //        }
    //        default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //    }
    //}

    //Sub-class to handle swiping movement
    public class swipeListener extends GestureDetector.SimpleOnGestureListener {

        //When swipe (on-fling)
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            float yMaxValue = 100;

            //Calculate the difference in movement values from the initial touch (e1) to when the finger is let go (e2)
            final float xMovement = e2.getX() - e1.getX();

            //if difference is negative, user swipe left & check if fragment already exists
            if (xMovement < 0 && !fragmentTop$2.isAdded()) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                fragmentTransaction.replace(R.id.weather_fragment, fragmentTop$2);
                fragmentTransaction.commit();
                result = true;
            }
            if (xMovement > 0 && !fragmentTop$1.isAdded()) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                fragmentTransaction.replace(R.id.weather_fragment, fragmentTop$1);
                fragmentTransaction.commit();
                result = true;
            }

            return result;
        }
    }
}
