package com.example.weatherapp.Model;

import java.text.SimpleDateFormat;

public class Weather {
    private String time;
    private int picId;
    private double temp, min_temp, max_temp;
    private int rain, wind, humidity;
    private String description;

    public Weather(String time, int picId, double temp) {
        this.time = time;
        this.picId = picId;
        this.temp = temp;
    }

    public Weather(String time, int picId, double min_temp, double max_temp) {
        this.time = time;
        this.picId = picId;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
    }

    public Weather(String time, int picId, double temp, double min_temp, double max_temp, int rain, int wind, int humidity, String description) {
        this.time = time;
        this.picId = picId;
        this.temp = temp;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
        this.rain = rain;
        this.wind = wind;
        this.humidity = humidity;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(double min_temp) {
        this.min_temp = min_temp;
    }

    public double getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(double max_temp) {
        this.max_temp = max_temp;
    }

    public int getRain() {
        return rain;
    }

    public void setRain(int rain) {
        this.rain = rain;
    }

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
