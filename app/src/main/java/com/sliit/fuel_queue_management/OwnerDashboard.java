package com.sliit.fuel_queue_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class OwnerDashboard extends AppCompatActivity {

    private ImageView addAdminBtn, addStudentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);

        addAdminBtn = findViewById(R.id.iconFuel);
        addAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OwnerDashboard.this, FuelDetailsList.class);
                startActivity(intent);
            }
        });

        addStudentBtn = findViewById(R.id.iconStations);
        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OwnerDashboard.this, Login.class);
                startActivity(intent);
            }
        });
    }
}