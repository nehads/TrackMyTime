package com.javac.dell.trackmytime.views;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.javac.dell.trackmytime.Constants;
import com.javac.dell.trackmytime.R;
import com.javac.dell.trackmytime.adapters.RecyclerAdapter;
import com.javac.dell.trackmytime.controllers.NetworkConnectivity;
import com.javac.dell.trackmytime.controllers.StoredSharedPreferences;
import com.javac.dell.trackmytime.model.TimeRecords;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity implements GetRecords.SendFirebaseResult, NetworkConnectivity.ReturnInternetConn {

    DatabaseReference myDatabaseRef;
    private RecyclerView recyclerView;
    private ArrayList<TimeRecords> timeRecordsList = new ArrayList<>();
    private static String TAG = "DisplayActivity";
    ProgressBar progressBar;
    private TextView noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        noData = findViewById(R.id.no_data);

        StoredSharedPreferences.GetSharedPrefs prefs  = StoredSharedPreferences.GetSharedPrefValues(this);
        Log.e(TAG," name  "+prefs.getLoggedInName());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference(Constants.FIREBASE_DB_NAME).child(prefs.getLoggedInName());

        checkInternetWorking();

        recyclerView = findViewById(R.id.recycler_view);
    }

    private void checkInternetWorking() {
        NetworkConnectivity isInternetWorking = new NetworkConnectivity(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            isInternetWorking.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            isInternetWorking.execute();
    }


    @Override
    public void sendFirebaseResult(ArrayList<String> dates, ArrayList<String> checkInTimings, ArrayList<String> checkOutTimings) {

        Log.e(TAG, "date "+dates);
        Log.e(TAG, "In "+checkInTimings);
        Log.e(TAG, "Out "+checkOutTimings);
        try {

            progressBar.setVisibility(View.GONE);

            for (int i = 0; i < dates.size(); i++) {

                TimeRecords timeRecords = new TimeRecords(dates.get(i), checkInTimings.get(i), checkOutTimings.get(i));

                timeRecordsList.add(timeRecords);

                RecyclerAdapter mAdapter_clubs = new RecyclerAdapter(timeRecordsList);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter_clubs);

            }

            if(dates.isEmpty())
                noData.setVisibility(View.VISIBLE);


        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    @Override
    public void returnInternetConn(boolean result) {
        if(result){
            GetRecords getRecords = new GetRecords();
            getRecords.getRecords(myDatabaseRef, this);
        }
        else{
            progressBar.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            noData.setText(R.string.no_internet);
            findViewById(R.id.no_net_image).setVisibility(View.VISIBLE);
        }
    }
}
