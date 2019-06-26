package com.example.myrealweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView temperatureText;
    TextView temperature;
    ImageView mainIcon;
    TextView cityNameText;
    Button queryButton;
    Button fiveDay;
    TextView currentCity;
    TextView weatherTypeName;
    static final String API_KEY = "37c0fb022a528bcc6afc1295558f1efb&units=metric";
    static final String API_URL = "https://api.openweathermap.org/data/2.5/forecast?q=kitchener,Canada&appid=";
    String query = "";
    String finalUrl = "";

    LocationManager locationManager;
    LocationListener locationListener;


    /** Called when the user taps the Five-day forecast button */


    public void fiveDayView(View view) {
        Intent intent = new Intent(this, fiveDayView.class);
        startActivity(intent);
    }




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
        setContentView(R.layout.activity_main);
        temperatureText = findViewById(R.id.locationText);
        temperature = findViewById(R.id.temperatureTextView);
        mainIcon = findViewById(R.id.imageView);
        cityNameText = findViewById(R.id.locationString);
        currentCity = findViewById(R.id.current);
        mainIcon.setVisibility(View.GONE);
        currentCity.setVisibility(View.GONE);
        cityNameText.setVisibility(View.GONE);
        temperature.setVisibility(View.GONE);
        queryButton = findViewById(R.id.queryButton);
        fiveDay = findViewById(R.id.fiveDayButton);
        fiveDay.setVisibility(View.GONE);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.e("Location working? ", location.toString());

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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {

            temperature.setText("");
        }

        protected String doInBackground(Void... urls) {
            String email = "https://api.openweathermap.org/data/2.5/weather?q=kitchener,Canada&appid=37c0fb022a528bcc6afc1295558f1efb&units=metric"; // emailText.getText().toString();
            // Do some validation here

            try {
                URL url = new URL(email);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }

            Log.i("Response-->", response);
//            temperatureText.setText(response);    USED TO SHOW THE RETURNED JSON DATA
            try {
                JSONTokener jsonTokener = new JSONTokener(response);
                JSONObject topObject = (JSONObject) jsonTokener.nextValue();

                JSONObject mainObject = topObject.getJSONObject("main");
                double temp = mainObject.getDouble("temp");
                String cityName = topObject.getString("name");

                JSONArray weatherArray = topObject.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String iconString = weatherObject.getString("icon");
                String weatherDescription = weatherObject.getString("main");

//              Log.i("WORKED!!!----->", Double.toString(temp));
                Log.e("WORKED!!!--Name>", weatherDescription);
//              Log.i("WeatherWORKED!!!----->", weatherArray.toString());
//              Log.i("WeatherObjectWORKED!!>", weatherObject.toString());
//              Log.i("WeatherstringWORKED!!>", iconString);
                temperature.setText(Double.toString(temp));
                temperature.setText(Double.toString(temp) + "°");
                cityNameText.setVisibility(View.VISIBLE);
                cityNameText.setText(cityName);
                queryButton.setVisibility(View.GONE);
                fiveDay.setVisibility(View.VISIBLE);
                temperature.setVisibility(View.VISIBLE);
                currentCity.setVisibility(View.VISIBLE);
                mainIcon.setVisibility(View.VISIBLE);
//              weatherTypeName.setVisibility(View.VISIBLE);

//

                String weatherType = iconString;
//                weatherType = "Smoke"; THIS LINE IS USED TO TEST SWITCH CASE
//                weatherTypeName.setText(weatherDescription);
                Log.e("WORKED?--Name>", weatherType);
                switch (weatherType) {
                    case "02d":
                        mainIcon.setImageResource(R.drawable.icon_02d);
                        break;
                    case "03d":
                        mainIcon.setImageResource(R.drawable.icon_03d);
                        break;
                    case "04d":
                        mainIcon.setImageResource(R.drawable.icon_04d);
                        break;
                    case "09d":
                        mainIcon.setImageResource(R.drawable.icon_09d);
                        break;
                    case "10d":
                        mainIcon.setImageResource(R.drawable.icon_10d);
                        break;
                    case "11d":
                        mainIcon.setImageResource(R.drawable.icon_11d);
                        break;
                    case "13d":
                        mainIcon.setImageResource(R.drawable.icon_13d);
                        break;
                    case "50d":
                        mainIcon.setImageResource(R.drawable.icon_50d);
                        break;

                    case "02n":
                        mainIcon.setImageResource(R.drawable.icon_02n);
                        break;
                    case "03n":
                        mainIcon.setImageResource(R.drawable.icon_03n);
                        break;
                    case "04n":
                        mainIcon.setImageResource(R.drawable.icon_04n);
                        break;
                    case "09n":
                        mainIcon.setImageResource(R.drawable.icon_09n);
                        break;
                    case "10n":
                        mainIcon.setImageResource(R.drawable.icon_10n);
                        break;
                    case "11n":
                        mainIcon.setImageResource(R.drawable.icon_11n);
                        break;
                    case "13n":
                        mainIcon.setImageResource(R.drawable.icon_13n);
                        break;
                    case "50n":
                        mainIcon.setImageResource(R.drawable.icon_50n);
                        break;

                    default:
                        mainIcon.setImageResource(R.drawable.alert);
                        break;
                }

            } catch (JSONException e) {
                String errorMessage = e.toString();
                Log.e("ERROR----->", errorMessage);
//
            }
        }
    }
}
