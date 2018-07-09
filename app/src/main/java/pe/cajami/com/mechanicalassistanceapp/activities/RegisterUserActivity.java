package pe.cajami.com.mechanicalassistanceapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.LoginActivity;
import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Customer;
import pe.cajami.com.mechanicalassistanceapp.models.District;
import pe.cajami.com.mechanicalassistanceapp.models.Provider;
import pe.cajami.com.mechanicalassistanceapp.models.TypeDocument;
import pe.cajami.com.mechanicalassistanceapp.models.TypeUser;
import pe.cajami.com.mechanicalassistanceapp.models.User;

public class RegisterUserActivity extends AppCompatActivity {

    Spinner cboTipoUsuario_RegisterUser, cboTipoDocumento_RegisterUser;
    EditText txtNroDocumento_RegisterUser, txtUsuario_RegisterUser, txtClave_RegisterUser, txtClaveRepetir_RegisterUser;
    Button btnRegistrar_RegisterUser;

    List<TypeDocument> arrayTipoDocumentos = new ArrayList<>();
    List<TypeUser> arrayTipoUsusarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        cboTipoUsuario_RegisterUser = (Spinner) findViewById(R.id.cboTipoUsuario_RegisterUser);
        cboTipoDocumento_RegisterUser = (Spinner) findViewById(R.id.cboTipoDocumento_RegisterUser);
        txtNroDocumento_RegisterUser = (EditText) findViewById(R.id.txtNroDocumento_RegisterUser);
        txtUsuario_RegisterUser = (EditText) findViewById(R.id.txtUsuario_RegisterUser);
        txtClave_RegisterUser = (EditText) findViewById(R.id.txtClave_RegisterUser);
        txtClaveRepetir_RegisterUser = (EditText) findViewById(R.id.txtClaveRepetir_RegisterUser);

        btnRegistrar_RegisterUser = (Button) findViewById(R.id.btnRegistrar_RegisterUser);

        getTypeDocumens();
        getTypeUers();

        btnRegistrar_RegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iTipoUsuario = 0;
                int iTipoDocumento = 0;

