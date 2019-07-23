package com.javac.dell.trackmytime.controllers;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkConnectivity extends AsyncTask<Void,Void,Boolean> {

    private ReturnInternetConn returnInternetConn;
    private boolean success = false;

    public NetworkConnectivity(ReturnInternetConn callback){

        returnInternetConn = callback;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {

        }
        return success;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(!isCancelled())
            returnInternetConn.returnInternetConn(success);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public interface ReturnInternetConn{
        void returnInternetConn(boolean result);
    }
}

