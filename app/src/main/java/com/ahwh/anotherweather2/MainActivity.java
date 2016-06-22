package com.ahwh.anotherweather2;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ahwh.anotherweather2.weatherModel.DailyData_model;
import com.ahwh.anotherweather2.weatherModel.HourlyData_model;
import com.ahwh.anotherweather2.weatherModel.MainWeather_model;
import com.ahwh.anotherweather2.database.DailyForecast_RecyclerAdapter;
import com.ahwh.anotherweather2.database.DatabaseAccess;
import com.ahwh.anotherweather2.database.HourlyForecast_RecyclerAdapter;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;


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

    //Top fragment variable
    private WeatherFragment fragmentTop$1;
    private AdditionalWeatherInfo_Fragment fragmentTop$2;

    //Worker fragment to persist weather data across runtime changes (e.g. orientation change)
    public Memory_fragment memoryFragment;

    private MainWeather_model mainWeatherModel;

    private Realm realm;

    private TextView condition_main;
    private TextView temp_main;
    private TextView temp_unit;
    private TextView tempFeel_main;

    private RecyclerView hourlyForecast_rv;
    private RecyclerView dailyForecast_rv;

    private boolean restoreState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setting global Realm's configuration
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //Since ActionBar is disabled, ActionBar takes over. For now, it is essentially an ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_mainactivity);
        setSupportActionBar(toolbar);

        //locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //Creating Google Play Services' instance for Fused Location service
        //googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
        //                                                                    .addOnConnectionFailedListener(this)
        //                                                                    .addApi(LocationServices.API).build();
        //locationRequestSetup();

        //initiate gesture detector and hooking swipeListener to listen for swiping movement
        gestureDetectorCompat = new GestureDetectorCompat(this, new swipeListener());

        //Detect if this is cold start or restart due to runtime change
        if(savedInstanceState == null) {
            fragmentTop$1 = new WeatherFragment();
            fragmentTop$2 = new AdditionalWeatherInfo_Fragment();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
            fragmentTransaction.add(R.id.weather_fragment, fragmentTop$1, "topFragment$1");

            memoryFragment = new Memory_fragment();
            fragmentTransaction.add(memoryFragment, "MemoryFragment");

            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();

            restoreState = false;
        }
        //Prevent calling multiple instances of the same fragment when runtime changes by finding back the original instance through tag
        else {
            fragmentTop$1 = (WeatherFragment) getFragmentManager().findFragmentByTag("topFragment$1");
            memoryFragment = (Memory_fragment) getFragmentManager().findFragmentByTag("MemoryFragment");
            mainWeatherModel = memoryFragment.getMainWeather_model();
            restoreState = true;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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

        //Associate the view here to ensure the relevant Views are always valid
        condition_main = (TextView) findViewById(R.id.Condition_tv);
        temp_main = (TextView) findViewById(R.id.Temp_tv);
        temp_unit = (TextView) findViewById(R.id.TempTv_companion2);
        tempFeel_main = (TextView) findViewById(R.id.FeelTemp_tv);

        hourlyForecast_rv = (RecyclerView) findViewById(R.id.HourlyForecast_rv);
        dailyForecast_rv = (RecyclerView) findViewById(R.id.DailyForecast_rv);

        //Depending on start state, do stuff
        if(restoreState == false) {
            DatabaseAccess databaseAccess = new DatabaseAccess(this, null);
            databaseAccess.getFromDB();
        }
        if(restoreState == true) {
            dataSplitter(mainWeatherModel);
        }
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
                //Force first fragment to be present to prevent app crash
                if(!fragmentTop$1.isAdded()) {
                     FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                     fragmentTransaction.replace(R.id.weather_fragment, fragmentTop$1);
                     fragmentTransaction.commit();
                }

                //Configuring arguments to Retrofit Instance
                String serviceBaseURL = "https://api.forecast.io/forecast/";
                String apiKey = "";
                String coordinates = "1.3149013,103.7769791";

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

                //Get unit preference to get the correct weather data (default is "Celsius", hence the else)
                SharedPreferences preferencesFile = PreferenceManager.getDefaultSharedPreferences(this);
                String unitPref = preferencesFile.getString("temp_unit", "");
                String weatherUnit;
                if (unitPref.equals("Fahrenheit")) {
                    weatherUnit = "us";
                } else {
                    weatherUnit = "si";
                }

                String exclusion = "flags,alerts";

                //Create and use Retrofit Instance to get weather data
                ForecastService_Retrofit retrofitInstance = new ForecastService_Retrofit(this, serviceBaseURL, apiKey, coordinates, weatherUnit, exclusion);
                retrofitInstance.connectToAPI();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        if(realm != null) {
            realm.close();
        }
        super.onDestroy();
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
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                fragmentTransaction.replace(R.id.weather_fragment, fragmentTop$2);
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
                fragmentTop$2.updateUI(mainWeatherModel);
                result = true;
            }
            if (xMovement > 0 && !fragmentTop$1.isAdded()) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                fragmentTransaction.replace(R.id.weather_fragment, fragmentTop$1);
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
                fragmentTop$1.updateUI(mainWeatherModel);
                result = true;
            }

            return result;
        }
    }

    //Parse model data to update the views
    public void updateView() {
            MainWeather_model mainWeather_model = mainWeatherModel;
            RealmList<HourlyData_model> hourlyForecastArray = mainWeather_model.getHourly().getHourlyData_RL();
            hourlyForecastArray.subList(0,25);
            RealmList<DailyData_model> dailyForecastArray = mainWeather_model.getDaily().getDailyData_RL();

            HourlyForecast_RecyclerAdapter hourlyAdapter = new HourlyForecast_RecyclerAdapter(this, hourlyForecastArray);
            DailyForecast_RecyclerAdapter arrayAdapter = new DailyForecast_RecyclerAdapter(this, dailyForecastArray);

            condition_main.setText(mainWeather_model.getCurrentWeather().getSummary());

            String mainTemp = Helper.temperatureConverter(mainWeather_model.getCurrentWeather().getTemperature());
            temp_main.setText(mainTemp);
            SharedPreferences preferencesFile = PreferenceManager.getDefaultSharedPreferences(this);
            String unitPref = preferencesFile.getString("temp_unit", "");
            if (unitPref.equals("Celsius")) {
                temp_unit.setText("C");
            } else {
                temp_unit.setText("F");
            }

            String feelTemp = Helper.temperatureConverter(mainWeather_model.getCurrentWeather().getApparentTemperature());
            String feelTempFull = "Feels like: " + feelTemp + "°";
            tempFeel_main.setText(feelTempFull);

            hourlyForecast_rv.setAdapter(hourlyAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            hourlyForecast_rv.setLayoutManager(linearLayoutManager);

            dailyForecast_rv.setAdapter(arrayAdapter);
            dailyForecast_rv.setLayoutManager(new LinearLayoutManager(this));
            dailyForecast_rv.setHasFixedSize(true);
    }

    //Split the data to relevant variables and call methods to update the views
    public void dataSplitter(MainWeather_model mainWeather) {
        if(mainWeather != null) {
            memoryFragment.setMainWeather_model(mainWeather);
            mainWeatherModel = mainWeather;
            updateView();
        }
    }
}
