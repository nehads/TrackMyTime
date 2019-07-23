package com.javac.dell.trackmytime.views;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

class GetRecords {

    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> checkInTimings = new ArrayList<>();
    private ArrayList<String> checkOutTimings = new ArrayList<>();


    void getRecords(DatabaseReference myDatabaseRef, final SendFirebaseResult sendFirebaseResult){

        myDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    dates.add(postSnapshot.getKey());

                    for(DataSnapshot childSnapShot : postSnapshot.getChildren()){
                        String date = (String) childSnapShot.getKey();
                        i++;
                        Log.e("GetRed"," qqq "+date);

                        if(i == 1)
                            checkInTimings.add((String) childSnapShot.getValue());
                        else{
                            checkOutTimings.add((String) childSnapShot.getValue());
                        }

                        Log.e("GetRed"," "+date);
                    }

                    if(i == 1)
                        checkOutTimings.add("---");
                    i = 0;
                }

                sendFirebaseResult.sendFirebaseResult(dates, checkInTimings, checkOutTimings);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface SendFirebaseResult{

        void sendFirebaseResult(ArrayList<String> dates, ArrayList<String> checkInTimings, ArrayList<String> checkOutTimings);
    }
}
