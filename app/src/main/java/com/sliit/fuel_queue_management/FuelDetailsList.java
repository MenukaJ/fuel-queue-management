package com.sliit.fuel_queue_management;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sliit.fuel_queue_management.model.FuelDetails;
import com.sliit.fuel_queue_management.model.FuelDetailsAdapter;
import com.sliit.fuel_queue_management.model.FuelStation;
import com.sliit.fuel_queue_management.model.Owner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Fuel details list.
 */
public class FuelDetailsList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RequestQueue requestQueue;
    private SwipeRefreshLayout refresh;
    private ArrayList<FuelDetails> fuelDetails = new ArrayList<>();
    private JsonArrayRequest arrayRequest;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private FuelDetailsAdapter fuelDetailsAdapter;
    private String url = "https://fuel-queue-management.herokuapp.com/api/FuelDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_details_list);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipedown);
        recyclerView = (RecyclerView) findViewById(R.id.fuelList);

        dialog = new Dialog(this);

        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                fuelDetails.clear();
                getData();
            }
        });
    }

    private void getData() {
        refresh.setRefreshing(true);

        Intent intent = getIntent();
        String myEmail = intent.getStringExtra("email");
        System.out.println(myEmail);
        arrayRequest = new JsonArrayRequest(url + "/owner/" + myEmail, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        FuelDetails fuelDet = new FuelDetails();
                        fuelDet.setId(jsonObject.getString("id"));
                        fuelDet.setType(jsonObject.getString("type"));
                        fuelDet.setArrivalTime(jsonObject.getString("arrivalTime"));
                        fuelDet.setFinishTime(jsonObject.getString("finishTime"));
                        fuelDet.setDate(jsonObject.getString("date"));
                        fuelDet.setStatus(jsonObject.getString("status"));
                        Gson gson = new Gson();
                        FuelStation fuelStation = gson.fromJson(jsonObject.getString("fuelStation"), FuelStation.class);
                        fuelDet.setFuelStation(fuelStation);
                        fuelDetails.add(fuelDet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterPush(fuelDetails);
                refresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(FuelDetailsList.this);
        requestQueue.add(arrayRequest);
    }

    private void adapterPush(ArrayList<FuelDetails> fuelDetails) {
        fuelDetailsAdapter = new FuelDetailsAdapter(this, fuelDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fuelDetailsAdapter);
    }

    public void addFuelDetails(View view) {
        TextView close, type;
        final EditText fuelType;
        Button submit;

        dialog.setContentView(R.layout.activity_modify_fuel_details);

        close = (TextView) dialog.findViewById(R.id.txtClose);
        type = (TextView) dialog.findViewById(R.id.modFuel);
        type.setText("Save Details");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        fuelType = (EditText) dialog.findViewById(R.id.edFuelType);
        submit = (Button) dialog.findViewById(R.id.edBtnFuelDet);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = fuelType.getText().toString();
                Submit(data);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void Submit(String data) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                refresh.post(new Runnable() {
                    @Override
                    public void run() {
                        fuelDetails.clear();
                        getData();
                    }
                });
                Toast.makeText(getApplicationContext(), "Record saved successfully", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cat", data);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onRefresh() {
        fuelDetails.clear();
        getData();
    }
}