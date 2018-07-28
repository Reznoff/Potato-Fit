package com.unpas.potatosoft.potatofit.connection;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ricec on 16/03/18.
 */

public class SessionManager {

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    public static String SESSION = "Session";
    public static String internetProtocol = "192.168.1.168";

    public static void createSignInSession(Context context, String status) {
        preferences = context.getSharedPreferences(SESSION, 0);
        editor = preferences.edit();
        editor.putString("status", status);
        editor.commit();
    }

    public static void logout(Context context) {
        preferences = context.getSharedPreferences(SESSION, 0);
        editor = preferences.edit();
        editor.clear();
        editor.commit();
    }




//
//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;
//
//    // Context
//    Context context;
//
//
//
//    public static final String PREF_NAME = "UserData";
//
//    // All Shared Preferences Keys
//    public static final String IS_LOGIN = "IsLoggedIn";
//
//    public static final String KEY_NAME = "username";
//
//    public static final String KEY_PASS = "password";
//
//    // Constructor
//    public SessionManager(Context context){
//        this.context = context;
//        preferences = context.getSharedPreferences(PREF_NAME, 0);
//        editor = preferences.edit();
//    }
//
//    /**
//     * Create login session
//     * */
//    public void createLoginSession(String name, String pass){
//        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        editor = preferences.edit();
//
//        // Storing login value as TRUE
//        editor.putBoolean(IS_LOGIN, true);
//
//        // Storing name in pref
//        editor.putString(KEY_NAME, name);
//
//        // Storing pass in pref
//        editor.putString(KEY_PASS, pass);
//
//        // commit changes
//        editor.commit();
//    }
//
//    /**
//     * Check login method wil check user login status
//     * If false it will redirect user to login page
//     * Else won't do anything
//     * */
//    public void checkLogin(){
//        // Check login status
//        if(!this.isLoggedIn()){
//            // user is not logged in redirect him to Login Activity
//            Intent i = new Intent(context, LoginActivity.class);
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//            context.startActivity(i);
//        }
//    }
//
//    /**
//     * Get stored session data
//     * */
//    public HashMap<String, String> getUserDetails(){
//        HashMap<String, String> user = new HashMap<String, String>();
//        // user name
//        user.put(KEY_NAME, preferences.getString(KEY_NAME, null));
//
//        // user pass id
//        user.put(KEY_PASS, preferences.getString(KEY_PASS, null));
//
//        // return user
//        return user;
//    }
//
//    /**
//     * Clear session details
//     * */
//    public void logoutUser(){
//        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        // Clearing all data from Shared Preferences
//        editor = preferences.edit();
//        editor.clear();
//        editor.commit();
//
//        // After logout redirect user to Loing Activity
//        Intent i = new Intent(context, LoginActivity.class);
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        context.startActivity(i);
//    }
//
//
//    // Get Login State
//    public boolean isLoggedIn(){
//        return preferences.getBoolean(IS_LOGIN, false);
//    }

}
