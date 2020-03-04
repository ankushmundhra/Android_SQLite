package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText name,email,password,phone;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mydb = new DatabaseHelper(this);

        name = findViewById(R.id.editText_name);
        email = findViewById(R.id.editText_email);
        password = findViewById(R.id.editText_password);
        phone = findViewById(R.id.editText_mobile_number);

        findViewById(R.id.db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,AndroidDatabaseManager.class));
            }
        });

        AddData();

    }

    public void AddData(){
        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isInserted = mydb.insertData(name.getText().toString(), email.getText().toString()
                        , password.getText().toString(), phone.getText().toString());

                if (isInserted) {
                    Toast.makeText(RegisterActivity.this, "Sign-up Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }

                else {
                    Toast.makeText(RegisterActivity.this,"Data not Inserted",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
