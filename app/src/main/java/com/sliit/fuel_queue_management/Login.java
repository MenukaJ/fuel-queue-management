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

/**
 * The type Login.
 */
public class Login extends AppCompatActivity {

    EditText email, password;
    String role = null;
    Button btnSubmit;
    TextView createAcc;
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
        getSupportActionBar().hide();
        Boolean e=false,p=false;
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.text_email);
        password=findViewById(R.id.text_pass);
        btnSubmit = findViewById(R.id.btnSubmit_login);
        dbHelper = new DBHelper(this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailCheck = email.getText().toString();
                String passCheck = password.getText().toString();
                //String roleCheck = role.getText().toString();
                Cursor  cursor = dbHelper.getData();
                if(cursor.getCount() == 0){
                    Toast.makeText(Login.this,"No entries Exists",Toast.LENGTH_LONG).show();
                }
                if (loginCheck(cursor,emailCheck,passCheck)) {
                    if (role.equalsIgnoreCase("OWNER")) {
                        Intent intent = new Intent(Login.this,FuelDetailsList.class);
                        intent.putExtra("email",emailCheck);
                        email.setText("");
                        password.setText("");
                        startActivity(intent);
                    } else if (role.equalsIgnoreCase("CUSTOMER"))  {
                        Intent intent = new Intent(Login.this,FinalPage.class);
                        intent.putExtra("email",emailCheck);
                        email.setText("");
                        password.setText("");
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

    /**
     * Login check boolean.
     *
     * @param cursor     the cursor
     * @param emailCheck the email check
     * @param passCheck  the pass check
     * @return the boolean
     */
    public boolean loginCheck(Cursor cursor, String emailCheck, String passCheck) {
        while (cursor.moveToNext()){
            if (cursor.getString(0).equals(emailCheck)) {
                if (cursor.getString(2).equals(passCheck)) {
                    setRole(cursor.getString(4));
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}