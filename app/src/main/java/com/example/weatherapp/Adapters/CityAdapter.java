package com.example.weatherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.weatherapp.Activities.MainActivity;
import com.example.weatherapp.Controllers.CityAPIController;
import com.example.weatherapp.R;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.viewHolder> {
    Context context;
    private final MainActivity mainActivity;
    private CityAPIController cityAPIController;
    private ViewPager viewPager;
    private String cityName;

    public CityAdapter(String cityName, MainActivity mainActivity) {
        this.cityName = cityName;
        this.mainActivity = mainActivity;
    }

    public CityAdapter(Context context, CityAPIController cityAPIController, MainActivity mainActivity) {
        this.context = context;
        this.cityAPIController = cityAPIController;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public CityAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        context = parent.getContext();
        this.viewPager = mainActivity.getViewPager();
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.viewHolder holder, int position) {
        holder.tv_city.setText(cityName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToFragmentHome(cityName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tv_city;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tv_city = itemView.findViewById(R.id.tv_city);
        }
    }

    private void switchToFragmentHome(String cityName) {
        mainActivity.updateHomeFragment(cityName);
        viewPager.setCurrentItem(0);
    }
}
