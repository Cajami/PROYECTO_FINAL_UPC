package pe.cajami.com.mechanicalassistanceapp;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

public class MechanicalApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
