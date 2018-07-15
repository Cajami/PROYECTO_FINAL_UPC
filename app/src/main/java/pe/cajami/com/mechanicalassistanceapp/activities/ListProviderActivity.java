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
import pe.cajami.com.mechanicalassistanceapp.adapters.AdapterProvider;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Provider;

public class ListProviderActivity extends AppCompatActivity {

    RecyclerView rvProveedores;
    AdapterProvider adapterProvider;
    String token;
    List<Provider> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_provider);

        providers = new ArrayList<>();
        adapterProvider = new AdapterProvider(providers);
        rvProveedores = (RecyclerView) findViewById(R.id.rvProveedores);
        rvProveedores.setLayoutManager(new LinearLayoutManager(ListProviderActivity.this));
        rvProveedores.setAdapter(adapterProvider);

        token = FunctionsGeneral.getToken(ListProviderActivity.this);
        getListProviders();
    }

    public void getListProviders() {
        final ProgressDialog mProgressDialog = new ProgressDialog(ListProviderActivity.this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Recuperando...");
        mProgressDialog.show();

        AndroidNetworking.post(MechanicalApi.getProviders())
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
                                FunctionsGeneral.showMessageErrorUser(ListProviderActivity.this, response.getString("message"));
                                return;
                            }

                            JSONArray requestArray = response.getJSONArray("response");

                            if (providers.size() > 0)
                                providers = new ArrayList<>();

                            Provider provider = null;
                            for (int i = 0; i < requestArray.length(); i++) {
                                provider = new Provider();

                                provider.setIdprovider(requestArray.getJSONObject(i).getInt("idprovider"))
                                        .setItypedocument(requestArray.getJSONObject(i).getInt("itypedocument"))
                                        .setNrodocument(requestArray.getJSONObject(i).getString("nrodocument"))
                                        .setIduser(Integer.parseInt(requestArray.getJSONObject(i).getString("iduser")))
                                        .setName(requestArray.getJSONObject(i).getString("name"))
                                        .setAddress(requestArray.getJSONObject(i).getString("address"))
                                        .setIddistrict(requestArray.getJSONObject(i).getInt("iddistrict"))
                                        .setContact(requestArray.getJSONObject(i).getString("contact"))
                                        .setPhone(requestArray.getJSONObject(i).getString("phone"))
                                        .setEmail(requestArray.getJSONObject(i).getString("email"))
                                        .setWeb(requestArray.getJSONObject(i).getString("web"))
                                        .setScore(requestArray.getJSONObject(i).getInt("score"))
                                        .setLongitude(requestArray.getJSONObject(i).getDouble("longitude"))
                                        .setLatitude(requestArray.getJSONObject(i).getDouble("latitude"))
                                        .setDescriptionTypeDocument(requestArray.getJSONObject(i).getString("descriptionTypeDocument"));
                                providers.add(provider);
                            }

                            adapterProvider.setProviders(providers);
                            adapterProvider.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mProgressDialog.dismiss();
                        FunctionsGeneral.showMessageErrorUser(ListProviderActivity.this, "Error!!!");
                    }
                });
    }
}
