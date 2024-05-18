package com.example.weatherapp.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.Adapters.WeatherAdapter;
import com.example.weatherapp.Adapters.WeatherFutureAdapter;
import com.example.weatherapp.Controllers.WeatherAPIController;
import com.example.weatherapp.Model.Weather;
import com.example.weatherapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private WeatherAPIController weatherAPIController;
    private static final long INTERVAL = 10 * 60 * 1000;
    static final String API_KEY = "d9c0c317f90b32c51bb03768b11eb5a0";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final Handler handler = new Handler();
    private RecyclerView.Adapter weatherAdapter, weatherFutureAdapter;
    private RecyclerView recyclerViewToday, recyclerViewFuture;
    private TextView tv_description, tv_date, tv_temp, tv_minMaxTemp, tv_rain, tv_wind, tv_humidity, tv_today, tv_future;
    private ImageView iv_weather;
    private SearchView searchView;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            weatherAPIController.getWeather("hanoi");
            weatherAPIController.getWeathers("hanoi");
            weatherAPIController.getFutureWeathers("hanoi");
            handler.postDelayed(this, INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        weatherAPIController = new WeatherAPIController(this);
//        searchWeatherByCity();
        handler.post(runnable);
        tv_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_today.setTextColor(Color.parseColor("#DCA900"));
                tv_future.setTextColor(Color.parseColor("#ffffff"));
                recyclerViewToday.setVisibility(View.VISIBLE);
                recyclerViewFuture.setVisibility(View.GONE);
            }
        });

        tv_future.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_future.setTextColor(Color.parseColor("#DCA900"));
                tv_today.setTextColor(Color.parseColor("#ffffff"));
                recyclerViewToday.setVisibility(View.GONE);
                recyclerViewFuture.setVisibility(View.VISIBLE);
            }
        });
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
        tv_today = findViewById(R.id.tv_today);
        tv_future = findViewById(R.id.tv_future);
        recyclerViewToday = findViewById(R.id.recyclerViewToday);
        recyclerViewFuture = findViewById(R.id.recyclerViewFuture);
    }

    public void initWeather(Weather weather) {
        tv_description.setText(weather.getDescription());
        iv_weather.setImageResource(weather.getPicId());
        tv_date.setText(weather.getTime());

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String temp = decimalFormat.format(weather.getTemp());
        tv_temp.setText(temp + "°");
        String min_temp = decimalFormat.format(weather.getMin_temp());
        String max_temp = decimalFormat.format(weather.getMin_temp());
        String minMaxTemp = "H:" + min_temp + "°" + " L:" + max_temp + "°";
        tv_minMaxTemp.setText(minMaxTemp);

        String rainVolume = String.valueOf(weather.getRain()) + " mm";
        tv_rain.setText(rainVolume);

        String wind = String.valueOf(weather.getWind()) + " m/s";
        tv_wind.setText(wind);

        String humidity = String.valueOf(weather.getHumidity()) + "%";
        tv_humidity.setText(humidity);
    }

    public void initRecycleViewToday(ArrayList<Weather> items) {
        recyclerViewToday.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        weatherAdapter = new WeatherAdapter(items);
        recyclerViewToday.setAdapter(weatherAdapter);
    }

    public void initRecycleViewFuture(ArrayList<Weather> items) {
        recyclerViewFuture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        weatherFutureAdapter = new WeatherFutureAdapter(items);
        recyclerViewFuture.setAdapter(weatherFutureAdapter);
    }

//    private void searchWeatherByCity() {
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                getWeather(query);
//                getWeathers(query);
//                getFutureWeathers(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//    }
//
//    private void showCityNotFoundDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Không tìm thấy thành phố")
//                .setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

}