                if (cboTipoUsuario_RegisterUser.getSelectedItemPosition() == 0) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Debe seleccionar un tipo de usuario");
                    return;
                } else
                    iTipoUsuario = arrayTipoUsusarios.get(cboTipoUsuario_RegisterUser.getSelectedItemPosition() - 1).getIdtypeuser();

                if (cboTipoDocumento_RegisterUser.getSelectedItemPosition() == 0) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Debe seleccionar un tipo de documento");
                    return;
                } else
                    iTipoDocumento = arrayTipoDocumentos.get(cboTipoDocumento_RegisterUser.getSelectedItemPosition() - 1).getIdtypedocument();

                if (txtNroDocumento_RegisterUser.getText().toString().length() == 0) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Debe ingresar Nro de Documento");
                    return;
                }

                if (txtUsuario_RegisterUser.getText().toString().length() == 0) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Debe ingresar un Usuario");
                    return;
                }


                if (txtClave_RegisterUser.getText().toString().length() == 0) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Debe ingresar una clave");
                    return;
                }

                if (txtClaveRepetir_RegisterUser.getText().toString().length() == 0) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Debe repetir la clave");
                    return;
                }

                if (!txtClave_RegisterUser.getText().toString().equalsIgnoreCase(txtClave_RegisterUser.getText().toString())) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Las claves deben ser iguales");
                    return;
                }

                //registerUsers
                final ProgressDialog mProgressDialog = new ProgressDialog(RegisterUserActivity.this);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setMessage("Registrando Usuario...");
                mProgressDialog.show();

                AndroidNetworking.post(MechanicalApi.registerUsers())
                        .addBodyParameter("nrodocument", txtNroDocumento_RegisterUser.getText().toString().trim())
                        .addBodyParameter("user", txtUsuario_RegisterUser.getText().toString().trim())
                        .addBodyParameter("clave", txtClave_RegisterUser.getText().toString().trim())
                        .addBodyParameter("idtypeuser", String.valueOf(iTipoUsuario))
                        .addBodyParameter("idtypedocument", String.valueOf(iTipoDocumento))
                        .setTag(getString(R.string.tagMechanical))
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                mProgressDialog.dismiss();
                                try {
                                    if (response.has("message")) {
                                        FunctionsGeneral.showMessageErrorUser(RegisterUserActivity.this, response.getString("message"));
                                        return;
                                    }

                                    for (TypeUser u : arrayTipoUsusarios) {
                                        u.save();
                                    }

                                    for (TypeDocument d : arrayTipoDocumentos) {
                                        d.save();
                                    }

                                    /*GUARDAMOS EL USUARIO*/
                                    JSONObject usuarioResponnse = response.getJSONObject("user");

                                    User user = new User();
                                    user.setIduser(Integer.parseInt(usuarioResponnse.getString("iduser")))
                                            .setName(usuarioResponnse.getString("name"))
                                            .setPassword(usuarioResponnse.getString("password"))
                                            .setIdtypeuser(Integer.parseInt(usuarioResponnse.getString("idtypeuser"))).save();


                                    /*GUARDAMOS LOS DISTRITOS*/
                                    JSONArray distritosResponde = response.getJSONArray("district");
                                    District district = null;
                                    for (int i = 0; i < distritosResponde.length(); i++) {
                                        district = new District();
                                        district.setIddistrict(Integer.parseInt(distritosResponde.getJSONObject(i).getString("iddistrict")))
                                                .setName(distritosResponde.getJSONObject(i).getString("name"))
                                                .save();
                                    }

                                    Intent intent = null;

                                    if (response.has("customer")) {
                                        intent = new Intent(RegisterUserActivity.this, EditCustomerActivity.class);

                                        JSONObject customerResponde = response.getJSONObject("customer");

                                        Customer customer = new Customer();
                                        customer.setIdcustomer(Integer.parseInt(customerResponde.getString("idcustomer")))
                                                .setItypedocument(Integer.parseInt(customerResponde.getString("itypedocument")))
                                                .setNrodocumento(customerResponde.getString("nrodocument"))
                                                .setIduser(Integer.parseInt(customerResponde.getString("iduser")))
                                                .save();
                                    } else {
                                        intent = new Intent(RegisterUserActivity.this, MainProviderActivity.class);

                                        JSONObject providerResponde = response.getJSONObject("provider");

                                        Provider provider = new Provider();
                                        provider.setIdprovider(Integer.parseInt(providerResponde.getString("idprovider")))
                                                .setItypedocument(Integer.parseInt(providerResponde.getString("itypedocument")))
                                                .setNrodocument(providerResponde.getString("nrodocument"))
                                                .setIduser(Integer.parseInt(providerResponde.getString("iduser")))
                                                .save();
                                    }

                                    SharedPreferences mPrefs = getSharedPreferences(getString(R.string.keypreference), MODE_PRIVATE); //add key
                                    SharedPreferences.Editor editor = mPrefs.edit();
                                    editor.putString("token", response.getString("token"));
                                    editor.apply();


                                    /*PARA QUE NO SE REGRESE AL LOGIN*/
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                mProgressDialog.dismiss();
                                FunctionsGeneral.showMessageErrorUser(RegisterUserActivity.this, "Error!!!");
                            }
                        });


            }
        });
    }

    public void getTypeDocumens() {
        AndroidNetworking.get(MechanicalApi.getTypesDocuments())
                .setTag(getString(R.string.tagMechanical))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray lista = response.getJSONArray("response");
                            String[] documentos = new String[lista.length() + 1];

                            documentos[0] = "(SELECCIONE)";

                            TypeDocument item = null;
                            for (int i = 0; i < lista.length(); i++) {
                                item = new TypeDocument();
                                item.setIdtypedocument(Integer.parseInt(lista.getJSONObject(i).getString("idtypedocument")));
                                item.setDescription(lista.getJSONObject(i).getString("description"));
                                documentos[i + 1] = item.getDescription();
                                arrayTipoDocumentos.add(item);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterUserActivity.this, android.R.layout.simple_spinner_item, documentos);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cboTipoDocumento_RegisterUser.setAdapter(adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        FunctionsGeneral.showMessageErrorUser(RegisterUserActivity.this, "Error!!!");
                    }
                });
    }

    public void getTypeUers() {
        AndroidNetworking.get(MechanicalApi.getTypesUers())
                .setTag(getString(R.string.tagMechanical))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray lista = response.getJSONArray("response");
                            String[] usuarios = new String[lista.length() + 1];

                            usuarios[0] = "(SELECCIONE)";

                            TypeUser item = null;
                            for (int i = 0; i < lista.length(); i++) {
                                item = new TypeUser();

                                item.setIdtypeuser(Integer.parseInt(lista.getJSONObject(i).getString("idtypeuser")));
                                item.setName(lista.getJSONObject(i).getString("name"));
                                item.setDescription(lista.getJSONObject(i).getString("description"));

                                usuarios[i + 1] = item.getName();
                                arrayTipoUsusarios.add(item);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterUserActivity.this, android.R.layout.simple_spinner_item, usuarios);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cboTipoUsuario_RegisterUser.setAdapter(adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        FunctionsGeneral.showMessageErrorUser(RegisterUserActivity.this, "Error!!!");
                    }
                });

    }
}
