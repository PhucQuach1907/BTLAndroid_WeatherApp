package com.example.weatherapp.Controllers;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.Fragments.FragmentHome;
import com.example.weatherapp.Fragments.FragmentSearch;
import com.example.weatherapp.Model.Weather;
import com.example.weatherapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class WeatherAPIController {
    private FragmentHome fragmentHome;
    private FragmentSearch fragmentSearch;
    private final Context mContext;
    private final RequestQueue mRequestQueue;
    static final String API_KEY = "d9c0c317f90b32c51bb03768b11eb5a0";

    public WeatherAPIController(FragmentHome fragmentHome) {
        this.fragmentHome = fragmentHome;
        this.mContext = fragmentHome.getContext();
        this.mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public WeatherAPIController(FragmentSearch fragmentSearch) {
        this.fragmentSearch = fragmentSearch;
        this.mContext = fragmentSearch.getContext();
        this.mRequestQueue = Volley.newRequestQueue(mContext);
    }

    private int getWeatherIcon(String weatherMain) {
        switch (weatherMain) {
            case "Clouds":
                return R.drawable.cloudy;
            case "Rain":
                return R.drawable.rainy;
            case "Clear":
                return R.drawable.clear;
            case "Drizzle":
                return R.drawable.rainy;
            case "Thunderstorm":
                return R.drawable.storm;
            case "Mist":
                return R.drawable.mist;
        }
        return 0;
    }

    public void getWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherObj = weatherArray.getJSONObject(0);
                            String weatherMain = weatherObj.getString("main");
                            int weatherIconId = getWeatherIcon(weatherMain);
                            String weatherDescription = weatherObj.getString("description");
                            weatherDescription = weatherDescription.substring(0, 1).toUpperCase() + weatherDescription.substring(1);

                            JSONObject mainObj = response.getJSONObject("main");
                            double mainTemp = mainObj.getDouble("temp");
                            double mainTempMin = mainObj.getDouble("temp_min");
                            double mainTempMax = mainObj.getDouble("temp_max");

                            int mainHumidity = mainObj.getInt("humidity");

                            JSONObject windObj = response.getJSONObject("wind");
                            int windSpeed = windObj.getInt("speed");

                            JSONObject rainObj = response.optJSONObject("rain");
                            int rainVolume = 0;
                            if (rainObj != null) {
                                rainVolume = rainObj.getInt("1h");
                            }

                            long lDate = response.getLong("dt");
                            Date date = new Date(lDate * 1000L);
                            SimpleDateFormat dateFormat = new SimpleDateFormat(", EEEE");
                            String currentTime = dateFormat.format(date);

                            Weather weather = new Weather(currentTime, weatherIconId, mainTemp, mainTempMin, mainTempMax, rainVolume, windSpeed, mainHumidity, weatherDescription);
                            fragmentHome.initWeather(weather);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        mRequestQueue.add(jsonObjectRequest);
    }

    public void getWeathers(String city) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY + "&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weatherArray = response.getJSONArray("list");
                            ArrayList<Weather> weatherDataList = new ArrayList<>();
                            for (int i = 0; i < weatherArray.length(); i++) {
                                JSONObject weatherObject = weatherArray.getJSONObject(i);
                                JSONObject mainObject = weatherObject.getJSONObject("main");
                                JSONArray weatherDetailsArray = weatherObject.getJSONArray("weather");
                                JSONObject weatherDetailsObject = weatherDetailsArray.getJSONObject(0);

                                long dateTime = weatherObject.getLong("dt");
                                Date date = new Date(dateTime * 1000L);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                String todayDate = sdf.format(new Date());
                                String weatherDate = sdf.format(date);
                                String time = "";
                                if (weatherDate.equals(todayDate)) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE\nHH:mm a");
                                    time = dateFormat.format(date);
                                    double temperature = mainObject.getDouble("temp");
                                    String weatherMain = weatherDetailsObject.getString("main");

                                    Weather weather = new Weather(time, getWeatherIcon(weatherMain), temperature);
                                    weatherDataList.add(weather);
                                }
                            }
                            fragmentHome.updateRecycleViewToday(weatherDataList);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Error fetching weather data", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        mRequestQueue.add(jsonObjectRequest);
    }

    public void getFutureWeathers(String city) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY + "&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weatherArray = response.getJSONArray("list");
                            HashMap<String, ArrayList<Weather>> weatherMap = new HashMap<>();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                            for (int i = 0; i < weatherArray.length(); i++) {
                                JSONObject weatherObject = weatherArray.getJSONObject(i);
                                JSONObject mainObject = weatherObject.getJSONObject("main");
                                JSONArray weatherDetailsArray = weatherObject.getJSONArray("weather");
                                JSONObject weatherDetailsObject = weatherDetailsArray.getJSONObject(0);

                                long dateTime = weatherObject.getLong("dt");
                                Date date = new Date(dateTime * 1000L);
                                String weatherDate = sdf.format(date);

                                double temp_min = mainObject.getDouble("temp_min");
                                double temp_max = mainObject.getDouble("temp_max");
                                String weatherMain = weatherDetailsObject.getString("main");

                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                                String dayOfWeek = dateFormat.format(date);

                                Weather weather = new Weather(dayOfWeek, getWeatherIcon(weatherMain), temp_min, temp_max);

                                if (!weatherMap.containsKey(weatherDate)) {
                                    weatherMap.put(weatherDate, new ArrayList<Weather>());
                                }
                                weatherMap.get(weatherDate).add(weather);
                            }

                            Calendar calendar = Calendar.getInstance();
                            sdf.setTimeZone(calendar.getTimeZone());

                            ArrayList<Weather> futureWeathers = new ArrayList<>();

                            for (int i = 0; i < 3; i++) {
                                ArrayList<Weather> dayWeather = new ArrayList<>();
                                calendar.add(Calendar.DAY_OF_YEAR, 1);
                                String dateKey = sdf.format(calendar.getTime());
                                if (weatherMap.containsKey(dateKey)) {
                                    dayWeather = weatherMap.get(dateKey);
                                }
                                futureWeathers.add(dayWeather.get(0));
                            }

                            fragmentHome.initRecycleViewFuture(futureWeathers);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Error fetching weather data", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        mRequestQueue.add(jsonObjectRequest);
    }
}
