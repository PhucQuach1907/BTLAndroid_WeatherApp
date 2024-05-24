package com.example.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Activities.MainActivity;
import com.example.weatherapp.Adapters.CityAdapter;
import com.example.weatherapp.Controllers.CityAPIController;
import com.example.weatherapp.Controllers.WeatherAPIController;
import com.example.weatherapp.R;

public class FragmentSearch extends Fragment {
    private MainActivity mainActivity;
    private WeatherAPIController weatherAPIController;
    private CityAPIController cityAPIController;
    private CityAdapter cityAdapter;
    private EditText etSearch;
    private RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        weatherAPIController = new WeatherAPIController(this);
        cityAPIController = new CityAPIController(this);
        cityAdapter = new CityAdapter(getContext(), cityAPIController, mainActivity);
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch = view.findViewById(R.id.et_enter_name_city);
        recyclerView = view.findViewById(R.id.rv_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                cityAPIController.getCityName(newText, new CityAPIController.CityNameCallback() {
                    @Override
                    public void onCityNameReceived(String cityName) {
                        cityAdapter = new CityAdapter(cityName, mainActivity);
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(cityAdapter);
                    }
                });
            }
        });
    }
}