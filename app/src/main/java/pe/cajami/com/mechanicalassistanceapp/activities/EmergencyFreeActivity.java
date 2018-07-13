package pe.cajami.com.mechanicalassistanceapp.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.AddressLocation;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;

public class EmergencyFreeActivity extends AppCompatActivity {

    Double latitude = -12.0775;
    Double longitude = -77.0934;
    Geocoder geocoder;

    private static final String TAG = EmergencyFreeActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_free);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {

                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                        getLastLocation();

                        if (true) return;

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                            String iiiiii = "";
                            FunctionsGeneral.showMessageToast(EmergencyFreeActivity.this, city);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else {
                        // At least one permission is denied
                        FunctionsGeneral.showMessageToast(EmergencyFreeActivity.this, "denagado");

                    }
                });
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            latitude = mLastLocation.getLatitude();
                            longitude = mLastLocation.getLongitude();


                            AddressLocation addressLocation = FunctionsGeneral.getAddrees(EmergencyFreeActivity.this,latitude,longitude);
                            FunctionsGeneral.showMessageToast(EmergencyFreeActivity.this,"la ciudad es "+ addressLocation.getCity());


//                            mLatitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
//                                    mLatitudeLabel,
//                                    mLastLocation.getLatitude()));
//                            mLongitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
//                                    mLongitudeLabel,
//                                    mLastLocation.getLongitude()));
                        } else {
//                            Log.w(TAG, "getLastLocation:exception", task.getException());
//                            showSnackbar(getString(R.string.no_location_detected));
                            FunctionsGeneral.showMessageToast(EmergencyFreeActivity.this, "No se pudo");
                        }
                    }
                });
    }


}
