package pe.cajami.com.mechanicalassistanceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.activities.EditCustomerActivity;
import pe.cajami.com.mechanicalassistanceapp.activities.EditProviderActivity;
import pe.cajami.com.mechanicalassistanceapp.activities.MainCustomerActivity;
import pe.cajami.com.mechanicalassistanceapp.activities.MainProviderActivity;
import pe.cajami.com.mechanicalassistanceapp.activities.RegisterUserActivity;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Car;
import pe.cajami.com.mechanicalassistanceapp.models.Customer;
import pe.cajami.com.mechanicalassistanceapp.models.Flaw;
import pe.cajami.com.mechanicalassistanceapp.models.Provider;
import pe.cajami.com.mechanicalassistanceapp.models.Request;
import pe.cajami.com.mechanicalassistanceapp.models.RequestHistory;

public class LoginActivity extends AppCompatActivity {

    TextView lblRegistrarse_Login;
    EditText txtUsuario_Login, txtClave_Login;
    Button btnIngresar_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        startActivity(new Intent(LoginActivity.this, EditProviderActivity.class));
//        finish();
//        if (true)
//            return;

        SharedPreferences mPrefs = getSharedPreferences(getString(R.string.keypreference), MODE_PRIVATE); //add key
String iiii =mPrefs.getString("token", "");
        if (mPrefs.getString("token", "").length() > 0) {
            Intent intent = null;

            List<Customer> customer = Customer.listAll(Customer.class);
            if (customer.size() == 0) {
                Provider provider = Provider.listAll(Provider.class).get(0);
                if (provider.getName() == null)
                    intent = new Intent(LoginActivity.this, EditProviderActivity.class);
                else
                    intent = new Intent(LoginActivity.this, MainProviderActivity.class);
            } else {
                if (customer.get(0).getName() == null)
                    intent = new Intent(LoginActivity.this, EditCustomerActivity.class);
                else
                    intent = new Intent(LoginActivity.this, MainCustomerActivity.class);
            }
            startActivity(intent);
            finish();
        }


        lblRegistrarse_Login = (TextView) findViewById(R.id.lblRegistrarse_Login);
        txtUsuario_Login = (EditText) findViewById(R.id.txtUsuario_Login);
        txtClave_Login = (EditText) findViewById(R.id.txtClave_Login);
        btnIngresar_Login = (Button) findViewById(R.id.btnIngresar_Login);

