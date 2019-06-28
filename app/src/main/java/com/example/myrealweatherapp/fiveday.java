package com.example.myrealweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class fiveday extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiveday);
        new RetrieveFeedTask().execute();
    }


}

class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

    private Exception exception;


    @Override
    protected String doInBackground(Void... voids) {
        String accuWeatherApi = "https://dataservice.accuweather.com/forecasts/v1/daily/5day/49564?apikey=%09f1Yir8GP8jZo5oY50ubWDUaslWrjJAvF&metric=true";
        try {
            URL url = new URL(accuWeatherApi);
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
            response = "THERE WAS AN ERRORONPOSTEXCECUTE";
        }

        Log.e("ResponseACCU->", response);
//            temperatureText.setText(response);
        Log.e("WORKED?--Name>",response);
        JSONTokener jsonTokener = new JSONTokener(response);
            try {
                JSONObject topObject = (JSONObject) jsonTokener.nextValue();
//                Log.e("WORKED?--topObject>",topObject.toString());
                JSONObject mainObject = topObject.getJSONObject("DailyForecasts");
                Log.e("WORKED?--mainObject>",mainObject.toString());



            } catch (JSONException e) {
                e.printStackTrace();
            }


    }
}






