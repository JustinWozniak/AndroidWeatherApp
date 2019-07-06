package com.example.myrealweatherapp;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    TextView temperature;
    ImageView mainIcon;
    ImageView logo;
    TextView cityNameText;
    TextView humidityView;
    TextView humidityValueView;
    Button queryButton;
    Button fiveDay;
    TextView currentCity;
    TextView smallDescription;
    TextView sunriseText;
    TextView sunsetView;
    TextView sunsetValue;
    TextView sunriseTextValue;


    public void locationSearch(View view) {
        Intent intent = new Intent(this, locationSearch.class);
        startActivity(intent);
    }

    public void fiveDay(View view) {
        Intent intent = new Intent(this, fiveday.class);
        startActivity(intent);
    }
    public void gpsSearch(View view) {
        Intent intent = new Intent(this, gpsSearch.class);
        startActivity(intent);
    }

    private String convert_epochTime_to_dayOfWeek(long epochDate) {
        Date date = new Date(epochDate * 1000);
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("HH:mm:ssZ"); // the day of the week spelled out completely
        simpleDateformat.setTimeZone(TimeZone.getDefault());
        String dayName = simpleDateformat.format(date);
        return dayName;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sunsetValue = findViewById(R.id.sunsetValue);
        sunsetValue.setVisibility(View.GONE);
        sunsetView = findViewById(R.id.sunsetView);
        sunsetView.setVisibility(View.GONE);
        sunriseTextValue = findViewById(R.id.sunriseTextValue);
        sunriseTextValue.setVisibility(View.GONE);
        sunriseText = findViewById(R.id.sunriseText);
        sunriseText.setVisibility(View.GONE);
        humidityValueView = findViewById(R.id.humidityValue);
        humidityValueView.setVisibility(View.GONE);
        temperature = findViewById(R.id.temperatureTextView);
        mainIcon = findViewById(R.id.imageView);
//        logo = findViewById(R.id.logoimageview);
        humidityView = findViewById(R.id.humidityText);
        humidityView.setVisibility(View.GONE);
//        logo.setVisibility(View.VISIBLE);
        cityNameText = findViewById(R.id.locationString);
        currentCity = findViewById(R.id.current);
        mainIcon.setVisibility(View.GONE);
        currentCity.setVisibility(View.GONE);
        cityNameText.setVisibility(View.GONE);
        temperature.setVisibility(View.GONE);
        queryButton = findViewById(R.id.queryButton);
        fiveDay = findViewById(R.id.fiveDayButton);
        fiveDay.setVisibility(View.VISIBLE);
        smallDescription = findViewById(R.id.smallWeatherView);
        smallDescription.setVisibility(View.GONE);


        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //does the api call
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
            String weatherapi = "https://api.openweathermap.org/data/2.5/weather?q=kitchener,Canada&appid=37c0fb022a528bcc6afc1295558f1efb&units=metric"; // emailText.getText().toString();


            try {
                URL url = new URL(weatherapi);
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
//                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }

//            Log.i("Response-->", response);
//            temperatureText.setText(response);    USED TO SHOW THE RETURNED JSON DATA
            try {
                JSONTokener jsonTokener = new JSONTokener(response);
                JSONObject topObject = (JSONObject) jsonTokener.nextValue();

                JSONObject mainObject = topObject.getJSONObject("main");
                double temp = mainObject.getDouble("temp");
                String humidityValue = mainObject.getString("humidity");
                Log.e("Humidity", humidityValue);
                String cityName = topObject.getString("name");

                JSONArray weatherArray = topObject.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String iconString = weatherObject.getString("icon");
                String smallWeatherDescription = weatherObject.getString("description");
                Log.e("Weatherdescription: ", smallWeatherDescription);
                String weatherDescription = weatherObject.getString("main");

                JSONObject systemObject = topObject.getJSONObject("sys");
                Log.e("SYSTEMOBJECT:", systemObject.toString());
                String sunriseTime = systemObject.getString("sunrise");
                String sunsetTime = systemObject.getString("sunset");
                Log.e("sunriseTime:", sunriseTime);
                Log.e("sunsetTime:", sunsetTime);
                Long sunrisenumnum = Long.parseLong(sunriseTime);
                Long sunsetnum = Long.parseLong(sunsetTime);
                String convertedSunriseTime = convert_epochTime_to_dayOfWeek(sunrisenumnum);
                String convertedSunSetTime = convert_epochTime_to_dayOfWeek(sunsetnum);
                Log.e("ConvertedTimeRise:", convertedSunriseTime);
                Log.e("ConvertedTimeSet:", convertedSunSetTime);


                temperature.setText(Double.toString(temp));
                temperature.setText((temp) + "°");
                sunsetValue.setVisibility(View.VISIBLE);
                sunsetView.setVisibility(View.VISIBLE);
                sunriseTextValue.setVisibility(View.VISIBLE);
                sunriseText.setVisibility(View.VISIBLE);
                smallDescription.setVisibility(View.VISIBLE);
                smallDescription.setText(smallWeatherDescription);
                cityNameText.setVisibility(View.VISIBLE);
                humidityView.setVisibility(View.VISIBLE);
                cityNameText.setText(cityName);
                queryButton.setVisibility(View.GONE);
                fiveDay.setVisibility(View.VISIBLE);
                temperature.setVisibility(View.VISIBLE);
                currentCity.setVisibility(View.VISIBLE);
                mainIcon.setVisibility(View.VISIBLE);
//                logo.setVisibility(View.GONE);
                humidityValueView.setVisibility(View.VISIBLE);
                sunriseTextValue.setText(convertedSunriseTime);
                sunsetValue.setText(convertedSunSetTime);
                humidityValueView.setText(humidityValue);
                String weatherType = iconString;
//                weatherType = "Smoke"; THIS LINE IS USED TO TEST SWITCH CASE
//                Log.e("WORKED?--Name>", weatherType);
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
//                Log.e("ERROR----->", errorMessage);
            }
        }
    }
}
