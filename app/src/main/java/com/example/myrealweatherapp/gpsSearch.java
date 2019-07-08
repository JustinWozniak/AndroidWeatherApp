package com.example.myrealweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class gpsSearch extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView searchView1;
    TextView resultTextView;
    TextView locationTextTexView;
    TextView latitudeResultView;
    TextView longitudeResultTextView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationTextTexView = findViewById(R.id.locationTextTexView);
        longitudeResultTextView = findViewById(R.id.longitudeResultTextView);
        setContentView(R.layout.activity_gps_search);
        latitudeResultView = findViewById(R.id.latitudeResultView);
        resultTextView = findViewById(R.id.resultTextView);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location",location.toString());
                Double laditutde = location.getLatitude();
                Double longitude = location.getLongitude();
                Log.e("Laditude=",laditutde.toString());
                Log.e("longitude=",longitude.toString());
                String finalLongitude = longitude.toString();

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
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }



    }            public void getGpsWeather(View view) {
        try {
            gpsSearch.DownloadTask task = new gpsSearch.DownloadTask();
            String encodedCityName1 = URLEncoder.encode(searchView1.getText().toString(), "UTF-8");

            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName1 + "&appid=37c0fb022a528bcc6afc1295558f1efb&units=metric");


            //hides they keyboard when button is pushed
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(searchView1.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Could not find weather:", e.toString());
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();

                Log.e("Could not find weather", e.toString());

                return null;
            }
        }


        private int convert_AccuIconCode_to_Id(int iconCode) {
            // The following avoids a giant Switch/Case or If/Else-If statement!!!
            String codeString = String.format("%02d", iconCode);
            String iconResource = "accu" + codeString + "s";
            Resources r = getResources();
            int drawableId = r.getIdentifier(iconResource, "drawable", "com.example.myrealweatherapp");
            return drawableId;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");
                String mainWeatherInfo = jsonObject.getString("main");
                Log.i("Weather content", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                String message = "";

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");


                    String iconString = jsonPart.getString("icon");
                    Log.e("IconCode is:", iconString);


                    if (!main.equals("") && !description.equals("")) {
                        message += main + ": " + description + "\r\n";
                    }
                }

                if (!message.equals("")) {
                    resultTextView.setText(message);
                } else {
                    message = "Could not find weather";
                }

            } catch (Exception e) {

                Log.e("Could not find weather:", e.toString());

                e.printStackTrace();
            }


        }
    }

}
