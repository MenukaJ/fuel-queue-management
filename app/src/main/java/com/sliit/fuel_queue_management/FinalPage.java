package com.sliit.fuel_queue_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
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

public class FinalPage extends AppCompatActivity {

    String myUrl = "https://fuel-queue-management.herokuapp.com/email/";
    TextView queueCount;
    TextView vehicleType;
    TextView vehicleNumber;
    TextView fuelStation;
    TextView fuelArriveTime;
    TextView fuelFinishTime;
    TextView joinTime;
    TextView existTime;
    Button join;
    Button exist;
    Button complete;

    ProgressDialog progressDialog;

    TextView text;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public void onBackPressed() {
        FinalPage.this.finish();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getSupportActionBar().hide();
        setContentView(R.layout.activity_final_page);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setTitle("Home");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.drawable.background_main)));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A443E9")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setContentView(R.layout.activity_final_page);
        /*text = findViewById(R.id.changeText);
        Intent intent = getIntent();
        String s2 = intent.getStringExtra("email");
        text.setText(s2);*/

        queueCount = (TextView) findViewById(R.id.count);
        vehicleType = (TextView) findViewById(R.id.vehicleType);
        vehicleNumber = (TextView) findViewById(R.id.vehicleNumber);
        fuelStation = (TextView) findViewById(R.id.fuelStation);
        fuelArriveTime = (TextView) findViewById(R.id.fuelArriveTime);
        fuelFinishTime = (TextView) findViewById(R.id.fuelFinishTime);
        joinTime = (TextView) findViewById(R.id.joinTime);
        existTime = (TextView) findViewById(R.id.existTime);
        existTime = (TextView) findViewById(R.id.existTime);
        join = findViewById(R.id.join);
        exist = findViewById(R.id.exist);
        complete = findViewById(R.id.complete);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateJoinTime updateJoinTime = new UpdateJoinTime();
                updateJoinTime.execute();
            }
        });

        exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateExistTime updateExistTime = new UpdateExistTime();
                updateExistTime.execute();
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateCompleteTime updateCompleteTime = new UpdateCompleteTime();
                updateCompleteTime.execute();
            }
        });
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
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(FinalPage.this);
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

                JSONObject userObj = jsonObject.optJSONObject("user");
                JSONObject fuelStat = jsonObject.optJSONObject("fuelStation");


                //JSONObject jsonObject1 =jsonArray1.getJSONObject(0);
                String count = jsonObject.getString("count");

                //Show the Textview after fetching data
                queueCount.setVisibility(View.VISIBLE);
                //Display data with the Textview
                queueCount.setText(count);
                vehicleType.setText(userObj.getString("vehicleType"));
                vehicleNumber.setText(userObj.getString("vehicleNo"));
                fuelStation.setText(fuelStat.getString("name"));
                joinTime.setText("Join Time: "+jsonObject.getString("arrivalTime"));
                existTime.setText("Departure Time: "+jsonObject.getString("departureTime"));
                fuelArriveTime.setText("Fuel Arrival Time: "+jsonObject.getString("arrivalTime"));
                fuelFinishTime.setText("Fuel Departure Time: "+jsonObject.getString("departureTime"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdateJoinTime extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(FinalPage.this);
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
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
            try {
                JSONObject jsonObject = new JSONObject(s);

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                String date = format.format(new Date());

                jsonObject.put("arrivalTime", date);
                jsonObject.put("status", "JOINED");

                final String urlAddress = "https://fuel-queue-management.herokuapp.com/api/FuelQueue/"+jsonObject.getString("id");
                URL url = new URL(urlAddress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                Log.i("JSON", jsonObject.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonObject.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                conn.disconnect();

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
                }
            });

            thread.start();
        }
    }


    private class UpdateExistTime extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(FinalPage.this);
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
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                        String date = format.format(new Date());

                        jsonObject.put("departureTime", date);
                        jsonObject.put("status", "EXIST");

                        final String urlAddress = "https://fuel-queue-management.herokuapp.com/api/FuelQueue/"+jsonObject.getString("id");
                        URL url = new URL(urlAddress);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("PUT");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept","application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        Log.i("JSON", jsonObject.toString());
                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                        os.writeBytes(jsonObject.toString());

                        os.flush();
                        os.close();

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG" , conn.getResponseMessage());

                        conn.disconnect();

                    } catch (JSONException | MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }
    }

    private class UpdateCompleteTime extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(FinalPage.this);
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
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                        String date = format.format(new Date());

                        jsonObject.put("departureTime", date);
                        jsonObject.put("status", "COMPLETED");

                        final String urlAddress = "https://fuel-queue-management.herokuapp.com/api/FuelQueue/"+jsonObject.getString("id");
                        URL url = new URL(urlAddress);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("PUT");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept","application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        Log.i("JSON", jsonObject.toString());
                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                        os.writeBytes(jsonObject.toString());

                        os.flush();
                        os.close();

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG" , conn.getResponseMessage());

                        conn.disconnect();

                    } catch (JSONException | MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }
    }
}