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
import android.widget.EditText;
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

    EditText textViewFirstName;
    EditText textViewLastName;
    TextView textViewEmail;
    TextView textViewContactNo;
    EditText textViewAddress;
    EditText textViewNIC;
    TextView textViewPassword;
    EditText textViewVehicleNo;
    EditText textViewVehicleType;
    TextView textViewId;

    Button btnUpdate;

    static ProgressDialog progressDialog;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    boolean recordExist = false;

    DBHelper dbHelper;

    public boolean isRecordExist() {
        return recordExist;
    }

    public void setRecordExist(boolean recordExist) {
        this.recordExist = recordExist;
    }

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
        textViewVehicleType = findViewById(R.id.textView_vehicle_type);
        textViewId = findViewById(R.id.textView_id);
        btnUpdate = findViewById(R.id.btn_submit);

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("email");
        System.out.println("This is a test" + userEmail);

        Cursor cursor = dbHelper.getUserId(userEmail);
        while (cursor.moveToNext()) {
            textViewPassword.setText(cursor.getString(2));
            textViewEmail.setText(cursor.getString(0));
            textViewFirstName.setText(cursor.getString(1));
            textViewContactNo.setText(cursor.getString(3));

        }

        GetUser getUser = new GetUser();
        getUser.execute();
        //recreate();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recordExist) {
                    //call update
                    UpdateUser updUser = new UpdateUser();
                    updUser.execute();
                }else {
                    //call post
                    CreateUser creUser = new CreateUser();
                    creUser.execute();
                }

            }
        });

        System.out.println("Record Exist is "+recordExist);



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
            Intent intent = new Intent(UserProfile.this, Login.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_account) {
            Intent intent = new Intent(UserProfile.this, UserProfile.class);
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
            progressDialog.setMessage("processing results 3");
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
                    url = new URL(myUrl + encodedURL);
                    //open a URL coonnection
                    System.out.println("This is a test" + url);
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
                if (!jsonObject.isNull("id")) {
                    textViewFirstName.setText(jsonObject.getString("fistName"));
                    textViewLastName.setText(jsonObject.getString("lastName"));
                    textViewEmail.setText(jsonObject.getString("email"));
                    textViewContactNo.setText(jsonObject.getString("contactNo"));
                    textViewAddress.setText(jsonObject.getString("addressLine1"));
                    textViewNIC.setText(jsonObject.getString("nic"));
                    textViewPassword.setText(jsonObject.getString("password"));
                    textViewVehicleNo.setText(jsonObject.getString("vehicleNo"));
                    textViewVehicleType.setText(jsonObject.getString("vehicleType"));
                    textViewId.setText(jsonObject.getString("id"));
                    setRecordExist(true);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class CreateUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(UserProfile.this);
            progressDialog.setMessage("processing results 2");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", textViewId.getText().toString());
                jsonObject.put("fistName", textViewFirstName.getText().toString());
                jsonObject.put("lastName", textViewLastName.getText().toString());
                jsonObject.put("email", textViewEmail.getText().toString());
                jsonObject.put("contactNo", textViewContactNo.getText().toString());
                jsonObject.put("nic", textViewNIC.getText().toString());
                jsonObject.put("addressLine1", textViewAddress.getText().toString());
                jsonObject.put("vehicleNo", textViewVehicleNo.getText().toString());
                jsonObject.put("vehicleType", textViewVehicleType.getText().toString());
                jsonObject.put("password", textViewPassword.getText().toString());

                final String urlAddress = "https://fuel-queue-management.herokuapp.com/api/User/" + textViewId.getText().toString();
                URL url = new URL(urlAddress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                Log.i("JSON", jsonObject.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonObject.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG", conn.getResponseMessage());

                conn.disconnect();

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("OnPostExecute method called");
                }
            });

            thread.start();
        }
    }

    private class UpdateUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(UserProfile.this);
            progressDialog.setMessage("processing results 1");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", textViewId.getText().toString());
                jsonObject.put("fistName", textViewFirstName.getText().toString());
                jsonObject.put("lastName", textViewLastName.getText().toString());
                jsonObject.put("email", textViewEmail.getText().toString());
                jsonObject.put("contactNo", textViewContactNo.getText().toString());
                jsonObject.put("nic", textViewNIC.getText().toString());
                jsonObject.put("addressLine1", textViewAddress.getText().toString());
                jsonObject.put("vehicleNo", textViewVehicleNo.getText().toString());
                jsonObject.put("vehicleType", textViewVehicleType.getText().toString());
                jsonObject.put("password", textViewPassword.getText().toString());

                final String urlAddress = "https://fuel-queue-management.herokuapp.com/api/User/" + textViewId.getText().toString();
                URL url = new URL(urlAddress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                Log.i("JSON", jsonObject.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonObject.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG", conn.getResponseMessage());

                conn.disconnect();

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("OnPostExecute method called");
                }
            });

            thread.start();
        }
    }
}