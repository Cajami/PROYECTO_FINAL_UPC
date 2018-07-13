package pe.cajami.com.mechanicalassistanceapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import pe.cajami.com.mechanicalassistanceapp.R;
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

        updateViewFrom();
        getHistory();
        imagenMap.setImageUrl("https://maps.googleapis.com/maps/api/staticmap?center=-12.0448827,-77.1005715&zoom=17&size=500x300&markers=icon:http%3A%2F%2Fgoo.gl%2FGjVUSC|-12.0448827,-77.1005715");
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
                            } else
                                btnPostular.setVisibility(View.VISIBLE);
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
}
