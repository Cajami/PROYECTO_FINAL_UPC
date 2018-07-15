package pe.cajami.com.mechanicalassistanceapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.internal.operators.flowable.FlowableMergeWithCompletable;
import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.adapters.AdapterEmergencyProviders;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Flaw;
import pe.cajami.com.mechanicalassistanceapp.models.Request;
import pe.cajami.com.mechanicalassistanceapp.models.RequestHistory;

public class EmergencyPendingActivity extends AppCompatActivity implements AdapterEmergencyProviders.AdapterCallback {

    List<Request> requests;
    TextView lblTipoEmergencia, lblDetalle, lblFechaRegistro;
    ImageView btnRefrescar;

    String token;

    RecyclerView rvProveedores;
    AdapterEmergencyProviders adapterEmergencyProviders;
    List<RequestHistory> requestHistories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_pending);

        requests = Request.find(Request.class, "idstate = ?", "P");

        if (requests.size() == 0) {
            FunctionsGeneral.showMessageAlertUser(EmergencyPendingActivity.this, "Sin Datos", "No cuenta con Emergencias Pendientes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            return;
        }

        token = FunctionsGeneral.getToken(EmergencyPendingActivity.this);

        List<Flaw> arrayFallas = Flaw.listAll(Flaw.class);

        lblTipoEmergencia = (TextView) findViewById(R.id.lblTipoEmergencia);
        lblDetalle = (TextView) findViewById(R.id.lblDetalle);
        lblFechaRegistro = (TextView) findViewById(R.id.lblFechaRegistro);

        btnRefrescar = (ImageView) findViewById(R.id.btnRefrescar);
        rvProveedores = (RecyclerView) findViewById(R.id.rvProveedores);

        requestHistories = new ArrayList<>();
        adapterEmergencyProviders = new AdapterEmergencyProviders(requestHistories, this);

        rvProveedores.setAdapter(adapterEmergencyProviders);
        rvProveedores.setLayoutManager(new LinearLayoutManager(this));

        for (int i = 0; i < arrayFallas.size(); i++) {
            if (requests.get(0).getIdflaw() == arrayFallas.get(0).getIdflaw()) {
                lblTipoEmergencia.setText(arrayFallas.get(0).getDescription());
                break;
            }
        }

        lblDetalle.setText(requests.get(0).getDetails());
        lblFechaRegistro.setText(FunctionsGeneral.getDateToString(requests.get(0).getDate()));

        getEmergencyProvidersPostulate();
    }

    public void getEmergencyProvidersPostulate() {
        final ProgressDialog mProgressDialog = new ProgressDialog(EmergencyPendingActivity.this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Recuperando Proveedores...");
        mProgressDialog.show();

        AndroidNetworking.post(MechanicalApi.getEmergencyProviders())
                .addBodyParameter("idrequest", String.valueOf(requests.get(0).getIdrequest()))
                .addBodyParameter("token", token)
                .setTag(getString(R.string.tagMechanical))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProgressDialog.dismiss();

                        try {
                            JSONArray requestArray = response.getJSONArray("request");

                            if (requestHistories.size() > 0)
                                requestHistories = new ArrayList<>();

                            RequestHistory requestHistory = null;
                            for (int i = 0; i < requestArray.length(); i++) {
                                requestHistory = new RequestHistory();
                                requestHistory.setIdrequesthistory(requestArray.getJSONObject(i).getInt("idrequesthistory"))
                                        .setIdrequest(requestArray.getJSONObject(i).getInt("idrequest"))
                                        .setDate(FunctionsGeneral.getStringToDate(requestArray.getJSONObject(i).getString("date")))
                                        .setIdstate(requestArray.getJSONObject(i).getString("idstate"))
                                        .setIdprovider(requestArray.getJSONObject(i).getInt("idprovider"))
                                        .setNameProvider(requestArray.getJSONObject(i).getString("name"))
                                        .setScoreProvider(requestArray.getJSONObject(i).getInt("score"))
                                        .setLatitude(requestArray.getJSONObject(i).getDouble("latitude"))
                                        .setLongitude(requestArray.getJSONObject(i).getDouble("longitude"))
                                        .setLatitudeParent(requests.get(0).getLatitude())
                                        .setLongitudeParent(requests.get(0).getLongitude());

                                requestHistories.add(requestHistory);
                            }

                            adapterEmergencyProviders.setRequestHistories(requestHistories);
                            adapterEmergencyProviders.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mProgressDialog.dismiss();
                        FunctionsGeneral.showMessageErrorUser(EmergencyPendingActivity.this, "Error!!!");
                    }
                });

    }

    @Override
    public void onClickCallback(final RequestHistory item, int position) {
        FunctionsGeneral.showMessageConfirmationUser(EmergencyPendingActivity.this,
                "¿Acepta que el proveedor " + item.getNameProvider() + " atienda su emergencia?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog mProgressDialog = new ProgressDialog(EmergencyPendingActivity.this);
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.setMessage("Asociando Emergencia...");
                        mProgressDialog.show();

                        AndroidNetworking.post(MechanicalApi.setEmergencyToProviders())
                                .addBodyParameter("idrequest", String.valueOf(requests.get(0).getIdrequest()))
                                .addBodyParameter("idrequesthistory", String.valueOf(item.getIdrequesthistory()))
                                .addBodyParameter("token", token)
                                .setTag(getString(R.string.tagMechanical))
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        mProgressDialog.dismiss();

                                        try {
                                           if (response.has("message")){
                                               FunctionsGeneral.showMessageAlertUser(EmergencyPendingActivity.this,getString(R.string.tagMechanical),response.getString("message"),null);
                                           }else{
                                               requests.get(0).setIdstate(response.getJSONObject("request").getString("idstate"))
                                                       .save();

                                               FunctionsGeneral.showMessageAlertUser(EmergencyPendingActivity.this,
                                                       getString(R.string.tagMechanical),
                                                       "Emergencia Asociada al proveedor: " + item.getNameProvider(),
                                                       new DialogInterface.OnClickListener() {
                                                           @Override
                                                           public void onClick(DialogInterface dialogInterface, int i) {
                                                               finish();
                                                           }
                                                       });
                                           }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        mProgressDialog.dismiss();
                                        FunctionsGeneral.showMessageErrorUser(EmergencyPendingActivity.this, "Error!!!");
                                    }
                                });


                    }
                }, null);
    }
}
