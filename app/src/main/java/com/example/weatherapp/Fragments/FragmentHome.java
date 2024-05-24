package com.example.weatherapp.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Adapters.WeatherAdapter;
import com.example.weatherapp.Adapters.WeatherFutureAdapter;
import com.example.weatherapp.Controllers.WeatherAPIController;
import com.example.weatherapp.Model.Weather;
import com.example.weatherapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private static final String ARG_CITY_NAME = "city_name";
    private String cityName = "hanoi";
    private WeatherAPIController weatherAPIController;
    private static final long INTERVAL = 10 * 60 * 1000;
    private final Handler handler = new Handler();
    private WeatherAdapter weatherAdapter;
    private WeatherFutureAdapter weatherFutureAdapter;
    private RecyclerView recyclerViewToday, recyclerViewFuture;
    private TextView tv_description, tv_date, tv_temp, tv_minMaxTemp, tv_rain, tv_wind, tv_humidity, tv_today, tv_future;
    private ImageView iv_weather;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            weatherAPIController.getWeather(cityName);
            weatherAPIController.getWeathers(cityName);
            weatherAPIController.getFutureWeathers(cityName);
            handler.postDelayed(this, INTERVAL);
        }
    };

    public static FragmentHome newInstance(String cityName) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityName = getArguments().getString(ARG_CITY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);
        weatherAPIController = new WeatherAPIController(this);
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

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCity(cityName);
    }

    public void updateCity(String cityName) {
        this.cityName = cityName;
        handler.post(runnable);
    }

    private void initView(View view) {
        tv_description = view.findViewById(R.id.tv_description);
        tv_date = view.findViewById(R.id.tv_date);
        tv_temp = view.findViewById(R.id.tv_temp);
        tv_minMaxTemp = view.findViewById(R.id.tv_minMaxTemp);
        iv_weather = view.findViewById(R.id.iv_weather);
        tv_rain = view.findViewById(R.id.tv_rain);
        tv_wind = view.findViewById(R.id.tv_wind);
        tv_humidity = view.findViewById(R.id.tv_humidity);
        tv_today = view.findViewById(R.id.tv_today);
        tv_future = view.findViewById(R.id.tv_future);
        recyclerViewToday = view.findViewById(R.id.recyclerViewToday);
        recyclerViewFuture = view.findViewById(R.id.recyclerViewFuture);
    }

    public void initWeather(Weather weather) {
        tv_description.setText(weather.getDescription());
        iv_weather.setImageResource(weather.getPicId());
        String tv_cityName = cityName.substring(0, 1).toUpperCase() + cityName.substring(1);
        tv_date.setText(tv_cityName + weather.getTime());

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String temp = decimalFormat.format(weather.getTemp());
        tv_temp.setText(temp + "°");
        String min_temp = decimalFormat.format(weather.getMin_temp());
        String max_temp = decimalFormat.format(weather.getMin_temp());
        String minMaxTemp = "H:" + min_temp + "°" + " L:" + max_temp + "°";
        tv_minMaxTemp.setText(minMaxTemp);

        String rainVolume = weather.getRain() + " mm";
        tv_rain.setText(rainVolume);

        String wind = weather.getWind() + " m/s";
        tv_wind.setText(wind);

        String humidity = weather.getHumidity() + "%";
        tv_humidity.setText(humidity);
    }

    public void updateRecycleViewToday(ArrayList<Weather> items) {
        if (weatherAdapter != null) {
            weatherAdapter.updateWeatherData(items);
        } else {
            recyclerViewToday.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            weatherAdapter = new WeatherAdapter(items);
            recyclerViewToday.setAdapter(weatherAdapter);
        }
    }

    public void initRecycleViewFuture(ArrayList<Weather> items) {
        recyclerViewFuture.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        weatherFutureAdapter = new WeatherFutureAdapter(items);
        recyclerViewFuture.setAdapter(weatherFutureAdapter);
    }
}
