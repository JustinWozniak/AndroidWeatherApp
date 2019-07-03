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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class fiveday extends AppCompatActivity {

    TextView dayOneHighTemp;
    TextView dayOneLowTemp;
    TextView dayOneText;
    ImageView dayOneIcon;
    TextView descriptionone;

    TextView dayTwoHighTemp;
    TextView dayTwoLowTemp;
    TextView dayTwoText;
    ImageView dayTwoIcon;
    TextView descriptiontwo;

    TextView dayThreeHighTemp;
    TextView dayThreeLowTemp;
    TextView dayThreeText;
    ImageView dayThreeIcon;
    TextView descriptionthree;

    TextView dayFourHighTemp;
    TextView dayFourLowTemp;
    TextView dayFourText;
    ImageView dayFourIcon;
    TextView descriptionfour;

    TextView dayFiveHighTemp;
    TextView dayFiveLowTemp;
    TextView dayFiveText;
    ImageView dayFiveIcon;
    TextView descriptionfive;

    //The following is used to convert days to text formatted day names
    String convertDay(String oldDate) {

        String[] day1parts = oldDate.split("T");
        String day1part1 = day1parts[0];
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null;
        try {
            date2 = formatter2.parse(day1part1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayOneStringDate = date2.toString();
        String[] parts2 = dayOneStringDate.split(" ");
        String part4 = parts2[0];
        return part4;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiveday);
        dayOneHighTemp = findViewById(R.id.highTemp1);
        dayOneLowTemp = findViewById(R.id.lowTemp1);
        dayOneText = findViewById(R.id.day2Text);
        dayOneIcon = findViewById(R.id.day2Icon);
        descriptionone = findViewById(R.id.description1);

        dayTwoHighTemp = findViewById(R.id.highTemp2);
        dayTwoLowTemp = findViewById(R.id.lowTemp2);
        //the next two variables are offset by a day
        dayTwoText = findViewById(R.id.day3Text);
        dayTwoIcon = findViewById(R.id.day3Icon);
        descriptiontwo = findViewById(R.id.description2);

        dayThreeHighTemp = findViewById(R.id.highTemp3);
        dayThreeLowTemp = findViewById(R.id.lowTemp3);
        //the next two variables are offset by a day
        dayThreeText = findViewById(R.id.day4text);
        dayThreeIcon = findViewById(R.id.day4Icon);
        descriptionthree = findViewById(R.id.description3);

        dayFourHighTemp = findViewById(R.id.hightemp4);
        dayFourLowTemp = findViewById(R.id.lowTemp4);
        //the next two variables are offset by a day
        dayFourText = findViewById(R.id.day5Text);
        dayFourIcon = findViewById(R.id.day5icon);
        descriptionfour = findViewById(R.id.description4);

        dayFiveHighTemp = findViewById(R.id.highTemp5);
        dayFiveLowTemp = findViewById(R.id.lowTemp5);
        //the next two variables are offset by a day
        dayFiveText = findViewById(R.id.day6Text);
        dayFiveIcon = findViewById(R.id.day6Icon);
        descriptionfive = findViewById(R.id.description5);

        //Does the api call
        new RetrieveFeedTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(Void... voids) {
            String accuWeatherApi = "\n" +
                    "http://dataservice.accuweather.com/forecasts/v1/daily/5day/49564?apikey=p5eumBP6yILniKSREfzT6jWTKG9IBopF&metric=true";
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

                //high temps starts here...
                JSONObject TopObject = (JSONObject) jsonTokener.nextValue();
                JSONArray day1DailyForecasts = TopObject.getJSONArray("DailyForecasts");
                JSONObject day1FullDay = day1DailyForecasts.getJSONObject(0);
                JSONObject day2FullDay = day1DailyForecasts.getJSONObject(1);
                JSONObject day3FullDay =  day1DailyForecasts.getJSONObject(2);
                JSONObject day4FullDay =  day1DailyForecasts.getJSONObject(3);
                JSONObject day5FullDay =  day1DailyForecasts.getJSONObject(4);

                //icon selection starts here....
                JSONObject day1daytimeIcon =  day1FullDay.getJSONObject("Day");
                JSONObject day2daytimeIcon =  day2FullDay.getJSONObject("Day");
                JSONObject day3daytimeIcon = day3FullDay.getJSONObject("Day");
                JSONObject day4daytimeIcon =  day4FullDay.getJSONObject("Day");
                JSONObject day5daytimeIcon =  day5FullDay.getJSONObject("Day");

                Integer day1iconInt = day1daytimeIcon.getInt("Icon");
                Integer day2iconInt = day2daytimeIcon.getInt("Icon");
                Integer day3iconInt = day3daytimeIcon.getInt("Icon");
                Integer day4iconInt = day4daytimeIcon.getInt("Icon");
                Integer day5iconInt = day5daytimeIcon.getInt("Icon");
                String dayOneconvertedIconName = day2iconInt.toString();

//                weatherType = "Smoke"; THIS LINE IS USED TO TEST SWITCH CASE
//                Log.e("WORKED?--Name>", weatherType);
//                weatherType = "24";
                switch (dayOneconvertedIconName) {
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
                //icon ends here

                //day headline starts here..
                String day1headline = day1daytimeIcon.getString("IconPhrase");
                String day2headline = day2daytimeIcon.getString("IconPhrase");
                String day3headline = day3daytimeIcon.getString("IconPhrase");
                String day4headline = day4daytimeIcon.getString("IconPhrase");
                String day5headline = day5daytimeIcon.getString("IconPhrase");
                descriptionone.setText(day1headline);
                descriptiontwo.setText(day2headline);
                descriptionthree.setText(day3headline);
                descriptionfour.setText(day4headline);
                descriptionfive.setText(day5headline);

                JSONObject day1HighString =  day1FullDay.getJSONObject("Temperature");
                JSONObject day2HighString =  day2FullDay.getJSONObject("Temperature");
                JSONObject day3HighString =  day3FullDay.getJSONObject("Temperature");
                JSONObject day4HighString =  day4FullDay.getJSONObject("Temperature");
                JSONObject day5HighString =  day5FullDay.getJSONObject("Temperature");

                JSONObject day1highTemp =  day1HighString.getJSONObject("Maximum");
                JSONObject day2highTemp =  day2HighString.getJSONObject("Maximum");
                JSONObject day3highTemp =  day3HighString.getJSONObject("Maximum");
                JSONObject day4highTemp =  day4HighString.getJSONObject("Maximum");
                JSONObject day5highTemp =  day5HighString.getJSONObject("Maximum");

                Double day1HighTempIncelcius =  day1highTemp.getDouble("Value");
                Double day2HighTempIncelcius =  day2highTemp.getDouble("Value");
                Double day3HighTempIncelcius =  day3highTemp.getDouble("Value");
                Double day4HighTempIncelcius =  day4highTemp.getDouble("Value");
                Double day5HighTempIncelcius =  day5highTemp.getDouble("Value");

                dayOneHighTemp.setText((day1HighTempIncelcius) + "°");
                dayTwoHighTemp.setText((day2HighTempIncelcius) + "°");
                dayThreeHighTemp.setText((day3HighTempIncelcius) + "°");
                dayFourHighTemp.setText((day4HighTempIncelcius) + "°");
                dayFiveHighTemp.setText((day5HighTempIncelcius) + "°");
                //high temp ends here.......

                //low temp starts here...
                JSONObject day1LowString =  day1FullDay.getJSONObject("Temperature");
                JSONObject day2LowString =  day2FullDay.getJSONObject("Temperature");
                JSONObject day3LowString =  day3FullDay.getJSONObject("Temperature");
                JSONObject day4LowString =  day4FullDay.getJSONObject("Temperature");
                JSONObject day5LowString =  day5FullDay.getJSONObject("Temperature");

                JSONObject day1LowTemp =  day1LowString.getJSONObject("Minimum");
                JSONObject day2LowTemp =  day2LowString.getJSONObject("Minimum");
                JSONObject day3LowTemp =  day3LowString.getJSONObject("Minimum");
                JSONObject day4LowTemp =  day4LowString.getJSONObject("Minimum");
                JSONObject day5LowTemp =  day5LowString.getJSONObject("Minimum");

                double day1LowTempIncelcius =  day1LowTemp.getDouble("Value");
                double day2LowTempIncelcius =  day2LowTemp.getDouble("Value");
                double day3LowTempIncelcius =  day3LowTemp.getDouble("Value");
                double day4LowTempIncelcius =  day4LowTemp.getDouble("Value");
                double day5LowTempIncelcius =  day5LowTemp.getDouble("Value");

                dayOneLowTemp.setText((day1LowTempIncelcius) + "°");
                dayTwoLowTemp.setText((day2LowTempIncelcius) + "°");
                dayThreeLowTemp.setText((day3LowTempIncelcius) + "°");
                dayFourLowTemp.setText((day4LowTempIncelcius) + "°");
                dayFiveLowTemp.setText((day5LowTempIncelcius) + "°");
                //low temp ends here...

                //date day1 starts here....
                String day1Date = day1FullDay.getString("Date");
                String day2Date = day2FullDay.getString("Date");
                String day3Date = day3FullDay.getString("Date");
                String day4Date = day4FullDay.getString("Date");
                String day5Date = day5FullDay.getString("Date");

                //call convertday function to change dateformat to nicer string format
                String finalDateString1 = convertDay(day1Date);
                String finalDateString2 = convertDay(day2Date);
                String finalDateString3 = convertDay(day3Date);
                String finalDateString4 = convertDay(day4Date);
                String finalDateString5 = convertDay(day5Date);
                dayOneText.setText(finalDateString1);
                dayTwoText.setText(finalDateString2);
                dayThreeText.setText(finalDateString3);
                dayFourText.setText(finalDateString4);
                dayFiveText.setText(finalDateString5);

            } catch (JSONException e) {
                Log.e("ErrorMessage=", e.toString());
            }
        }
    }
}






