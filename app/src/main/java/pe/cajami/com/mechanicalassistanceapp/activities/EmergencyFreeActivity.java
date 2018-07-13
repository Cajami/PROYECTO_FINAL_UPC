package pe.cajami.com.mechanicalassistanceapp.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.adapters.AdapterEmergencyFree;
import pe.cajami.com.mechanicalassistanceapp.api.AddressLocation;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Request;

public class EmergencyFreeActivity extends AppCompatActivity {

    RecyclerView rvEmergenciasLibres;
    GridLayoutManager emergencyLayoutManager;
    AdapterEmergencyFree emergencyAdapter;
    List<Request> requests;

    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_free);

        rvEmergenciasLibres = (RecyclerView) findViewById(R.id.rvEmergenciasLibres);


        requests = new ArrayList<>();
        emergencyAdapter = new AdapterEmergencyFree(requests);
        emergencyLayoutManager = new GridLayoutManager(EmergencyFreeActivity.this,1);

        rvEmergenciasLibres.setAdapter(emergencyAdapter);
        rvEmergenciasLibres.setLayoutManager(emergencyLayoutManager);

        token = FunctionsGeneral.getToken(EmergencyFreeActivity.this);

        getEmergenciesFree();

    }

    public void getEmergenciesFree() {

        final ProgressDialog mProgressDialog = new ProgressDialog(EmergencyFreeActivity.this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Recuperando...");
        mProgressDialog.show();

        AndroidNetworking.get(MechanicalApi.getEmergenciesFree())
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

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                            if (requests.size()>0)
                                requests = new ArrayList<>();

                            Request request = null;
                            for (int i = 0; i < requestArray.length(); i++) {
                                request = new Request();
                                request.setIdrequest(requestArray.getJSONObject(i).getInt("idrequest"))
                                        .setDate(dateFormat.parse(requestArray.getJSONObject(i).getString("date")))
                                        .setIdcar(requestArray.getJSONObject(i).getInt("idcar"))
                                        .setDetails(requestArray.getJSONObject(i).getString("details"))
                                        .setIdflaw(requestArray.getJSONObject(i).getInt("idflaw"))
                                        .setDistrict(requestArray.getJSONObject(i).getString("district"))
                                        .setAddress(requestArray.getJSONObject(i).getString("address"))
                                        .setLatitude(requestArray.getJSONObject(i).getDouble("latitude"))
                                        .setLongitude(requestArray.getJSONObject(i).getDouble("longitude"))
                                        .setDescription(requestArray.getJSONObject(i).getString("description"));
                                requests.add(request);
                            }

                            emergencyAdapter.setRequests(requests);
                            emergencyAdapter.notifyDataSetChanged();



                            /*
                            {"request":[
                            {"idrequest":14,"date":"2018-07-13T15:41:56.000Z","idcar":5,"idstate":"P","details":"POR FAVOR URGENTE","idflaw":1,
                            "district":"Cercado de Lima","address":"Wari 522, Cercado de Lima 15003, Perú","latitude":-12.0497795,
                            "longitude":-77.014019,"description":"PONCHADO DE LLANTA"},


                            {"idrequest":15,"date":"2018-07-13T15:42:43.000Z","idcar":5,"idstate":"P","details":"DEBO LLEGAR A MI REUNION LO ANTES POSIBLE",
                            "idflaw":3,"district":"Cercado de Lima","address":"Wari 522, Cercado de Lima 15003, Perú","latitude":-12.0497795,"longitude":-77.014019,
                            "description":"AUTO NO ARRANCA"}]}


                             */


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mProgressDialog.dismiss();
                        FunctionsGeneral.showMessageErrorUser(EmergencyFreeActivity.this, "Error!!!");
                    }
                });

    }

}
