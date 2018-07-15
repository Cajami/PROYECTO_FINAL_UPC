package pe.cajami.com.mechanicalassistanceapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONObject;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.AddressLocation;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Provider;
import pe.cajami.com.mechanicalassistanceapp.models.Request;
import pe.cajami.com.mechanicalassistanceapp.models.RequestHistory;

public class EmergencyPostulateActivity extends AppCompatActivity {

    TextView lblTipoEmergencia, lblDetalle, lblDistrito, lblDireccion;
    ANImageView imagenMap;
    Button btnPostular, btnRetirarPostulacion;

    Request request;
    RequestHistory requestHistory = null;

    String token = "";
    Provider provider;

    AddressLocation addressLocation = null;
    FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_postulate);

        lblTipoEmergencia = (TextView) findViewById(R.id.lblTipoEmergencia);
        lblDetalle = (TextView) findViewById(R.id.lblDetalle);
        lblDistrito = (TextView) findViewById(R.id.lblDistrito);
        lblDireccion = (TextView) findViewById(R.id.lblDireccion);
        imagenMap = (ANImageView) findViewById(R.id.imagenMap);
        btnPostular = (Button) findViewById(R.id.btnPostular);
        btnRetirarPostulacion = (Button) findViewById(R.id.btnRetirarPostulacion);

        imagenMap.setDefaultImageResId(R.mipmap.ic_launcher);
        imagenMap.setErrorImageResId(R.mipmap.ic_launcher);

        Intent intent = getIntent();
        if (intent == null) return;

        token = FunctionsGeneral.getToken(EmergencyPostulateActivity.this);

        request = Request.toBuilder(intent.getExtras());
        provider = Provider.listAll(Provider.class).get(0);

        btnPostular.setOnClickListener(btnPostularOnClickListener);

        RxPermissions rxPermissions = new RxPermissions(EmergencyPostulateActivity.this);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {
                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(EmergencyPostulateActivity.this);
                        getLastLocation();

                        updateViewFrom();
                        getHistory();
                    } else {
                        FunctionsGeneral.showMessageAlertUser(EmergencyPostulateActivity.this, getString(R.string.tagMechanical), "Debe aceptar el permiso para compartir ubicación",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });

                    }
                });
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            addressLocation = FunctionsGeneral.getAddrees(EmergencyPostulateActivity.this, task.getResult().getLatitude(), task.getResult().getLongitude());
                        } else {
                            addressLocation = new AddressLocation();
                        }
                    }
                });
    }

    private void updateViewFrom() {
        lblTipoEmergencia.setText(request.getDescription());
        lblDetalle.setText(request.getDetails());
        lblDistrito.setText(request.getDistrict());
        lblDireccion.setText(request.getAddress());
    }

    private void getHistory() {
        final ProgressDialog mProgressDialog = new ProgressDialog(EmergencyPostulateActivity.this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Recuperando Estado...");
        mProgressDialog.show();

        AndroidNetworking.post(MechanicalApi.getEmergencyFreeHistorial())
                .addBodyParameter("idrequest", String.valueOf(request.getIdrequest()))
                .addBodyParameter("idprovider", String.valueOf(provider.getIdprovider()))
                .addBodyParameter("token", token)
                .setTag(getString(R.string.tagMechanical))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProgressDialog.dismiss();

                        try {
                            JSONArray respuesta = response.getJSONArray("request");

                            requestHistory = new RequestHistory();

                            if (respuesta.length() > 0) {
                                requestHistory.setIdrequesthistory(respuesta.getJSONObject(0).getInt("idrequesthistory"))
                                        .setIdrequest(respuesta.getJSONObject(0).getInt("idrequest"))
                                        .setDate(FunctionsGeneral.getStringToDate(respuesta.getJSONObject(0).getString("date")))
                                        .setIdstate(respuesta.getJSONObject(0).getString("idstate"))
                                        .setIdprovider(respuesta.getJSONObject(0).getInt("idprovider"))
                                        .setLatitude(respuesta.getJSONObject(0).getDouble("latitude"))
                                        .setLongitude(respuesta.getJSONObject(0).getDouble("longitude"));
                                btnRetirarPostulacion.setVisibility(View.VISIBLE);
                            } else {
                                btnPostular.setVisibility(View.VISIBLE);
                                requestHistory.setIdprovider(provider.getIdprovider())
                                        .setIdrequest(request.getIdrequest());
                            }
                            imagenMap.setImageUrl(FunctionsGeneral.getStaticmap(request.getLatitude(), request.getLongitude()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mProgressDialog.dismiss();
                        FunctionsGeneral.showMessageErrorUser(EmergencyPostulateActivity.this, "Error!!!");
                    }
                });
    }

    View.OnClickListener btnPostularOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (requestHistory.getLatitude() == 0) {
                requestHistory.setLatitude(addressLocation.getLatitude());
                requestHistory.setLongitude(addressLocation.getLongitude());
            }
            FunctionsGeneral.showMessageConfirmationUser(EmergencyPostulateActivity.this, "¿Está seguro que desea postular a esta emergencia?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            final ProgressDialog mProgressDialog = new ProgressDialog(EmergencyPostulateActivity.this);
                            mProgressDialog.setCanceledOnTouchOutside(false);
                            mProgressDialog.setMessage("Registrando Postulación...");
                            mProgressDialog.show();

                            AndroidNetworking.post(MechanicalApi.insertRequestsHistory())
                                    .addBodyParameter("idrequesthistory", String.valueOf(requestHistory.getIdrequesthistory()))
                                    .addBodyParameter("idrequest", String.valueOf(requestHistory.getIdrequest()))
                                    .addBodyParameter("idprovider", String.valueOf(requestHistory.getIdprovider()))
                                    .addBodyParameter("latitude", String.valueOf(requestHistory.getLatitude()))
                                    .addBodyParameter("longitude", String.valueOf(requestHistory.getLongitude()))
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

                                                requestHistory.setIdrequesthistory(requestJSON.getInt("idrequesthistory"))
                                                        .setDate(FunctionsGeneral.getStringToDate(requestJSON.getString("date")));

                                                FunctionsGeneral.showMessageAlertUser(EmergencyPostulateActivity.this, "Registro Exitoso", "Se registró su postulación", new DialogInterface.OnClickListener() {
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
                                            FunctionsGeneral.showMessageErrorUser(EmergencyPostulateActivity.this, "Error!!!");
                                        }
                                    });
                        }
                    }, null);
        }
    };
}