        lblRegistrarse_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });

        btnIngresar_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtUsuario_Login.getText().toString().length() == 0) {
                    FunctionsGeneral.showMessageToast(LoginActivity.this, "Debe in gresar un Usuario");
                    return;
                }

                if (txtClave_Login.getText().toString().length() == 0) {
                    FunctionsGeneral.showMessageToast(LoginActivity.this, "Debe in gresar una Clave");
                    return;
                }

                final ProgressDialog mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setMessage("Conectanose...");
                mProgressDialog.show();

                AndroidNetworking.post(MechanicalApi.loginApi())
                        .addBodyParameter("user", txtUsuario_Login.getText().toString().trim())
                        .addBodyParameter("clave", txtClave_Login.getText().toString().trim())
                        .setTag(getString(R.string.tagMechanical))
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                mProgressDialog.dismiss();

                                try {
                                    if (response.has("message")) {
                                        FunctionsGeneral.showMessageAlertUser(LoginActivity.this, getString(R.string.tagMechanical), response.getString("message"), null);
                                        return;
                                    }

                                    SharedPreferences mPrefs = getSharedPreferences(getString(R.string.keypreference), MODE_PRIVATE); //add key
                                    SharedPreferences.Editor editor = mPrefs.edit();
                                    editor.putString("token", response.getString("token"));
                                    editor.apply();

                                    /*BORRAMOS CUSTOMER Y PROVIDER*/
                                    Customer.deleteAll(Customer.class);
                                    Provider.deleteAll(Provider.class);
                                    Car.deleteAll(Car.class);
                                    Request.deleteAll(Request.class);
                                    RequestHistory.deleteAll(RequestHistory.class);
                                    Flaw.deleteAll(Flaw.class);

                                    Intent intent = null;

                                    if (response.has("customer")) {
                                        JSONObject customerResponde = response.getJSONObject("customer");

                                        Customer customer = new Customer();

                                        customer.setIdcustomer(customerResponde.getInt("idcustomer"))
                                                .setItypedocument(customerResponde.getInt("itypedocument"))
                                                .setNrodocumento(customerResponde.getString("nrodocument"))
                                                .setIduser(Integer.parseInt(customerResponde.getString("iduser")));

                                        if (customerResponde.isNull("name"))
                                            intent = new Intent(LoginActivity.this, EditCustomerActivity.class);
                                        else {
                                            intent = new Intent(LoginActivity.this, MainCustomerActivity.class);

                                            customer.setName(customerResponde.getString("name"))
                                                    .setAddress(customerResponde.getString("address"))
                                                    .setIddistrict(Integer.parseInt(customerResponde.getString("iddistrict")))
                                                    .setPhone(customerResponde.getString("phone"))
                                                    .setEmail(customerResponde.getString("email"));

                                            JSONArray carResponde = response.getJSONArray("car");
                                            if (carResponde.length() > 0) {
                                                Car car = new Car();
                                                car.setIdcar(carResponde.getJSONObject(0).getInt("idcar"))
                                                        .setIdcustomer(carResponde.getJSONObject(0).getInt("idcustomer"))
                                                        .setIdbrand(carResponde.getJSONObject(0).getInt("idbrand"))
                                                        .setModel(carResponde.getJSONObject(0).getString("modelo"))
                                                        .setYear(carResponde.getJSONObject(0).getInt("year"))
                                                        .save();
                                            }

                                            JSONArray requestPending = response.getJSONArray("request");
                                            Request request = null;
                                            for (int i = 0; i < requestPending.length(); i++) {
                                                request = new Request();
                                                request.setIdrequest(requestPending.getJSONObject(i).getInt("idrequest"))
                                                        .setDate(FunctionsGeneral.getStringToDate(requestPending.getJSONObject(i).getString("date")))
                                                        .setIdcar(requestPending.getJSONObject(i).getInt("idcar"))
                                                        .setIdstate(requestPending.getJSONObject(i).getString("idstate"))
                                                        .setDetails(requestPending.getJSONObject(i).getString("details"))
                                                        .setIdflaw(requestPending.getJSONObject(i).getInt("idflaw"))
                                                        .setDistrict(requestPending.getJSONObject(i).getString("district"))
                                                        .setAddress(requestPending.getJSONObject(i).getString("address"))
                                                        .setLatitude(requestPending.getJSONObject(i).getDouble("latitude"))
                                                        .setLongitude(requestPending.getJSONObject(i).getDouble("longitude"))
                                                        .save();
                                            }

                                            if (response.has("requestHistory")){
                                                RequestHistory requestHistory = new RequestHistory();
                                                requestHistory.setIdrequesthistory(response.getJSONObject("requestHistory").getInt("idrequesthistory"))
                                                        .setIdrequest(response.getJSONObject("requestHistory").getInt("idrequest"))
                                                        .setDate(FunctionsGeneral.getStringToDate(response.getJSONObject("requestHistory").getString("date")))
                                                        .setIdstate(response.getJSONObject("requestHistory").getString("idstate"))
                                                        .setIdprovider(response.getJSONObject("requestHistory").getInt("idprovider"))
                                                        .setNameProvider(response.getJSONObject("requestHistory").getString("nameProveedor"))
                                                        .setLatitude(response.getJSONObject("requestHistory").getDouble("latitude"))
                                                        .setLongitude(response.getJSONObject("requestHistory").getDouble("longitude"))
                                                        .save();
                                            }

                                            JSONArray requestFlaws = response.getJSONArray("flaw");
                                            Flaw flaw = null;
                                            for (int i = 0; i < requestFlaws.length(); i++) {
                                                flaw = new Flaw();
                                                flaw.setIdflaw(requestFlaws.getJSONObject(i).getInt("idflaw"))
                                                        .setDescription(requestFlaws.getJSONObject(i).getString("description"))
                                                        .save();
                                            }
                                        }
                                        customer.save();

                                    } else {
                                        JSONObject providerResponde = response.getJSONObject("provider");

                                        Provider provider = new Provider();
                                        provider.setIdprovider(providerResponde.getInt("idprovider"))
                                                .setItypedocument(providerResponde.getInt("itypedocument"))
                                                .setNrodocument(providerResponde.getString("nrodocument"))
                                                .setIduser(Integer.parseInt(providerResponde.getString("iduser")));

                                        if (providerResponde.isNull("name"))
                                            intent = new Intent(LoginActivity.this, EditProviderActivity.class);
                                        else {
                                            intent = new Intent(LoginActivity.this, MainProviderActivity.class);

                                            provider.setName(providerResponde.getString("name"))
                                                    .setAddress(providerResponde.getString("address"))
                                                    .setIddistrict(providerResponde.getInt("iddistrict"))
                                                    .setContact(providerResponde.getString("contact"))
                                                    .setPhone(providerResponde.getString("phone"))
                                                    .setEmail(providerResponde.getString("email"))
                                                    .setWeb(providerResponde.getString("web"))
                                                    .setLongitude(providerResponde.getDouble("longitude"))
                                                    .setLatitude(providerResponde.getDouble("latitude"));

                                            if (!providerResponde.isNull("score"))
                                                provider.setScore(providerResponde.getInt("score"));
                                            if (!providerResponde.isNull("schedule"))
                                                provider.setSchedule(providerResponde.getString("schedule"));
                                        }
                                        provider.save();
                                    }

                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                mProgressDialog.dismiss();
                                try {
                                    if (error.getErrorCode() == 400)
                                        FunctionsGeneral.showMessageErrorUser(LoginActivity.this, "Usuario o Clave incorrectos");
                                    else
                                        FunctionsGeneral.showMessageErrorUser(LoginActivity.this, "Problemas para conectarnos con el servicio");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });

    }
}
