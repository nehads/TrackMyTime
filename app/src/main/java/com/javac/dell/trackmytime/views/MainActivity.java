package com.javac.dell.trackmytime.views;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javac.dell.trackmytime.Constants;
import com.javac.dell.trackmytime.R;
import com.javac.dell.trackmytime.controllers.NetworkConnectivity;
import com.javac.dell.trackmytime.controllers.StoredSharedPreferences;

import static com.javac.dell.trackmytime.controllers.DateTime.getDate;
import static com.javac.dell.trackmytime.controllers.DateTime.getTime;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NetworkConnectivity.ReturnInternetConn {

    private static String TAG = "MainActivity";
    private Button myTimeIn, myTimeOut;
    DatabaseReference myDatabaseRef;
    private Button seeRecords;

    private RelativeLayout firstTimeLay;
    private RelativeLayout nextLay;
    private Button done;
    private EditText name;

    private Button logOut;
    private boolean checkedBool, loggedInBool = false;
    private String nameInput;

    private int count = 0;

    private TextView disclaimer;

    StoredSharedPreferences.GetSharedPrefs prefs;
    StoredSharedPreferences.SetSharedPrefValues setPrefs;

    private TextView loggedInNameTv;

    private String pendingCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs  = StoredSharedPreferences.GetSharedPrefValues(this);
        setPrefs  = new StoredSharedPreferences.SetSharedPrefValues();

        firstTimeLay = findViewById(R.id.first_time_layout);
        nextLay = findViewById(R.id.every_time_layout);
        name = findViewById(R.id.enter_name);

        done = findViewById(R.id.done);

        done.setOnClickListener(this);

        logOut = findViewById(R.id.log_out);

        logOut.setOnClickListener(this);

        disclaimer = findViewById(R.id.disclaimer);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference(Constants.FIREBASE_DB_NAME);
        myTimeIn = findViewById(R.id.my_time_in);
        myTimeOut = findViewById(R.id.my_time_out);

        seeRecords = findViewById(R.id.see_records);

        myTimeIn.setOnClickListener(this);
        myTimeOut.setOnClickListener(this);

        seeRecords.setOnClickListener(this);

        loggedInBool = prefs.isLoggedIn();
        checkedBool = prefs.isCheckedIn();
        nameInput = prefs.getLoggedInName();

        loggedInNameTv = findViewById(R.id.logged_in_name);

        if(checkedBool) {
            myTimeIn.setVisibility(View.GONE);
            myTimeOut.setVisibility(View.VISIBLE);
        }
        else{
            myTimeIn.setVisibility(View.VISIBLE);
            myTimeOut.setVisibility(View.GONE);
        }

        if(loggedInBool){
            firstTimeLay.setVisibility(View.GONE);
            nextLay.setVisibility(View.VISIBLE);

            loggedInNameTv.setText(nameInput);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.equals(myTimeIn)){
            try{

                pendingCall = Constants.CHECK_IN_PENDING;

                checkInternetWorking();


            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else if(v.equals(myTimeOut)){

            try{
                pendingCall = Constants.CHECK_OUT_PENDING;

                checkInternetWorking();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        else if(v.equals(seeRecords)){
            openRecords();
        }

        else if(v.equals(done)){

            String nameInput = name.getText().toString();
            if(TextUtils.isEmpty(nameInput.trim()))
                name.setError("Required");

            else if(nameInput.length() < 10)
                disclaimer.setTextColor(getResources().getColor(R.color.disclaimer));

            else{
                this.nameInput = nameInput;
                pendingCall = Constants.LOG_IN_PENDING;
                checkInternetWorking();

            }
        }

        else if(v.equals(logOut)){
            count++;

            if(count == 1){
                logOut.setBackgroundResource(R.drawable.custom_btn);

                Snackbar snackbar = Snackbar.make(nextLay, R.string.snack_bar_text, Snackbar.LENGTH_LONG);

                TextView tv = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/Lato-Regular.ttf");
                tv.setTypeface(font);

                snackbar.show();
            }
            else{
                StoredSharedPreferences.ClearStoredSharedPrefs(this);
                StoredSharedPreferences.ClearStoredSharedPrefsLogGedIn(this);
                logOut.setBackgroundResource(R.drawable.custom_btn_white);
                myTimeOut.setVisibility(View.GONE);
                myTimeIn.setVisibility(View.VISIBLE);
                firstTimeLay.setVisibility(View.VISIBLE);
                name.setText("");
                nameInput = "";
                nextLay.setVisibility(View.GONE);
                StoredSharedPreferences.GetSharedPrefs prefs1 = StoredSharedPreferences.GetSharedPrefValues(this);
                Log.e(TAG, "logout "+prefs1.getLoggedInName());
                count = 0;
            }
        }
    }

    private void checkIfValueExists(final String nameInput) {

        myDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //logInFunc(false, nameInput);
                boolean execute = false;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Log.e(TAG, "Outside loop  "+data.getKey());

                    if (data.getKey().equalsIgnoreCase(nameInput)) {

                        execute = true;
                        logInFunc(true, nameInput);
                    }

                }
                if(!execute)
                    logInFunc(false, nameInput);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    private void checkInternetWorking() {
        NetworkConnectivity isInternetWorking = new NetworkConnectivity(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            isInternetWorking.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            isInternetWorking.execute();
    }

    private void logInFunc(boolean result, String nameInput) {
        if(!result)
            myDatabaseRef.child(nameInput).setValue(nameInput);

        Toast.makeText(this, R.string.welcome_msg, Toast.LENGTH_LONG).show();

        setPrefs.SetLoggedInPrefs(this, nameInput);

        firstTimeLay.setVisibility(View.GONE);
        nextLay.setVisibility(View.VISIBLE);

        loggedInNameTv.setText(nameInput);

        StoredSharedPreferences.GetSharedPrefs prefs1 = StoredSharedPreferences.GetSharedPrefValues(this);
        Log.e(TAG, "done func "+prefs1.getLoggedInName());

    }

    private void openRecords() {
        Intent intent = new Intent(this, DisplayActivity.class);
        startActivity(intent);
    }


    @Override
    public void returnInternetConn(boolean result) {
        if(result){
            switch (pendingCall){
                case Constants.CHECK_IN_PENDING:
                    checkInFunc();
                    break;

                case Constants.CHECK_OUT_PENDING:
                    checkOutFunc();
                    break;

                case Constants.LOG_IN_PENDING:
                    checkIfValueExists(nameInput);
                    break;
            }

            pendingCall = "";
        }

        else{
            Snackbar snackbar = Snackbar.make(nextLay, R.string.no_internet, Snackbar.LENGTH_LONG);

            TextView tv = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
            Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/Lato-Regular.ttf");
            tv.setTypeface(font);

            snackbar.show();
        }
    }

    private void checkInFunc() {
        if(nameInput.trim().isEmpty()) {
            StoredSharedPreferences.GetSharedPrefs newPrefs = StoredSharedPreferences.GetSharedPrefValues(this);
            nameInput = newPrefs.getLoggedInName();
        }
        Log.e(TAG,"name: "+nameInput);
        myDatabaseRef.child(nameInput).child(getDate()).child(Constants.FIREBASE_CHILD_CHECK_IN).setValue(getTime());
        myTimeIn.setVisibility(View.GONE);
        myTimeOut.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Great Start !! Focus learn foccusss", Toast.LENGTH_LONG).show();

        setPrefs.SetCheckedInPrefs(this);
    }

    private void checkOutFunc() {

        myDatabaseRef.child(nameInput).child(getDate()).child(Constants.FIREBASE_CHILD_CHECK_OUT).setValue(getTime());
        myTimeOut.setVisibility(View.GONE);
        myTimeIn.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Wooohooo! Time to go homeee..", Toast.LENGTH_LONG).show();
        StoredSharedPreferences.ClearStoredSharedPrefs(this);
    }
}
