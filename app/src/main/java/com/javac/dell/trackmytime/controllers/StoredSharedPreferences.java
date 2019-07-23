package com.javac.dell.trackmytime.controllers;

import android.app.Activity;
import android.content.SharedPreferences;

import com.javac.dell.trackmytime.Constants;

import static android.content.Context.MODE_PRIVATE;

public class StoredSharedPreferences {

    public static class GetSharedPrefs{

        private boolean checkedIn, loggedIn;
        private String loggedInName;

        public GetSharedPrefs(boolean loggedIn, boolean checkedIn, String loggedInName){
            this.loggedIn = loggedIn;
            this.checkedIn = checkedIn;
            this.loggedInName = loggedInName;
        }

        public String getLoggedInName() {
            return loggedInName;
        }

        public void setLoggedInName(String loggedInName) {
            this.loggedInName = loggedInName;
        }

        public boolean isCheckedIn() {
            return checkedIn;
        }

        public void setCheckedIn(boolean checkedIn) {
            this.checkedIn = checkedIn;
        }

        public void setLoggedIn(boolean loggedIn) {
            this.loggedIn = loggedIn;
        }

        public boolean isLoggedIn() {
            return loggedIn;
        }
    }

    public static GetSharedPrefs GetSharedPrefValues(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences(Constants.CHECKED_IN_PREF_LABEL, MODE_PRIVATE);
        SharedPreferences logPrefs = activity.getSharedPreferences(Constants.LOGGED_IN_PREF_LABEL, MODE_PRIVATE);

        String nameInput = (logPrefs.getString(Constants.LOGGED_IN_NAME, ""));
        boolean checkedBool = (prefs.getBoolean(Constants.CHECKED_IN_BOOL_NAME, false));
        boolean loggedInBool = (logPrefs.getBoolean(Constants.LOGGED_IN_BOOL_NAME, false));

        return new GetSharedPrefs(loggedInBool, checkedBool, nameInput);
    }

    public static class SetSharedPrefValues{

        public void SetLoggedInPrefs(Activity activity, String name){
            SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.LOGGED_IN_PREF_LABEL, MODE_PRIVATE).edit();
                editor.putBoolean(Constants.LOGGED_IN_BOOL_NAME, true);
                editor.putString(Constants.LOGGED_IN_NAME, name);
                editor.apply();
               // editor.commit();
        }

        public void SetCheckedInPrefs(Activity activity){
            SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.CHECKED_IN_PREF_LABEL, MODE_PRIVATE).edit();
                editor.putBoolean(Constants.CHECKED_IN_BOOL_NAME, true);
                editor.apply();
             //   editor.commit();
        }
    }

    public static void ClearStoredSharedPrefs(Activity activity){
        SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.CHECKED_IN_PREF_LABEL, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public static void ClearStoredSharedPrefsLogGedIn(Activity activity){
        SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.LOGGED_IN_PREF_LABEL, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
