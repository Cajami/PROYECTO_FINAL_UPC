package pe.cajami.com.mechanicalassistanceapp;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.orm.SugarApp;

public class MechanicalApp extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
