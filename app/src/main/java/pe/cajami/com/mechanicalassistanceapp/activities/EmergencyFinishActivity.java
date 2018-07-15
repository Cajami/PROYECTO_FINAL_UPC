package pe.cajami.com.mechanicalassistanceapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Car;
import pe.cajami.com.mechanicalassistanceapp.models.Favorite;
import pe.cajami.com.mechanicalassistanceapp.models.Flaw;
import pe.cajami.com.mechanicalassistanceapp.models.Request;
import pe.cajami.com.mechanicalassistanceapp.models.RequestHistory;

public class EmergencyFinishActivity extends AppCompatActivity {

    SeekBar seekBar;
    TextView lblCalificacion, lblTipoEmergencia, lblDetalle, lblFechaRegistro;
    Request request;
    RequestHistory requestHistory;
    Button btnFinalizarEmergencia;
    EditText txtObservacionAdicional;
    Switch switchAgregarFavorito;

    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_finish);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        lblCalificacion = (TextView) findViewById(R.id.lblCalificacion);
        lblTipoEmergencia = (TextView) findViewById(R.id.lblTipoEmergencia);
        lblDetalle = (TextView) findViewById(R.id.lblDetalle);
        lblFechaRegistro = (TextView) findViewById(R.id.lblFechaRegistro);

        txtObservacionAdicional = (EditText) findViewById(R.id.txtObservacionAdicional);
        btnFinalizarEmergencia = (Button) findViewById(R.id.btnFinalizarEmergencia);
        switchAgregarFavorito = (Switch) findViewById(R.id.switchAgregarFavorito);

        txtObservacionAdicional.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        token = FunctionsGeneral.getToken(EmergencyFinishActivity.this);

        List<Request> requests = Request.find(Request.class, "idstate = ?", "C");

        if (requests.size() == 0) {
            FunctionsGeneral.showMessageAlertUser(EmergencyFinishActivity.this, getString(R.string.tagMechanical),
                    "No se encontró Emergencia en curso",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }
            );
            return;
        }
        request = requests.get(0);

        List<RequestHistory> requestHistories = Request.find(RequestHistory.class, "idstate = ? and idrequest = ?",
                new String[]{"C", String.valueOf(request.getIdrequest())});

        if (requestHistories.size() == 0) {
            FunctionsGeneral.showMessageAlertUser(EmergencyFinishActivity.this, getString(R.string.tagMechanical),
                    "No se encontró proveedor asociado a Emergencia en curso",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }
            );
            return;
        }
        requestHistory = requestHistories.get(0);

        List<Flaw> arrayFallas = Flaw.listAll(Flaw.class);

        for (int i = 0; i < arrayFallas.size(); i++) {
            if (requests.get(0).getIdflaw() == arrayFallas.get(0).getIdflaw()) {
                lblTipoEmergencia.setText(arrayFallas.get(0).getDescription());
                break;
            }
        }

        lblDetalle.setText(requests.get(0).getDetails());
        lblFechaRegistro.setText(FunctionsGeneral.getDateToString(requests.get(0).getDate()));

        btnFinalizarEmergencia.setOnClickListener(btnFinalizarEmergenciaOnClickListener);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i + 1;
                lblCalificacion.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lblCalificacion.setText(progress + "");
            }
        });
    }

    View.OnClickListener btnFinalizarEmergenciaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FunctionsGeneral.showMessageConfirmationUser(EmergencyFinishActivity.this,
                    "¿Está seguro que desea finalizar la emergencia?<br/><br/>Puntuación: "+lblCalificacion.getText().toString(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final ProgressDialog mProgressDialog = new ProgressDialog(EmergencyFinishActivity.this);
                            mProgressDialog.setCanceledOnTouchOutside(false);
                            mProgressDialog.setMessage("Finalizando Emergencia...");
                            mProgressDialog.show();

                            String favorite = switchAgregarFavorito.isChecked() ? "1" : "0";

                            AndroidNetworking.post(MechanicalApi.setEmergencyFinishs())
                                    .addBodyParameter("idrequest", String.valueOf(request.getIdrequest()))
                                    .addBodyParameter("idrequesthistory", String.valueOf(requestHistory.getIdrequesthistory()))
                                    .addBodyParameter("idprovider", String.valueOf(requestHistory.getIdprovider()))
                                    .addBodyParameter("idcar", String.valueOf(request.getIdcar()))
                                    .addBodyParameter("score", lblCalificacion.getText().toString())
                                    .addBodyParameter("observation", txtObservacionAdicional.getText().toString())
                                    .addBodyParameter("favorite", favorite)
                                    .addBodyParameter("token", token)
                                    .setTag(getString(R.string.tagMechanical))
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            mProgressDialog.dismiss();

                                            try {
                                                if (response.has("message")) {
                                                    FunctionsGeneral.showMessageErrorUser(EmergencyFinishActivity.this, response.getString("message"));
                                                    return;
                                                } else {
                                                    request.setIdstate(response.getJSONObject("request").getString("idstate"))
                                                            .setObservation(response.getJSONObject("request").getString("observation"))
                                                            .setScore(response.getJSONObject("request").getInt("score"))
                                                            .setDatefinished(FunctionsGeneral.getStringToDate(response.getJSONObject("request").getString("datefinished")))
                                                            .save();

                                                    requestHistory.setIdstate(response.getJSONObject("requesthistory").getString("idstate"))
                                                            .save();

                                                    if (response.has("favorite")) {
                                                        List<Favorite> favorites = Favorite.find(Favorite.class, "idprovider = ? and idcustomer = ?", new String[]{
                                                                String.valueOf(requestHistory.getIdprovider()), response.getJSONObject("request").getString("idcustomer")});

                                                        if (favorites.size() == 0) {
                                                            Favorite favorite = new Favorite();
                                                            favorite.setIdfavorite(response.getJSONObject("favorite").getInt("idfavorite"))
                                                                    .setIdprovider(response.getJSONObject("favorite").getInt("idprovider"))
                                                                    .setDate(FunctionsGeneral.getStringToDate(response.getJSONObject("favorite").getString("idprovider")))
                                                                    .setIdcustomer(response.getJSONObject("request").getInt("idcustomer"))
                                                                    .setIdstate(response.getJSONObject("request").getString("idstate"))
                                                                    .save();
                                                        } else {
                                                            favorites.get(0).setIdstate(response.getJSONObject("request").getString("idstate"))
                                                                    .save();
                                                        }
                                                    }
                                                    FunctionsGeneral.showMessageAlertUser(EmergencyFinishActivity.this, getString(R.string.tagMechanical),
                                                            "Emergencia fue Finalizada",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    setResult(RESULT_OK);
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
                                            FunctionsGeneral.showMessageErrorUser(EmergencyFinishActivity.this, "Error!!!");
                                        }
                                    });
                        }
                    }, null);

        }
    };
}
