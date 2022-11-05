package com.sliit.fuel_queue_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.sliit.fuel_queue_management.Sql.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserProfile extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    String myUrl = "https://fuel-queue-management.herokuapp.com/user/";

    TextView textViewFirstName;
    TextView textViewLastName;
    TextView textViewEmail;
    TextView textViewContactNo;
    TextView textViewAddress;
    TextView textViewNIC;
    TextView textViewPassword;
    TextView textViewVehicleNo;
    TextView textViewVehicleType;

    Button update;

    static ProgressDialog progressDialog;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    boolean recordExist=true;

    DBHelper dbHelper;

    @Override
    public void onBackPressed() {
        UserProfile.this.finish();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        drawerLayout = findViewById(R.id.user_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setTitle("My Account");

        dbHelper = new DBHelper(this);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A443E9")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewFirstName = findViewById(R.id.textView_first_name);
        textViewLastName = findViewById(R.id.textView_last_name);
        textViewEmail = findViewById(R.id.textView_email);
        textViewContactNo = findViewById(R.id.textView_phone);
        textViewAddress = findViewById(R.id.textView_address);
        textViewNIC = findViewById(R.id.textView_nic);
        textViewPassword = findViewById(R.id.textView_password);
        textViewVehicleNo = findViewById(R.id.textView_vehicle_no);
        textViewVehicleNo = findViewById(R.id.textView_vehicle_type);

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("email");

        Cursor cursor = dbHelper.getUserId(userEmail);

        JSONObject jsonObject = new JSONObject();
        try {
            textViewEmail.setText(jsonObject.getString("userID"));
            textViewPassword.setText(jsonObject.getString("password"));
            textViewContactNo.setText(jsonObject.getString("number"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            Intent intent = new Intent(UserProfile.this,Login.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.nav_account) {
            Intent intent = new Intent(UserProfile.this,UserProfile.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public class GetUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(UserProfile.this);
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
                    url = new URL(myUrl+encodedURL);
                    //open a URL coonnection

                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();

                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();
                    }

                    // return the data to onPostExecute method
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
                JSONObject jsonObject = new JSONObject(s);
                // JSONArray jsonArray1 = jsonObject.getJSONArray(jsonObject);
                //JSONArray jsonArray1 = new JSONArray(s);

//                JSONObject  = jsonObject.optJSONObject("user");
//                JSONObject fuelStat = jsonObject.optJSONObject("fuelStation");
//
//                //JSONObject fuelDetails = new JSONObject(responce);
//
//                //JSONObject jsonObject1 =jsonArray1.getJSONObject(0);
//                String count = jsonObject.getString("count");
//
//                //Show the Textview after fetching data
//                queueCount.setVisibility(View.VISIBLE);
//                //Display data with the Textview
//                queueCount.setText(count);
//                vehicleType.setText(userObj.getString("vehicleType"));
//                vehicleNumber.setText(userObj.getString("vehicleNo"));
//                fuelStation.setText(fuelStat.getString("name"));
//                joinTime.setText("Join Time: "+jsonObject.getString("arrivalTime"));
//                existTime.setText("Departure Time: "+jsonObject.getString("departureTime"));
//                fuelArriveTime.setText("Fuel Arrival Time: "+jsonObject.getString("arrivalTime"));
//                fuelFinishTime.setText("Fuel Departure Time: "+jsonObject.getString("departureTime"));

                System.out.println("This is a test"+jsonObject.toString());
                System.out.println("This is a test"+jsonObject.getString("fistName"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}