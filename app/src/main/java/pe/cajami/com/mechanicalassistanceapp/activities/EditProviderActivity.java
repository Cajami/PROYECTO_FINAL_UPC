package pe.cajami.com.mechanicalassistanceapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
import pe.cajami.com.mechanicalassistanceapp.models.District;
import pe.cajami.com.mechanicalassistanceapp.models.Provider;
import pe.cajami.com.mechanicalassistanceapp.models.TypeDocument;

public class EditProviderActivity extends AppCompatActivity {

    Spinner cboTipoDocumento_EditProvider, cboDistrito_EditProvider;
    EditText txtNroDocumento_EditProvider, txtNombre_EditProvider, txtDireccion_EditProvider, txtContacto_EditProvider, txtTelefono_EditProvider, txtEmail_EditProvider, txtWeb_EditProvider, txtLongitud_EditProvider, txtLatitud_EditProvider;
    ImageView btnRefrescar_EditProvider;
    Button btnGuardarModificaion_EditProviver;

    List<TypeDocument> arrayTipoDocumentos = new ArrayList<>();
    List<District> arrayDistritos = new ArrayList<>();
    String token = "";

    Provider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_provider);

        cboTipoDocumento_EditProvider = (Spinner) findViewById(R.id.cboTipoDocumento_EditProvider);
        cboDistrito_EditProvider = (Spinner) findViewById(R.id.cboDistrito_EditProvider);
        txtNroDocumento_EditProvider = (EditText) findViewById(R.id.txtNroDocumento_EditProvider);
        txtNombre_EditProvider = (EditText) findViewById(R.id.txtNombre_EditProvider);
        txtDireccion_EditProvider = (EditText) findViewById(R.id.txtDireccion_EditProvider);
        txtContacto_EditProvider = (EditText) findViewById(R.id.txtContacto_EditProvider);
        txtTelefono_EditProvider = (EditText) findViewById(R.id.txtTelefono_EditProvider);
        txtEmail_EditProvider = (EditText) findViewById(R.id.txtEmail_EditProvider);
        txtWeb_EditProvider = (EditText) findViewById(R.id.txtWeb_EditProvider);
        txtLongitud_EditProvider = (EditText) findViewById(R.id.txtLongitud_EditProvider);
        txtLatitud_EditProvider = (EditText) findViewById(R.id.txtLatitud_EditProvider);

        btnRefrescar_EditProvider = (ImageView) findViewById(R.id.btnRefrescar_EditProvider);
        btnGuardarModificaion_EditProviver = (Button) findViewById(R.id.btnGuardarModificaion_EditProviver);

        btnGuardarModificaion_EditProviver.setOnClickListener(btnGuardar_OnClickListener);

        SharedPreferences mPrefs = getSharedPreferences(getString(R.string.keypreference), MODE_PRIVATE); //add key
        token = mPrefs.getString("token", "");

        provider = Provider.listAll(Provider.class).get(0);

        txtNroDocumento_EditProvider.setText(provider.getNrodocument());
        txtNombre_EditProvider.setText(provider.getName());
        txtDireccion_EditProvider.setText(provider.getAddress());
        txtContacto_EditProvider.setText(provider.getContact());
        txtTelefono_EditProvider.setText(provider.getPhone());
        txtEmail_EditProvider.setText(provider.getEmail());
        txtWeb_EditProvider.setText(provider.getWeb());
        txtLongitud_EditProvider.setText(String.valueOf(provider.getLongitude()));
        txtLatitud_EditProvider.setText(String.valueOf(provider.getLatitude()));

        getTypeDocumens();
        getDistritos();
    }

    public void isStack() {
        if (isTaskRoot()) {
            //SI ES LA ULTIA ACTIVIDAD
            Intent intent = new Intent(EditProviderActivity.this, MainProviderActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        isStack();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        isStack();
        finish();
        return true;
    }

    public void getDistritos() {
        arrayDistritos = District.listAll(District.class);
        if (arrayDistritos.size() > 0) {
            String[] documentos = new String[arrayDistritos.size()];
            int position = -1;
            for (int i = 0; i < arrayDistritos.size(); i++) {
                if (arrayDistritos.get(i).getIddistrict() == provider.getIddistrict())
                    position = i;
                documentos[i] = arrayDistritos.get(i).getName();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProviderActivity.this, android.R.layout.simple_spinner_item, documentos);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboDistrito_EditProvider.setAdapter(adapter);

            if (position != -1)
                cboDistrito_EditProvider.setSelection(position);
        } else {
            AndroidNetworking.get(MechanicalApi.getDistrict())
                    .setTag(getString(R.string.tagMechanical))
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray distritosResponde = response.getJSONArray("response");
                                String[] distritos = new String[distritosResponde.length()];

                                District district = null;
                                for (int i = 0; i < distritosResponde.length(); i++) {
                                    district = new District();
                                    district.setIddistrict(Integer.parseInt(distritosResponde.getJSONObject(i).getString("iddistrict")))
                                            .setName(distritosResponde.getJSONObject(i).getString("name"))
                                            .save();
                                }
                                getDistritos();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Error!!!");
                        }
                    });
        }
    }

    public void getTypeDocumens() {
        arrayTipoDocumentos = TypeDocument.listAll(TypeDocument.class);
        if (arrayTipoDocumentos.size() > 0) {
            String[] documentos = new String[arrayTipoDocumentos.size()];
            int position = -1;
            for (int i = 0; i < arrayTipoDocumentos.size(); i++) {
                if (arrayTipoDocumentos.get(i).getIdtypedocument() == provider.getItypedocument())
                    position = i;
                documentos[i] = arrayTipoDocumentos.get(i).getDescription();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProviderActivity.this, android.R.layout.simple_spinner_item, documentos);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboTipoDocumento_EditProvider.setAdapter(adapter);

            if (position != -1)
                cboTipoDocumento_EditProvider.setSelection(position);
        } else {
            AndroidNetworking.get(MechanicalApi.getTypesDocuments())
                    .setTag(getString(R.string.tagMechanical))
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray lista = response.getJSONArray("response");
                                String[] documentos = new String[lista.length()];

                                TypeDocument item = null;
                                for (int i = 0; i < lista.length(); i++) {
                                    item = new TypeDocument();
                                    item.setIdtypedocument(Integer.parseInt(lista.getJSONObject(i).getString("idtypedocument")));
                                    item.setDescription(lista.getJSONObject(i).getString("description"));
                                    documentos[i] = item.getDescription();
                                    item.save();
                                }

                                getTypeDocumens();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Error!!!");
                        }
                    });
        }
    }

    View.OnClickListener btnGuardar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (txtNroDocumento_EditProvider.getText().toString().length() == 0) {
                FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Ingrese Nro de Documento");
                return;
            }

            if (txtNombre_EditProvider.getText().toString().length() == 0) {
                FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Ingrese Nombre /  Razón Social");
                return;
            }

            if (txtNroDocumento_EditProvider.getText().toString().length() == 0) {
                FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Ingrese Nro de Documento");
                return;
            }

            if (txtDireccion_EditProvider.getText().toString().length() == 0) {
                FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Ingrese Dirección");
                return;
            }

            if (txtNroDocumento_EditProvider.getText().toString().length() == 0) {
                FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Ingrese Nro de Documento");
                return;
            }

            if (txtContacto_EditProvider.getText().toString().length() == 0) {
                FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Ingrese Contacto");
                return;
            }

            if (txtTelefono_EditProvider.getText().toString().length() == 0) {
                FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Ingrese Teléfono");
                return;
            }

            if (txtNroDocumento_EditProvider.getText().toString().length() == 0) {
                FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Ingrese Nro de Documento");
                return;
            }

            provider.setItypedocument(arrayTipoDocumentos.get(cboTipoDocumento_EditProvider.getSelectedItemPosition()).getIdtypedocument());
            provider.setIddistrict(arrayDistritos.get(cboDistrito_EditProvider.getSelectedItemPosition()).getIddistrict());
            provider.setNrodocument(txtNroDocumento_EditProvider.getText().toString().trim());
            provider.setName(txtNombre_EditProvider.getText().toString().trim());
            provider.setAddress(txtDireccion_EditProvider.getText().toString().trim());
            provider.setContact(txtContacto_EditProvider.getText().toString().trim());
            provider.setPhone(txtTelefono_EditProvider.getText().toString().trim());
            provider.setEmail(txtEmail_EditProvider.getText().toString().trim());
            provider.setWeb(txtWeb_EditProvider.getText().toString().trim());
            provider.setLongitude(Double.parseDouble(txtLongitud_EditProvider.getText().toString()));
            provider.setLatitude(Double.parseDouble(txtLatitud_EditProvider.getText().toString()));


            final ProgressDialog mProgressDialog = new ProgressDialog(EditProviderActivity.this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage("Guardando...");
            mProgressDialog.show();

            AndroidNetworking.post(MechanicalApi.editProvider())
                    .addBodyParameter("idprovider", String.valueOf(provider.getIdprovider()))
                    .addBodyParameter("itypedocument", String.valueOf(provider.getItypedocument()))
                    .addBodyParameter("nrodocument", provider.getNrodocument())
                    .addBodyParameter("name", provider.getName())
                    .addBodyParameter("address", provider.getAddress())
                    .addBodyParameter("iddistrict", String.valueOf(provider.getIddistrict()))
                    .addBodyParameter("phone", provider.getPhone())
                    .addBodyParameter("email", provider.getEmail())
                    .addBodyParameter("contact", provider.getContact())
                    .addBodyParameter("web", provider.getWeb())
                    .addBodyParameter("longitude",String.valueOf(provider.getLongitude()))
                    .addBodyParameter("latitude", String.valueOf(provider.getLatitude()))
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
                                    FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, response.getString("message"));
                                } else {
                                    provider.save();
                                    FunctionsGeneral.showMessageToast(EditProviderActivity.this, "Se guardaron los datos");
                                }
                                isStack();
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
                                    FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Error!!!");
                                else
                                    FunctionsGeneral.showMessageErrorUser(EditProviderActivity.this, "Problemas con el servicio");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    };
}
