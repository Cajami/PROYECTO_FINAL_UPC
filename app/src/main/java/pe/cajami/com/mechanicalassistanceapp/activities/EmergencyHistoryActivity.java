package pe.cajami.com.mechanicalassistanceapp.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.adapters.AdapterEmergencyHistory;
import pe.cajami.com.mechanicalassistanceapp.api.EmergencyHistory;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Car;

public class EmergencyHistoryActivity extends AppCompatActivity {

    RecyclerView rvHistorial;
    AdapterEmergencyHistory adapterEmergencyHistory;
    List<EmergencyHistory> emergencyHistories;

    String token = "";
    Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_history);

        rvHistorial = (RecyclerView) findViewById(R.id.rvHistorial);
        emergencyHistories = new ArrayList<>();
        adapterEmergencyHistory = new AdapterEmergencyHistory(emergencyHistories);
        rvHistorial.setAdapter(adapterEmergencyHistory);
        rvHistorial.setLayoutManager(new LinearLayoutManager(EmergencyHistoryActivity.this));

        token = FunctionsGeneral.getToken(EmergencyHistoryActivity.this);

        List<Car> cars = Car.listAll(Car.class);
        if (cars.size() == 0) {
            FunctionsGeneral.showMessageToast(EmergencyHistoryActivity.this, "No se encontró carro asociado");
            finish();
        }

        car = cars.get(0);

        getEmergencyHistories();
    }

    public void getEmergencyHistories() {
        final ProgressDialog mProgressDialog = new ProgressDialog(EmergencyHistoryActivity.this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Recuperando...");
        mProgressDialog.show();

        AndroidNetworking.post(MechanicalApi.getEmergencyHistories())
                .addBodyParameter("idcar", String.valueOf(car.getIdcar()))
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
                                FunctionsGeneral.showMessageErrorUser(EmergencyHistoryActivity.this,response.getString("message"));
                                return;
                            }

                            JSONArray requestArray = response.getJSONArray("response");

                            if (emergencyHistories.size() > 0)
                                emergencyHistories = new ArrayList<>();

                            EmergencyHistory emergencyHistory = null;
                            for (int i = 0; i < requestArray.length(); i++) {
                                emergencyHistory = new EmergencyHistory();
                                emergencyHistory.setIdrequest(requestArray.getJSONObject(i).getInt("idrequest"))
                                        .setIdRequestHistory(requestArray.getJSONObject(i).getInt("idRequestHistory"))
                                        .setDate(FunctionsGeneral.getStringToDate(requestArray.getJSONObject(i).getString("date")))
                                        .setDatefinish(FunctionsGeneral.getStringToDate(requestArray.getJSONObject(i).getString("datefinish")))
                                        //.setIdcustomer(requestArray.getJSONObject(i).getInt("idcustomer")))
                                        .setIdprovider(requestArray.getJSONObject(i).getInt("idprovider"))
                                        .setIdflaw(requestArray.getJSONObject(i).getString("idflaw"))
                                        .setDetalleFlaw(requestArray.getJSONObject(i).getString("detalleFlaw"))
                                        .setDistrict(requestArray.getJSONObject(i).getString("district"))
                                        .setNameProvider(requestArray.getJSONObject(i).getString("nameProvider"))
                                        .setScore(requestArray.getJSONObject(i).getInt("score"))
                                        .setDistrict(requestArray.getJSONObject(i).getString("district"));
                                emergencyHistories.add(emergencyHistory);
                            }

                            adapterEmergencyHistory.setEmergencyHistories(emergencyHistories);
                            adapterEmergencyHistory.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mProgressDialog.dismiss();
                        FunctionsGeneral.showMessageErrorUser(EmergencyHistoryActivity.this, "Error!!!");
                    }
                });

    }
}
