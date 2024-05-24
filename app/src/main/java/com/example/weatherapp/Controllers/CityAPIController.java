package com.example.weatherapp.Controllers;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.Adapters.CityAdapter;
import com.example.weatherapp.Fragments.FragmentSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CityAPIController {
    static final String API_KEY = "d9c0c317f90b32c51bb03768b11eb5a0";
    private final FragmentSearch fragmentSearch;
    private CityAdapter cityAdapter;
    private final Context mContext;
    private final RequestQueue mRequestQueue;
    private SearchView searchView;

    public CityAPIController(FragmentSearch fragmentSearch) {
        this.fragmentSearch = fragmentSearch;
        this.mContext = fragmentSearch.getContext();
        this.mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public interface CityNameCallback {
        void onCityNameReceived(String cityName);
    }

    public void getCityName(String query, final CityNameCallback callback) {
        String url = "https://api.openweathermap.org/geo/1.0/direct?appid=" + API_KEY + "&q=" + query + "&limit=1";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                JSONObject cityObj = response.getJSONObject(0);
                                String cityName = cityObj.getString("name");
                                callback.onCityNameReceived(cityName);
                            } else {
                                callback.onCityNameReceived("Not found city name!");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        mRequestQueue.add(jsonArrayRequest);
    }

}
