package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TintableImageSourceView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    SQLiteDatabase mDatabase;
    public static final String DATABASE_NAME = "Registration.db";
    SharedPreferences sharedPreferences;

    //Notification
    NotificationManager notificationManager;
    String channelId = "some_channel_id";
    CharSequence channelName = "Some Channel";
    int notifyId = 1;
    Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences  = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        findViewById(R.id.btRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdetails();
            }
        });

    }

    public void checkdetails(){
        DatabaseHelper mydb = new DatabaseHelper(this);

        Cursor res = mydb.getAllData();
        if (res.getCount() == 0) {
            //showMessage("Error", "Nothing Found");
            return;
        }

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // here usr_given_userName can be username as well as email
        String usr_given_userName = email.getText().toString().trim();
        String user_given_passWord = password.getText().toString().trim();

        //intent to pass username or email to profile activity.
        //SharedPreferences.Editor myEdit1 = sharedPreferences.edit();
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("uname_email", usr_given_userName);
//        myEdit1.commit();

        String[] array_name = new String[res.getCount()]; //db user
        String[] array_pass = new String[res.getCount()]; //db pass
        String[] array_email = new String[res.getCount()]; //db email

        int i = 0;
        while(res.moveToNext()){
            String uname = res.getString(res.getColumnIndex("NAME"));
            String pass = res.getString(res.getColumnIndex("PASSWORD"));
            String mail = res.getString(res.getColumnIndex("EMAIL"));
            array_name[i] = uname;
            array_pass[i] = pass;
            array_email[i] = mail;
            i++;
        }

        for(i = 0; i < res.getCount(); i++)
        {
            if (usr_given_userName.equals(array_name[i]) || usr_given_userName.equals(array_email[i])){
                if (user_given_passWord.equals(array_pass[i])) {

                    //using sharedPreference to save name and email
                    myEdit.putString("uname", array_name[i]);
                    myEdit.putString("email", array_email[i]);
                    myEdit.commit();

                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Main3Activity.class));
                }
                else
                    Toast.makeText(getApplicationContext(),"Wrong Password!",Toast.LENGTH_SHORT).show();
            }
        }

        //notification starts here
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
//        notificationChannel.setSound(null,null);
        notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(notificationChannel);

        String groupId = "some_group_id";
        CharSequence groupName = "Some Group";
        notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(groupId, groupName));

        notification = new Notification.Builder(MainActivity.this,channelId)
                .setContentTitle("Uploading...")
                .setContentText("is being uploaded!")
                .setSmallIcon(R.mipmap.bell)
                .build();

        notificationManager.notify(notifyId, notification);

    }

}
