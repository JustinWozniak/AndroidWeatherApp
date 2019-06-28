package com.example.myrealweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class fiveday extends AppCompatActivity {

    TextView dayOneHighTemp;
    TextView dayOneLowTemp;
    TextView dayOneText;
    ImageView dayOneIcon;
    TextView descriptionone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiveday);
        dayOneHighTemp = findViewById(R.id.highTemp1);
        dayOneLowTemp = findViewById(R.id.lowTemp1);
        dayOneText = findViewById(R.id.day1Text);
        dayOneIcon = findViewById(R.id.day1Icon);
        descriptionone = findViewById(R.id.description1);

        //Does the api call
        new RetrieveFeedTask().execute();
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
            Log.e("responce-->", response);
            JSONTokener jsonTokener = new JSONTokener(response);
            try {

                //day 1 high temp starts here...
                JSONObject TopObject = (JSONObject) jsonTokener.nextValue();
                //                Log.e("WORKED?--topObject>",topObject.toString());
                JSONArray day1DailyForecasts = TopObject.getJSONArray("DailyForecasts");
                Log.e("Dailyforecast:>", day1DailyForecasts.toString());
                JSONObject day1FullDay = (JSONObject) day1DailyForecasts.getJSONObject(0);
                Log.e("fullDay:>", day1FullDay.toString());
                //icon selection starts here....
                JSONObject day1daytimeIcon = (JSONObject) day1FullDay.getJSONObject("Day");
                Log.e("day1Icon: ", day1daytimeIcon.toString());
                Integer day1iconInt = day1daytimeIcon.getInt("Icon");
                String dayOneconvertedIconName = day1iconInt.toString();
                Log.e("day1iconInt: ", day1iconInt.toString());
                Log.e("day1iconString: ", dayOneconvertedIconName);
                //icon ends here


                //day headline starts here..
                JSONObject day1headlineObj = TopObject.getJSONObject("Headline");
                Log.e("day1headlineObj:>", day1headlineObj.toString());
                String day1headline = day1daytimeIcon.getString("IconPhrase");
                Log.e("day1headline",day1headline);
                descriptionone.setText(day1headline);

                JSONObject day1HighString = (JSONObject) day1FullDay.getJSONObject("Temperature");
                Log.e("HighString: ", day1HighString.toString());
                JSONObject day1highTemp = (JSONObject) day1HighString.getJSONObject("Maximum");
                Log.e("HighTemp: ", day1highTemp.toString());
                Double day1HighTempIncelcius = (Double) day1highTemp.getDouble("Value");
                Log.e("highTempIncelcius: ", day1HighTempIncelcius.toString());
                dayOneHighTemp.setText((day1HighTempIncelcius) + "°");
                //high temp ends here.......

                //day 1 low temp starts here...
                JSONObject day1LowString = (JSONObject) day1FullDay.getJSONObject("Temperature");
                Log.e("LowString: ", day1LowString.toString());
                JSONObject day1LowTemp = (JSONObject) day1LowString.getJSONObject("Minimum");
                Log.e("LowTemp: ", day1LowTemp.toString());
                Double day1LowTempIncelcius = (Double) day1LowTemp.getDouble("Value");
                Log.e("day1LowTempIncelcius: ", day1LowTempIncelcius.toString());
                dayOneLowTemp.setText((day1LowTempIncelcius) + "°");
                //low temp ends here...

                //date day1 starts here....
                String day1Date = day1FullDay.getString("Date");
                Log.e("day1date: ", day1Date);
                dayOneText.setText(day1Date);


                String weatherType = dayOneconvertedIconName;
//                weatherType = "Smoke"; THIS LINE IS USED TO TEST SWITCH CASE
//                Log.e("WORKED?--Name>", weatherType);
//                weatherType = "24";
                switch (weatherType) {
                    case "1":
                        dayOneIcon.setImageResource(R.drawable.accu01s);
                        break;
                    case "2":
                        dayOneIcon.setImageResource(R.drawable.accu02s);
                        break;
                    case "3":
                        dayOneIcon.setImageResource(R.drawable.accu03s);
                        break;
                    case "4":
                        dayOneIcon.setImageResource(R.drawable.accu04s);
                        break;
                    case "5":
                        dayOneIcon.setImageResource(R.drawable.accu05s);
                        break;
                    case "6":
                        dayOneIcon.setImageResource(R.drawable.accu06s);
                        break;
                    case "7":
                        dayOneIcon.setImageResource(R.drawable.accu07s);
                        break;
                    case "8":
                        dayOneIcon.setImageResource(R.drawable.accu08s);
                        break;
                    case "11":
                        dayOneIcon.setImageResource(R.drawable.accu11s);
                        break;
                    case "12":
                        dayOneIcon.setImageResource(R.drawable.accu12s);
                        break;
                    case "13":
                        dayOneIcon.setImageResource(R.drawable.accu13s);
                        break;
                    case "14":
                        dayOneIcon.setImageResource(R.drawable.accu14s);
                        break;
                    case "15":
                        dayOneIcon.setImageResource(R.drawable.accu15s);
                        break;
                    case "16":
                        dayOneIcon.setImageResource(R.drawable.accu16s);
                        break;
                    case "17":
                        dayOneIcon.setImageResource(R.drawable.accu17s);
                        break;
                    case "18":
                        dayOneIcon.setImageResource(R.drawable.accu18s);
                        break;
                    case "19":
                        dayOneIcon.setImageResource(R.drawable.accu19s);
                        break;
                    case "20":
                        dayOneIcon.setImageResource(R.drawable.accu20s);
                        break;
                    case "21":
                        dayOneIcon.setImageResource(R.drawable.accu21s);
                        break;
                    case "22":
                        dayOneIcon.setImageResource(R.drawable.accu22s);
                        break;
                    case "24":
                        dayOneIcon.setImageResource(R.drawable.accu24s);
                        break;
                    case "25":
                        dayOneIcon.setImageResource(R.drawable.accu25s);
                        break;
                    case "26":
                        dayOneIcon.setImageResource(R.drawable.accu26s);
                        break;
                    case "29":
                        dayOneIcon.setImageResource(R.drawable.accu29s);
                        break;
                    case "30":
                        dayOneIcon.setImageResource(R.drawable.accu30s);
                        break;
                    case "31":
                        dayOneIcon.setImageResource(R.drawable.accu31s);
                        break;
                    case "32":
                        dayOneIcon.setImageResource(R.drawable.accu32s);
                        break;
                    case "33":
                        dayOneIcon.setImageResource(R.drawable.accu33s);
                        break;
                    case "34":
                        dayOneIcon.setImageResource(R.drawable.accu35s);
                        break;
                    case "36":
                        dayOneIcon.setImageResource(R.drawable.accu36s);
                        break;
                    case "37":
                        dayOneIcon.setImageResource(R.drawable.accu37s);
                        break;
                    case "38":
                        dayOneIcon.setImageResource(R.drawable.accu38s);
                        break;
                    case "39":
                        dayOneIcon.setImageResource(R.drawable.accu39s);
                        break;
                    case "40":
                        dayOneIcon.setImageResource(R.drawable.accu40s);
                        break;
                    case "41":
                        dayOneIcon.setImageResource(R.drawable.accu41s);
                        break;
                    case "42":
                        dayOneIcon.setImageResource(R.drawable.accu42s);
                        break;
                    case "43":
                        dayOneIcon.setImageResource(R.drawable.accu43s);
                        break;
                    case "44":
                        dayOneIcon.setImageResource(R.drawable.accu44s);
                        break;
                    default:
                        dayOneIcon.setImageResource(R.drawable.alert);
                        break;
                }


            } catch (JSONException e) {
                Log.e("ErrorMessage=", e.toString());

            }












        }
    }
}






