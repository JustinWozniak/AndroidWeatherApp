package com.example.myrealweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
    ProgressBar progressBar;
    TextView temperature;

    static final String API_KEY = "37c0fb022a528bcc6afc1295558f1efb&units=metric";
    static final String API_URL = "https://api.openweathermap.org/data/2.5/forecast?q=kitchener,Canada&appid=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureText = (TextView) findViewById(R.id.locationText);
        temperature = (TextView) findViewById(R.id.temperatureTextView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button queryButton = (Button) findViewById(R.id.queryButton);
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
            progressBar.setVisibility(View.VISIBLE);
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
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("Response-->", response);
            temperatureText.setText(response);
            try {

                JSONTokener jsonTokener = new JSONTokener(response);
                JSONObject topObject = (JSONObject) jsonTokener.nextValue();

                JSONObject mainObject = topObject.getJSONObject("main");
                double temp = mainObject.getDouble("temp");

                JSONArray weatherArray =  topObject.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String iconString = weatherObject.getString("icon");

                Log.i("WORKED!!!----->", Double.toString(temp));
                Log.i("WeatherWORKED!!!----->", weatherArray.toString());
                Log.i("WeatherObjectWORKED!!>", weatherObject.toString());
                Log.i("WeatherstringWORKED!!>", iconString);
                temperature.setText(Double.toString(temp));



            } catch (JSONException e) {
                String errorMessage = e.toString();
                Log.e("ERROR----->", errorMessage);
            }
        }
    }
}
