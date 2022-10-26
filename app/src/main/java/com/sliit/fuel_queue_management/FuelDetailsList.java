package com.sliit.fuel_queue_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * The type Fuel details list.
 */
public class FuelDetailsList extends AppCompatActivity {

    String myUrl = "https://fuel-queue-management.herokuapp.com/api/FuelDetails";
    TextView type;
    TextView date;
    TextView status;
    TextView fuelStation;
    TextView fuelArriveTime;
    TextView fuelFinishTime;
    ProgressDialog progressDialog;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public void onBackPressed() {
        FuelDetailsList.this.finish();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_details_list);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        FuelDetailsList.MyAsyncTasks myAsyncTasks = new FuelDetailsList.MyAsyncTasks();
        myAsyncTasks.execute();

        getSupportActionBar().setTitle("Fuel Details");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A443E9")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        type = (TextView) findViewById(R.id.type);
        date = (TextView) findViewById(R.id.date);
        status = (TextView) findViewById(R.id.status);
        fuelStation = (TextView) findViewById(R.id.fuelStation);
        fuelArriveTime = (TextView) findViewById(R.id.fuelArriveTime);
        fuelFinishTime = (TextView) findViewById(R.id.fuelFinishTime);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FuelDetailsList.this);
            progressDialog.setMessage("processing results");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    Intent intent = getIntent();
                    String s2 = intent.getStringExtra("email");
                    final String encodedURL = URLEncoder.encode(s2, "UTF-8");
                    url = new URL(myUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            try {
                JSONArray jSONArray = new JSONArray(s);
                JSONObject jsonObject = jSONArray.getJSONObject(0);
                JSONObject fuelStationObject = jsonObject.optJSONObject("fuelStation");
                type.setText("Fuel Type : "+jsonObject.getString("type"));
                date.setText("Date : "+jsonObject.getString("date"));
                status.setText("Status : "+jsonObject.getString("status"));
                fuelStation.setText(fuelStationObject.getString("name") + " Station");
                fuelArriveTime.setText("Arrival Time : "+jsonObject.getString("arrivalTime"));
                fuelFinishTime.setText("Finished Time : "+jsonObject.getString("finishTime"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}