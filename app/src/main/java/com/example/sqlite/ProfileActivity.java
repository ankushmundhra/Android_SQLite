package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;

public class ProfileActivity extends AppCompatActivity {

    EditText name,password,email,mob;
    String usrname ;
    String usremail;
    String usrpass;
    String usrmob;
    DatabaseHelper mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.editText_name);
        password = findViewById(R.id.editText_password);
        email = findViewById(R.id.editText_email);
        mob = findViewById(R.id.editText_mobile_number);

        //get data of sharedPreference
        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);
        String s1 = sh.getString("uname_email", "");

        mydb = new DatabaseHelper(this);
        Cursor res = mydb.getCurrentData(s1);
        if (res.getCount() == 0) {
            //showMessage("Error", "Nothing Found");
            return;
        }


        while(res.moveToNext()){
            usrname = res.getString(res.getColumnIndex("NAME"));
            usremail = res.getString(res.getColumnIndex("EMAIL"));
            usrpass = res.getString(res.getColumnIndex("PASSWORD"));
            usrmob = res.getString(res.getColumnIndex("MOBILE_NUMBER"));
        }

        name.setText(usrname);
        password.setText(usrpass);
        email.setText(usremail);
        mob.setText(usrmob);

        findViewById(R.id.btnupdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = name.getText().toString();
                String pass1 = password.getText().toString();
                String email1 = email.getText().toString();
                String mob1 = mob.getText().toString();

                //mydb.updateProfile(name1,pass1,email1,mob1);

                SQLiteDatabase db = mydb.getWritableDatabase();
                //db.execSQL("update registration_table set EMAIL="+"'"+email1+"'"+ " where NAME="+"'"+name1+"'");
                db.execSQL("update registration_table set NAME="+"'"+name1+"',"+"EMAIL="+"'"+email1+"'"+ " where NAME="+"'"+name1+"'");

                View contextView = findViewById(R.id.context_view);
                Snackbar.make(contextView, "Details Updated", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}
