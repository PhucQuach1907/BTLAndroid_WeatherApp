package com.example.weatherapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.Adapters.WeatherAdapter;
import com.example.weatherapp.Model.Weather;
import com.example.weatherapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final long INTERVAL = 10 * 60 * 1000;
    static final String API_KEY = "d9c0c317f90b32c51bb03768b11eb5a0";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final Handler handler = new Handler();
    private RecyclerView.Adapter weatherAdapter;
    private RecyclerView recyclerView;
    private TextView tv_description, tv_date, tv_temp, tv_minMaxTemp, tv_rain, tv_wind, tv_humidity;
    private ImageView iv_weather;
    private SearchView searchView;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getWeather("hanoi");
            getWeathers("hanoi");
            handler.postDelayed(this, INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        searchWeatherByCity();
        handler.post(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void initView() {
        tv_description = findViewById(R.id.tv_description);
        tv_date = findViewById(R.id.tv_date);
        tv_temp = findViewById(R.id.tv_temp);
        tv_minMaxTemp = findViewById(R.id.tv_minMaxTemp);
        iv_weather = findViewById(R.id.iv_weather);
        tv_rain = findViewById(R.id.tv_rain);
        tv_wind = findViewById(R.id.tv_wind);
        tv_humidity = findViewById(R.id.tv_humidity);
        searchView = findViewById(R.id.searchView);
    }

    private void initRecycleView(ArrayList<Weather> items) {
        recyclerView = findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        weatherAdapter = new WeatherAdapter(items);
        recyclerView.setAdapter(weatherAdapter);
    }

    private int getWeatherIcon(String weatherMain) {
        switch (weatherMain) {
            case "Clouds":
                return R.drawable.cloudy;
            case "Rain":
                return R.drawable.rainy;
            case "Clear":
                return R.drawable.clear_sky;
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                            String mainTemp = mainObj.getString("temp") + "°";
                            String mainTempMin = mainObj.getString("temp_min");
                            String mainTempMax = mainObj.getString("temp_max");
                            String minMaxTemp = "H:" + mainTempMax + "°" + " L:" + mainTempMin + "°";
                            String mainHumidity = mainObj.getString("humidity") + "%";

                            JSONObject windObj = response.getJSONObject("wind");
                            String windSpeed = windObj.getString("speed") + " m/s";

                            JSONObject rainObj = response.optJSONObject("rain");
                            String rainVolume = "0 mm";
                            if (rainObj != null) {
                                rainVolume = rainObj.getString("1h") + " mm";
                            }

                            long lDate = response.getLong("dt");
                            Date date = new Date(lDate * 1000L);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE | MMM dd yyyy");
                            String currentTime = dateFormat.format(date);

                            tv_description.setText(weatherDescription);
                            iv_weather.setImageResource(weatherIconId);
                            tv_date.setText(currentTime);
                            tv_temp.setText(mainTemp);
                            tv_minMaxTemp.setText(minMaxTemp);
                            tv_rain.setText(rainVolume);
                            tv_wind.setText(windSpeed);
                            tv_humidity.setText(mainHumidity);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showCityNotFoundDialog();
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void getWeathers(String city) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY + "&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weatherArray = response.getJSONArray("list");
                            ArrayList<Weather> weatherDataList = new ArrayList<>();
                            for (int i = 0; i < 24; i++) {
                                JSONObject weatherObject = weatherArray.getJSONObject(i);
                                JSONObject mainObject = weatherObject.getJSONObject("main");
                                JSONArray weatherDetailsArray = weatherObject.getJSONArray("weather");
                                JSONObject weatherDetailsObject = weatherDetailsArray.getJSONObject(0);

                                long dateTime = weatherObject.getLong("dt");
                                Date date = new Date(dateTime * 1000L);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE\nMMM dd yyyy | HH a");
                                String time = dateFormat.format(date);
                                double temperature = mainObject.getDouble("temp");
                                String weatherMain = weatherDetailsObject.getString("main");

                                Weather weather = new Weather(time, getWeatherIcon(weatherMain), temperature);
                                weatherDataList.add(weather);
                            }

                            initRecycleView(weatherDataList);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void searchWeatherByCity() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getWeather(query);
                getWeathers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void showCityNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Không tìm thấy thành phố")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}