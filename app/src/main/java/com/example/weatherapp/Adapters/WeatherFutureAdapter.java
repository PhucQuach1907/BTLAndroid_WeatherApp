package com.example.weatherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Model.Weather;
import com.example.weatherapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WeatherFutureAdapter extends RecyclerView.Adapter<WeatherFutureAdapter.viewHolder> {
    ArrayList<Weather> items;
    Context context;

    public WeatherFutureAdapter(ArrayList<Weather> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public WeatherFutureAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_future_weather, parent, false);
        context = parent.getContext();
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherFutureAdapter.viewHolder holder, int position) {
        holder.tv_date.setText(items.get(position).getTime());
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String min_temp = decimalFormat.format(items.get(position).getMin_temp());
        String max_temp = decimalFormat.format(items.get(position).getMax_temp());
        holder.min_temp.setText(min_temp + "°");
        holder.max_temp.setText(max_temp + "°");
        holder.weather_icon.setImageResource(items.get(position).getPicId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView tv_date, min_temp, max_temp;
        ImageView weather_icon;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_date);
            min_temp = itemView.findViewById(R.id.min_temp);
            max_temp = itemView.findViewById(R.id.max_temp);
            weather_icon = itemView.findViewById(R.id.weather_icon);
        }
    }
}
