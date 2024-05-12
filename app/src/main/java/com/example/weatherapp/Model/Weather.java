package com.example.weatherapp.Model;

import java.text.SimpleDateFormat;

public class Weather {
    private String time;
    private int picId;
    private double temp;

    public Weather(String time, int picId, double temp) {
        this.time = time;
        this.picId = picId;
        this.temp = temp;
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
}
