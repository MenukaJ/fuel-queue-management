package com.sliit.fuel_queue_management.model;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sliit.fuel_queue_management.FuelDetailsList;
import com.sliit.fuel_queue_management.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FuelDetailsAdapter extends RecyclerView.Adapter<FuelDetailsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<FuelDetails> fuelDetails;
    private SwipeRefreshLayout refresh;
    private EditText fuelType, fuelArrival, fuelFinished, fuelStatus;
    private TextView fuelStationName, fuelDate;
    private FuelStation fuelStationClass = new FuelStation();
    private String url = "https://fuel-queue-management.herokuapp.com/api/FuelDetails";

    public FuelDetailsAdapter(Context context, ArrayList<FuelDetails> fuelDetails) {
        this.context = context;
        this.fuelDetails = fuelDetails;
    }

    @NonNull
    @Override
    public FuelDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.activitiy_fuel_details_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FuelDetailsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.no.setText("#" + String.valueOf(position+1));
        holder.name.setText(fuelDetails.get(position).getType());
        holder.date.setText(fuelDetails.get(position).getDate());
        holder.editFuelDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = fuelDetails.get(position).getId();
                String type = fuelDetails.get(position).getType();
                String arrivalTime = fuelDetails.get(position).getArrivalTime();
                String finishTime = fuelDetails.get(position).getFinishTime();
                String date = fuelDetails.get(position).getDate();
                String status = fuelDetails.get(position).getStatus();
                String stationName = fuelDetails.get(position).getFuelStation().getName();
                fuelStationClass = fuelDetails.get(position).getFuelStation();
                editFuelDetails(id, type, arrivalTime, finishTime, date, status, stationName, fuelStationClass);
            }
        });
        holder.deleteFuelDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = fuelDetails.get(position).getId();
                deleteFuelDetails(id);
            }
        });
    }

    private void editFuelDetails(String id, String pType, String pArrival, String pFinish, String pDate, String pStatus, String pStation, FuelStation pFuelStation) {
        TextView close, header;
        Button submit;
        final Dialog dialog;

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_modify_fuel_details);

        close = (TextView) dialog.findViewById(R.id.txtClose);
        header = (TextView) dialog.findViewById(R.id.modFuel);
        header.setText("Update Details");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        fuelType = (EditText) dialog.findViewById(R.id.edFuelType);
        fuelType.setText(pType);
        fuelArrival = (EditText) dialog.findViewById(R.id.edFuelArrival);
        fuelArrival.setText(pArrival);
        fuelFinished = (EditText) dialog.findViewById(R.id.edFuelFinish);
        fuelFinished.setText(pFinish);
        fuelDate = (TextView) dialog.findViewById(R.id.edFuelDate);
        fuelDate.setText(pDate);
        fuelStatus = (EditText) dialog.findViewById(R.id.edFuelStatus);
        fuelStatus.setText(pStatus);
        fuelStationName = (TextView) dialog.findViewById(R.id.edFuelStation);
        fuelStationName.setText(pStation);

        submit = (Button) dialog.findViewById(R.id.edBtnFuelDet);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fuelType.getText().toString().isEmpty()) {
                    Toast.makeText(context.getApplicationContext(), "Fuel Type is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fuelArrival.getText().toString().isEmpty()) {
                    Toast.makeText(context.getApplicationContext(), "Arrived time is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fuelArrival.getText().toString().matches("^$|(\\d{2}):(\\d{2})$")) {
                    Toast.makeText(context.getApplicationContext(), "Arrived time should be in HH:mm pattern.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fuelFinished.getText().toString().isEmpty()) {
                    Toast.makeText(context.getApplicationContext(), "Finished time is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fuelFinished.getText().toString().matches("^$|(\\d{2}):(\\d{2})$")) {
                    Toast.makeText(context.getApplicationContext(), "Finished time should be in HH:mm pattern.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fuelDate.getText().toString().isEmpty()) {
                    Toast.makeText(context.getApplicationContext(), "Date is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fuelStatus.getText().toString().isEmpty()) {
                    Toast.makeText(context.getApplicationContext(), "Status is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fuelStatus.getText().toString().matches("^$|Available|Finished")) {
                    Toast.makeText(context.getApplicationContext(), "Fuel status is should be Available or Finished.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fuelStationName.getText().toString().isEmpty()) {
                    Toast.makeText(context.getApplicationContext(), "Station name is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateData(fuelType.getText().toString(), fuelArrival.getText().toString(), fuelFinished.getText().toString(), fuelDate.getText().toString(), fuelStatus.getText().toString(), pFuelStation, dialog, id);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void deleteFuelDetails(final String id) {
        TextView close, header;
        Button submit;
        final Dialog dialog;

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_delete_fuel_details);

        close = (TextView) dialog.findViewById(R.id.txtClose);
        header = (TextView) dialog.findViewById(R.id.modFuel);
        header.setText("Delete Details");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        submit = (Button) dialog.findViewById(R.id.edBtnFuelDet);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData(dialog, id);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void updateData(String pFuelType, String pFuelArrival, String pFuelFinished, String pFuelDate, String pFuelStatus, FuelStation fuelStationObject, Dialog dialog, String id) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", id);
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

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url + "/" + id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    dialog.dismiss();
                    /*refresh.post(new Runnable() {
                        @Override
                        public void run() {
                            fuelDetails.clear();
                            //getData();
                        }
                    });*/
                    Toast.makeText(context.getApplicationContext(), "Record updated successfully", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("VOLLEY", error.toString());
                    Toast.makeText(context.getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
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

    private void deleteData(final Dialog dialog, final String id) {
        StringRequest request = new StringRequest(Request.Method.DELETE, url + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                refresh.post(new Runnable() {
                    @Override
                    public void run() {
                        fuelDetails.clear();
                    }
                });
                Toast.makeText(context, "Record deleted successfully", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error occurred", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public int getItemCount() {
        return fuelDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView no, name, date;
        private ImageView editFuelDet, deleteFuelDet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            no = (TextView) itemView.findViewById(R.id.no);
            name = (TextView) itemView.findViewById(R.id.nameFuel);
            date = (TextView) itemView.findViewById(R.id.dateFuel);
            editFuelDet = (ImageView) itemView.findViewById(R.id.editFuel);
            deleteFuelDet = (ImageView) itemView.findViewById(R.id.deleteFuel);
        }
    }
}
