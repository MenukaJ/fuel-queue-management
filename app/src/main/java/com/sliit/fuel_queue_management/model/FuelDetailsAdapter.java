package com.sliit.fuel_queue_management.model;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sliit.fuel_queue_management.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FuelDetailsAdapter extends RecyclerView.Adapter<FuelDetailsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<FuelDetails> fuelDetails;
    private SwipeRefreshLayout refresh;
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
                editFuelDetails(id, type, arrivalTime, finishTime, date, status, stationName);
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

    private void editFuelDetails(String id, String pType, String pArrival, String pFinish, String pDate, String pStatus, String pStation) {
        TextView close, header;
        final EditText fuelType, fuelArrival, fuelFinished, fuelStatus;
        final TextView fuelStationName, fuelDate;
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
                String data = fuelType.getText().toString();
                Submit("PUT", data, dialog, id);
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
                Submit("DELETE", "", dialog, id);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void Submit(String method, final String data, final Dialog dialog, final String id) {
        if (method == "PUT") {
            StringRequest request = new StringRequest(Request.Method.PUT, url + "/" + id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Toast.makeText(context, "Record updated successfully", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error occurred", Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("cat", data);
                    params.put("id", id);
                    return params;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else if (method == "DELETE") {
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
