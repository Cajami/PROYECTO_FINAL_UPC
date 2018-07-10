package pe.cajami.com.mechanicalassistanceapp.activities;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Car;
import pe.cajami.com.mechanicalassistanceapp.models.Flaw;
import pe.cajami.com.mechanicalassistanceapp.models.Request;

public class RegisterEmergencyActivity extends AppCompatActivity {

    Spinner cboTipoEmergencia_RegisterEmergency;
    EditText txtDetalle_RegisterEmergency;
    Button btnRegistrarEmergencia_RegisterEmergency;

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

    View.OnClickListener btnRegistrarEmergenciaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            List<Car> cars = Car.listAll(Car.class);

            if (cars.size() == 0) {
                FunctionsGeneral.showMessageErrorUser(RegisterEmergencyActivity.this, "No se encontró ningun Auto asociado");
                return;
            }

            AndroidNetworking.post(MechanicalApi.insertRequests())
                    .addBodyParameter("idcar", String.valueOf(cars.get(0).getIdcar()))
                    .addBodyParameter("details", txtDetalle_RegisterEmergency.getText().toString().trim())

                    .setTag(getString(R.string.tagMechanical))
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                //SimpleDateFormat dateFormat = new SimpleDateFormat();

                                JSONObject requestJSON = response.getJSONObject("request");
                                Request request = new Request();
                                request.setIdrequest(requestJSON.getInt("idrequest"))
                                        .setDate(dateFormat.parse(requestJSON.getString("date")))//.substring(0,19)))
                                        .setIdcar(requestJSON.getInt("idcar"))
                                        .setIdstate(requestJSON.getString("idstate"))
                                        .setDetails(requestJSON.getString("details"))
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
                            FunctionsGeneral.showMessageErrorUser(RegisterEmergencyActivity.this, "Error!!!");
                        }
                    });
        }
    };
}
