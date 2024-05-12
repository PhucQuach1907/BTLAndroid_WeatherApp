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

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.viewHolder> {
    ArrayList<Weather> items;
    Context context;

    public WeatherAdapter(ArrayList<Weather> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public WeatherAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_weather, parent, false);
        context = parent.getContext();
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.viewHolder holder, int position) {
        holder.tv_time.setText(items.get(position).getTime());
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String temperature = decimalFormat.format(items.get(position).getTemp());
        holder.tv_temp.setText(temperature + "°");
        holder.pic.setImageResource(items.get(position).getPicId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView tv_time, tv_temp;
        ImageView pic;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_time = itemView.findViewById(R.id.tv_time);
            tv_temp = itemView.findViewById(R.id.tv_temp);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
