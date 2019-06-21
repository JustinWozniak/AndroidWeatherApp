package com.example.myrealweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class MainActivity extends AppCompatActivity {

    TextView temperatureText;
    TextView temperature;
    ImageView mainIcon;
    TextView cityNameText;
    Button queryButton;
    TextView currentCity;
    TextView weatherTypeName;
    static final String API_KEY = "37c0fb022a528bcc6afc1295558f1efb&units=metric";
    static final String API_URL = "https://api.openweathermap.org/data/2.5/forecast?q=kitchener,Canada&appid=";
    String query = "";
    String finalUrl = "";


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
//        weatherTypeName.findViewById(R.id.condition);
        queryButton = findViewById(R.id.queryButton);
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

//                Log.i("WORKED!!!----->", Double.toString(temp));
                Log.e("WORKED!!!--Name>", weatherDescription);
//                Log.i("WeatherWORKED!!!----->", weatherArray.toString());
//                Log.i("WeatherObjectWORKED!!>", weatherObject.toString());
//                Log.i("WeatherstringWORKED!!>", iconString);
                temperature.setText(Double.toString(temp));
                temperature.setText(Double.toString(temp));
                cityNameText.setText(cityName);
                currentCity.setVisibility(View.VISIBLE);
                mainIcon.setVisibility(View.VISIBLE);
//                weatherTypeName.setVisibility(View.VISIBLE);

//

                String weatherType = iconString;
//                weatherType = "Smoke"; THIS LINE IS USED TO TEST SWITCH CASE
//                weatherTypeName.setText(weatherDescription);
                Log.e("WORKED?--Name>", weatherType);
                switch (weatherType) {
                    case "01d":
                        mainIcon.setImageResource(R.drawable.cloudy);
                        break;
                    case "13d":
                        mainIcon.setImageResource(R.drawable.snow);
                        break;
                    case "02d":
                        mainIcon.setImageResource(R.drawable.sunny);
                        break;
                    case "09n":
                        mainIcon.setImageResource(R.drawable.rainy);
                        break;
                    case "11n":
                        mainIcon.setImageResource(R.drawable.alert);
                        break;
                    case "50d":
                        mainIcon.setImageResource(R.drawable.alert);
                        break;
                    default:
                        mainIcon.setImageResource(R.drawable.clouded);
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
