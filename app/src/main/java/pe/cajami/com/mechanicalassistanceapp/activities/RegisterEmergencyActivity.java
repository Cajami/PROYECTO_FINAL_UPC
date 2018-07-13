package pe.cajami.com.mechanicalassistanceapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.Manifest;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.AddressLocation;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Car;
import pe.cajami.com.mechanicalassistanceapp.models.Flaw;
import pe.cajami.com.mechanicalassistanceapp.models.Request;

public class RegisterEmergencyActivity extends AppCompatActivity {

    Spinner cboTipoEmergencia_RegisterEmergency;
    EditText txtDetalle_RegisterEmergency;
    Button btnRegistrarEmergencia_RegisterEmergency;

    AddressLocation addressLocation = null;
    FusedLocationProviderClient mFusedLocationClient;

    String token = "";
    List<Flaw> arrayFallas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_emergency);

        cboTipoEmergencia_RegisterEmergency = (Spinner) findViewById(R.id.cboTipoEmergencia_RegisterEmergency);
        txtDetalle_RegisterEmergency = (EditText) findViewById(R.id.txtDetalle_RegisterEmergency);
        btnRegistrarEmergencia_RegisterEmergency = (Button) findViewById(R.id.btnRegistrarEmergencia_RegisterEmergency);

        btnRegistrarEmergencia_RegisterEmergency.setOnClickListener(btnRegistrarEmergenciaOnClickListener);
        txtDetalle_RegisterEmergency.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        token = FunctionsGeneral.getToken(RegisterEmergencyActivity.this);

        getFlaws();
    }

    public void getFlaws() {
        arrayFallas = Flaw.listAll(Flaw.class);
        if (arrayFallas.size() > 0) {
            String[] documentos = new String[arrayFallas.size()];

            for (int i = 0; i < arrayFallas.size(); i++) {
                documentos[i] = arrayFallas.get(i).getDescription();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterEmergencyActivity.this, android.R.layout.simple_spinner_item, documentos);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboTipoEmergencia_RegisterEmergency.setAdapter(adapter);

            RxPermissions rxPermissions = new RxPermissions(RegisterEmergencyActivity.this);
            rxPermissions
                    .request(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION) // ask single or multiple permission once
                    .subscribe(granted -> {
                        if (granted) {
                            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(RegisterEmergencyActivity.this);
                            getLastLocation();
                        } else {
                            FunctionsGeneral.showMessageConfirmationUser(RegisterEmergencyActivity.this, "Debe aceptar el permiso para compartir ubicación para registrar una emergencia",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    }, null);

                        }
                    });
        } else {
            AndroidNetworking.get(MechanicalApi.getFlaws())
                    .setTag(getString(R.string.tagMechanical))
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray flawsResponde = response.getJSONArray("response");
                                //String[] distritos = new String[distritosResponde.length()];

                                Flaw flaw = null;
                                for (int i = 0; i < flawsResponde.length(); i++) {
                                    flaw = new Flaw();
                                    flaw.setIdflaw(flawsResponde.getJSONObject(i).getInt("idflaw"))
                                            .setDescription(flawsResponde.getJSONObject(i).getString("description"))
                                            .save();
                                }
                                getFlaws();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            FunctionsGeneral.showMessageErrorUser(RegisterEmergencyActivity.this, "Error!!!");
                        }
                    });
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            addressLocation = FunctionsGeneral.getAddrees(RegisterEmergencyActivity.this, task.getResult().getLatitude(), task.getResult().getLongitude());
                        } else {
                            addressLocation = new AddressLocation();
                        }
                    }
                });
    }

    View.OnClickListener btnRegistrarEmergenciaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            List<Car> cars = Car.listAll(Car.class);

            if (cars.size() == 0) {
                FunctionsGeneral.showMessageErrorUser(RegisterEmergencyActivity.this, "No se encontró ningun Auto asociado");
                return;
            }

            if (addressLocation == null) {
                FunctionsGeneral.showMessageErrorUser(RegisterEmergencyActivity.this, "Ubicación no encontrada aún");
                return;
            }

            final ProgressDialog mProgressDialog = new ProgressDialog(RegisterEmergencyActivity.this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage("Registrando...");
            mProgressDialog.show();

            AndroidNetworking.post(MechanicalApi.insertRequests())
                    .addBodyParameter("idcar", String.valueOf(cars.get(0).getIdcar()))
                    .addBodyParameter("details", txtDetalle_RegisterEmergency.getText().toString().trim())
                    .addBodyParameter("idflaw", String.valueOf(arrayFallas.get(cboTipoEmergencia_RegisterEmergency.getSelectedItemPosition()).getIdflaw()))
                    .addBodyParameter("district", addressLocation.getCity())
                    .addBodyParameter("address", addressLocation.getAddress())
                    .addBodyParameter("latitude", String.valueOf(addressLocation.getLatitude()))
                    .addBodyParameter("longitude", String.valueOf(addressLocation.getLongitude()))
                    .addBodyParameter("token", token)
                    .setTag(getString(R.string.tagMechanical))
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mProgressDialog.dismiss();

                            try {
                                JSONObject requestJSON = response.getJSONObject("request");
                                Request request = new Request();
                                request.setIdrequest(requestJSON.getInt("idrequest"))
                                        .setDate(FunctionsGeneral.getStringToDate(requestJSON.getString("date")))//.substring(0,19)))
                                        .setIdcar(requestJSON.getInt("idcar"))
                                        .setIdstate(requestJSON.getString("idstate"))
                                        .setDetails(requestJSON.getString("details"))
                                        .setIdflaw(requestJSON.getInt("idflaw"))
                                        .save();

                                FunctionsGeneral.showMessageAlertUser(RegisterEmergencyActivity.this, "Se Registró", "Emergencia", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            mProgressDialog.dismiss();
                            FunctionsGeneral.showMessageErrorUser(RegisterEmergencyActivity.this, "Error!!!");
                        }
                    });
        }
    };
}
