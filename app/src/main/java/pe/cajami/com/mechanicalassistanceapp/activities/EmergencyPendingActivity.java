package pe.cajami.com.mechanicalassistanceapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.adapters.AdapterEmergencyProviders;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Flaw;
import pe.cajami.com.mechanicalassistanceapp.models.Request;
import pe.cajami.com.mechanicalassistanceapp.models.RequestHistory;

public class EmergencyPendingActivity extends AppCompatActivity {

    List<Request> requests;
    TextView lblTipoEmergencia, lblDetalle, lblFechaRegistro;
    ImageView btnRefrescar;

    String token;

    RecyclerView rvProveedores;
    GridLayoutManager emergencyLayoutManager;
    AdapterEmergencyProviders adapterEmergencyProviders;
    List<RequestHistory> requestHistories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_pending);

        requests = Request.find(Request.class, "idstate = ?", "P");

        if (requests.size() == 0) {
            FunctionsGeneral.showMessageAlertUser(EmergencyPendingActivity.this, "Sn Datos", "No cuenta con Emergencias Pendientes",
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
        adapterEmergencyProviders = new AdapterEmergencyProviders(requestHistories);
        emergencyLayoutManager = new GridLayoutManager(EmergencyPendingActivity.this, 1);

        rvProveedores.setAdapter(adapterEmergencyProviders);
        rvProveedores.setLayoutManager(emergencyLayoutManager);

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
                .addHeaders("token", token)
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
}
