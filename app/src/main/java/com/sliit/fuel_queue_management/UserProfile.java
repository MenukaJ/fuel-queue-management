package com.sliit.fuel_queue_management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {

    private TextView textViewFirstName, textViewLastName, textViewEmail, textViewContactNo, textViewAddress, textViewNIC, textViewPassword, textViewVehicleNo, textViewVehicleType;
    private ProgressBar progressBar;
    private String FirstName, LastName,Email, ContactNo, Address, NIC, Password, VehicleNo, VehicleType;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Edit Profile");

        textViewFirstName = findViewById(R.id.textView_first_name);
        textViewLastName = findViewById(R.id.textView_last_name);
        textViewEmail = findViewById(R.id.textView_email);
        textViewContactNo = findViewById(R.id.textView_phone);
        textViewAddress = findViewById(R.id.textView_address);
        textViewNIC = findViewById(R.id.textView_nic);
        textViewPassword = findViewById(R.id.textView_password);
        textViewVehicleNo = findViewById(R.id.textView_vehicle_no);
        textViewVehicleNo = findViewById(R.id.textView_vehicle_type);
    }
}