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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sliit.fuel_queue_management.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FuelStationAdapter extends RecyclerView.Adapter<FuelStationAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<FuelStation> fuelStation;
    private String url = "https://fuel-queue-management.herokuapp.com/api/FuelStation";

    public FuelStationAdapter(Context context, ArrayList<FuelStation> fuelStation) {
        this.context = context;
        this.fuelStation = fuelStation;
    }

    @NonNull
    @Override
    public FuelStationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.activity_fuel_stations_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FuelStationAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.name.setText(fuelStation.get(position).getName());
        holder.no.setText("#" + String.valueOf(position+1));
        holder.viewFuelStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = fuelStation.get(position).getId();
                String name = fuelStation.get(position).getName();
                viewFuelStation(id, name);
            }
        });
    }

    private void viewFuelStation(String id, String pName) {
        TextView close, name;
        final TextView stationName;
        final Dialog dialog;

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_view_fuel_station);

        close = (TextView) dialog.findViewById(R.id.txtClose);
        name = (TextView) dialog.findViewById(R.id.modStation);
        name.setText("Fuel Station Details");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        stationName = (TextView) dialog.findViewById(R.id.edStationName);
        stationName.setText(pName + " Station");

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return fuelStation.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, no;
        private ImageView viewFuelStat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            no = (TextView) itemView.findViewById(R.id.no);
            name = (TextView) itemView.findViewById(R.id.nameStation);
            viewFuelStat = (ImageView) itemView.findViewById(R.id.viewStation);
        }
    }
}