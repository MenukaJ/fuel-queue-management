package com.sliit.fuel_queue_management;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.fuel_queue_management.Sql.DBHelper;

/**
 * The type Sign up.
 */
public class SignUp extends AppCompatActivity {


    EditText name , number , email, pass;
    TextView login;
    String role;

    DBHelper dbHelper;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        name=findViewById(R.id.textName);
        number=findViewById(R.id.textNumber);
        email=findViewById(R.id.textEmail);
        pass=findViewById(R.id.textPass);

        Button signUpAcc = findViewById(R.id.btnSignUpAcc);

        Spinner staticSpinner = (Spinner) findViewById(R.id.roles);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //String role;
                if (position == 0)
                    setRole("CUSTOMER");
                else if (position == 1)
                    setRole("OWNER");
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        dbHelper = new DBHelper(this);
        signUpAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = name.getText().toString();
                String number1 = number.getText().toString();
                String email1 = email.getText().toString();
                String pass1 = pass.getText().toString();

                if (name.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Name is required.", Toast.LENGTH_SHORT).show();
                    return;
                } if (number.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Phone number is required.", Toast.LENGTH_SHORT).show();
                    return;
                } if (!number.getText().toString().matches("^[0-9]{10}$")) {
                    Toast.makeText(SignUp.this, "Phone Number should be 10 Digit.", Toast.LENGTH_SHORT).show();
                    return;
                } if (email.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Email is required.", Toast.LENGTH_SHORT).show();
                    return;
                } if (!email.getText().toString().matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) {
                    Toast.makeText(SignUp.this, "Email should be in example@gmail.com ", Toast.LENGTH_SHORT).show();
                    return;
                } if (pass.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Password time is required.", Toast.LENGTH_SHORT).show();
                    return;
                } if (!pass.getText().toString().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
                    Toast.makeText(SignUp.this, "Password must contain [0-9], [a-z], [A-Z], ! @ # & ( ) ", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean b =dbHelper.insetUserData(name1,number1,email1,pass1, getRole());
                if (b){
                    Toast.makeText(SignUp.this,"Data inserted",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUp.this,Login.class);
                    startActivity(i);
                }else {
                    Toast.makeText(SignUp.this,"Failed To insert Data",Toast.LENGTH_SHORT).show();
                }
            }
        });
        login=findViewById(R.id.loginAcc);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this,Login.class);
                startActivity(i);
            }
        });
    }

}