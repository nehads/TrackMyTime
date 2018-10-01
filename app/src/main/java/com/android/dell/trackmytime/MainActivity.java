package com.android.dell.trackmytime;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "MainActivity";
    private Button myTimeIn, myTimeOut;
    DatabaseReference myDatabaseRef;

    private boolean checkedBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference("schedule");
        myTimeIn = findViewById(R.id.my_time_in);
        myTimeOut = findViewById(R.id.my_time_out);

        myTimeIn.setOnClickListener(this);
        myTimeOut.setOnClickListener(this);

        SharedPreferences prefs = this.getSharedPreferences("TrackTimeCheckIn", MODE_PRIVATE);

        checkedBool = (prefs.getBoolean("checkedIn", false));
        if(checkedBool) {
            myTimeIn.setVisibility(View.GONE);
            myTimeOut.setVisibility(View.VISIBLE);
        }
        else{
            myTimeIn.setVisibility(View.VISIBLE);
            myTimeOut.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        if(v.equals(myTimeIn)){
            try{

                Log.d(TAG,"timeIn: "+getTime());
                myDatabaseRef.child(getDate()).child("CheckIn").setValue(getTime());
                myTimeIn.setVisibility(View.GONE);
                myTimeOut.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Great Start !! Focus learn foccusss", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = this.getSharedPreferences("TrackTimeCheckIn", MODE_PRIVATE).edit();
                editor.putBoolean("checkedIn", true);
                editor.apply();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else if(v.equals(myTimeOut)){

            try{

                Log.d(TAG,"timeOut: "+getTime());
                myDatabaseRef.child(getDate()).child("CheckOut").setValue(getTime());
                myTimeOut.setVisibility(View.GONE);
                myTimeIn.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Wooohooo! Time to go homeee..", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = this.getSharedPreferences("TrackTimeCheckIn", MODE_PRIVATE).edit();
                editor.clear().apply();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getTime() {
        return java.text.DateFormat.getTimeInstance().format(new Date());
    }
}
