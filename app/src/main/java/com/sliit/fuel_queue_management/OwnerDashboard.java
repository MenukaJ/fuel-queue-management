package com.sliit.fuel_queue_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class OwnerDashboard extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
{

    private ImageView addAdminBtn, addStudentBtn;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);

        drawerLayout = findViewById(R.id.owner_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setTitle("Dashboard");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A443E9")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.owner_nav);

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
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
            Intent intent = new Intent(OwnerDashboard.this, Login.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.nav_account) {
            Intent intent = new Intent(OwnerDashboard.this,UserProfile.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}