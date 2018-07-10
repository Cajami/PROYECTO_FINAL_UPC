package pe.cajami.com.mechanicalassistanceapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        SharedPreferences mPrefs = getSharedPreferences(getString(R.string.keypreference), MODE_PRIVATE); //add key
        token = mPrefs.getString("token", "");


        provider = new Provider();
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

}
