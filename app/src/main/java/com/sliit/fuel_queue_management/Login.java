package com.sliit.fuel_queue_management;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.fuel_queue_management.Sql.DBHelper;

public class Login extends AppCompatActivity {

    EditText email, password, role;
    Button btnSubmit;
    TextView createAcc;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        Boolean e=false,p=false;
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.text_email);
        password=findViewById(R.id.text_pass);
        role=findViewById(R.id.text_role);
        btnSubmit = findViewById(R.id.btnSubmit_login);
        dbHelper = new DBHelper(this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailCheck = email.getText().toString();
                String passCheck = password.getText().toString();
                String roleCheck = role.getText().toString();
                Cursor  cursor = dbHelper.getData();
                if(cursor.getCount() == 0){
                    Toast.makeText(Login.this,"No entries Exists",Toast.LENGTH_LONG).show();
                }
                if (loginCheck(cursor,emailCheck,passCheck)) {
                    if (roleCheck.equalsIgnoreCase("OWNER")) {
                        Intent intent = new Intent(Login.this,FuelDetailsList.class);
                        intent.putExtra("email",emailCheck);
                        email.setText("");
                        password.setText("");
                        role.setText("");
                        startActivity(intent);
                    } else if (roleCheck.equalsIgnoreCase("CUSTOMER"))  {
                        Intent intent = new Intent(Login.this,FinalPage.class);
                        intent.putExtra("email",emailCheck);
                        email.setText("");
                        password.setText("");
                        role.setText("");
                        startActivity(intent);
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setCancelable(true);
                    builder.setTitle("Wrong Credential");
                    builder.setMessage("Wrong Credential");
                    builder.show();
                }
                dbHelper.close();
            }
        });
        createAcc=findViewById(R.id.createAcc);
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });
    }
    public static boolean loginCheck(Cursor cursor,String emailCheck,String passCheck) {
        while (cursor.moveToNext()){
            if (cursor.getString(0).equals(emailCheck)) {
                if (cursor.getString(2).equals(passCheck)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}