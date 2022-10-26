package com.sliit.fuel_queue_management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.sliit.fuel_queue_management.Sql.DBHelper;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The Login.
     */
    Button login, /**
     * The Reg.
     */
    Reg;
    /**
     * The Toolbar.
     */
    Toolbar toolbar;
    /**
     * The Db helper.
     */
    DBHelper dbHelper;

    @Override
    public void onBackPressed() {
        MainActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        login = (Button) findViewById(R.id.btnLogin);
        toolbar = (Toolbar) findViewById(R.id.tool_main);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
        Reg = findViewById(R.id.btnSignUp);
        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

    }
}