package com.example.weatherapp.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.weatherapp.Adapters.ViewPagerAdapter;
import com.example.weatherapp.Fragments.FragmentHome;
import com.example.weatherapp.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
    }

    public void updateHomeFragment(String cityName) {
        FragmentHome fragmentHome = (FragmentHome) adapter.getRegisteredFragment(0);
        if (fragmentHome != null) {
            fragmentHome.updateCity(cityName);
        }
        viewPager.setCurrentItem(0);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}