package com.sliit.fuel_queue_management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sliit.fuel_queue_management.model.FuelDetails;
import com.sliit.fuel_queue_management.model.FuelDetailsAdapter;
import com.sliit.fuel_queue_management.model.FuelStation;
import com.sliit.fuel_queue_management.model.FuelStationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FuelStationsList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RequestQueue requestQueue;
    private SwipeRefreshLayout refresh;
    private ArrayList<FuelStation> fuelStation = new ArrayList<>();
    private JsonArrayRequest arrayRequest;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private FuelStationAdapter fuelStationAdapter;
    private String url = "https://fuel-queue-management.herokuapp.com/api/FuelStation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_stations_list);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipedownStation);
        recyclerView = (RecyclerView) findViewById(R.id.stationList);

        dialog = new Dialog(this);

        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                fuelStation.clear();
                getData();
            }
        });
    }

    private void getData() {
        refresh.setRefreshing(true);

        arrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        FuelStation fuelStat = new FuelStation();
                        fuelStat.setId(jsonObject.getString("id"));
                        fuelStat.setName(jsonObject.getString("name"));
                        fuelStation.add(fuelStat);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterPush(fuelStation);
                refresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(FuelStationsList.this);
        requestQueue.add(arrayRequest);
    }

    private void adapterPush(ArrayList<FuelStation> fuelStation) {
        fuelStationAdapter = new FuelStationAdapter(this, fuelStation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fuelStationAdapter);
    }

    @Override
    public void onRefresh() {
        fuelStation.clear();
        getData();
    }
}