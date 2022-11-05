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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private EditText fuelType, fuelArrival, fuelFinished, fuelStatus;
    private TextView fuelStationName, fuelDate;
    private FuelStation fuelStationClass = new FuelStation();
    private String url = "https://fuel-queue-management.herokuapp.com/api/FuelDetails";
    private String stationUrl = "https://fuel-queue-management.herokuapp.com/api/FuelStation";

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
                getStationData();
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
        TextView close, header;
        //final EditText fuelType, fuelArrival, fuelFinished, fuelDate, fuelStatus, fuelStationName;
        Button submit;

        dialog.setContentView(R.layout.activity_modify_fuel_details);

        header = (TextView) dialog.findViewById(R.id.modFuel);
        header.setText("Save Details");

        close = (TextView) dialog.findViewById(R.id.txtClose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        fuelType = (EditText) dialog.findViewById(R.id.edFuelType);
        fuelArrival = (EditText) dialog.findViewById(R.id.edFuelArrival);
        fuelFinished = (EditText) dialog.findViewById(R.id.edFuelFinish);
        fuelDate = (TextView) dialog.findViewById(R.id.edFuelDate);
        fuelDate.setText(getCurrentDate());
        fuelStatus = (EditText) dialog.findViewById(R.id.edFuelStatus);
        fuelStationName = (TextView) dialog.findViewById(R.id.edFuelStation);
        fuelStationName.setText(fuelStationClass.getName());
        submit = (Button) dialog.findViewById(R.id.edBtnFuelDet);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fuelType.getText().toString().isEmpty()) {
                    Toast.makeText(FuelDetailsList.this, "Fuel Type is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fuelArrival.getText().toString().isEmpty()) {
                    Toast.makeText(FuelDetailsList.this, "Arrived time is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fuelArrival.getText().toString().matches("^$|(\\d{2}):(\\d{2})$")) {
                    Toast.makeText(FuelDetailsList.this, "Arrived time should be in HH:mm pattern.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fuelFinished.getText().toString().isEmpty()) {
                    Toast.makeText(FuelDetailsList.this, "Finished time is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fuelFinished.getText().toString().matches("^$|(\\d{2}):(\\d{2})$")) {
                    Toast.makeText(FuelDetailsList.this, "Finished time should be in HH:mm pattern.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fuelDate.getText().toString().isEmpty()) {
                    Toast.makeText(FuelDetailsList.this, "Date is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fuelStatus.getText().toString().isEmpty()) {
                    Toast.makeText(FuelDetailsList.this, "Status is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fuelStatus.getText().toString().matches("^$|Available|Finished")) {
                    Toast.makeText(FuelDetailsList.this, "Fuel status is should be Available or Finished.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fuelStationName.getText().toString().isEmpty()) {
                    Toast.makeText(FuelDetailsList.this, "Station name is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveData(fuelType.getText().toString(), fuelArrival.getText().toString(), fuelFinished.getText().toString(), fuelDate.getText().toString(), fuelStatus.getText().toString(), fuelStationClass);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void saveData(String pFuelType, String pFuelArrival, String pFuelFinished, String pFuelDate, String pFuelStatus, FuelStation fuelStationObject) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", "");
            jsonBody.put("type", pFuelType);
            jsonBody.put("arrivalTime", pFuelArrival);
            jsonBody.put("finishTime", pFuelFinished);
            jsonBody.put("date", pFuelDate);
            jsonBody.put("status", pFuelStatus);

            String fuelStat = new Gson().toJson(fuelStationObject);
            JSONObject fuelStatJob = new JSONObject(fuelStat);
            jsonBody.put("fuelStation", fuelStatJob);
            final String requestBody = jsonBody.toString();
            Log.i("VOLLEY", requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
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
                    Log.i("VOLLEY", error.toString());
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        fuelDetails.clear();
        getData();
    }

    private void getStationData() {
        Intent intent = getIntent();
        String myEmail = intent.getStringExtra("email");
        System.out.println(myEmail);
        System.out.println(stationUrl + "/owner/" + myEmail);
        arrayRequest = new JsonArrayRequest(stationUrl + "/owner/" + myEmail, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println(response);
                    JSONObject jsonObject = response.getJSONObject(0);
                    fuelStationClass.setId(jsonObject.getString("id"));
                    fuelStationClass.setName(jsonObject.getString("name"));
                    fuelStationClass.setContactNo(jsonObject.getString("contactNo"));
                    fuelStationClass.setAddressLine1(jsonObject.getString("addressLine1"));
                    fuelStationClass.setAddressLine2(jsonObject.getString("addressLine2"));
                    fuelStationClass.setAddressLine3(jsonObject.getString("addressLine3"));
                    Gson gson = new Gson();
                    Owner owner = gson.fromJson(jsonObject.getString("owner"), Owner.class);
                    fuelStationClass.setOwner(owner);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(FuelDetailsList.this);
        requestQueue.add(arrayRequest);
    }

    private String getCurrentDate() {
        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        return format.format(currentDate);
    }
}