package pe.cajami.com.mechanicalassistanceapp.activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Customer;
import pe.cajami.com.mechanicalassistanceapp.models.District;
import pe.cajami.com.mechanicalassistanceapp.models.TypeDocument;

public class EditCustomerActivity extends AppCompatActivity {

    Spinner cboTipoDocumento_EditCustomer, cboDistrito_EditCustomer;
    EditText txtNroDocumento_EditCustomer, txtNombre_EditCustomer, txtDireccion_EditCustomer, txtTelefono_EditCustomer, txtEmail_EditCustomer;
    Button btnGuardarModificaion_EditCustomer;

    List<TypeDocument> arrayTipoDocumentos = new ArrayList<>();
    List<District> arrayDistritos = new ArrayList<>();

    Customer customer;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        cboTipoDocumento_EditCustomer = (Spinner) findViewById(R.id.cboTipoDocumento_EditCustomer);
        cboDistrito_EditCustomer = (Spinner) findViewById(R.id.cboDistrito_EditCustomer);

        txtNroDocumento_EditCustomer = (EditText) findViewById(R.id.txtNroDocumento_EditCustomer);
        txtNombre_EditCustomer = (EditText) findViewById(R.id.txtNombre_EditCustomer);
        txtDireccion_EditCustomer = (EditText) findViewById(R.id.txtDireccion_EditCustomer);
        txtTelefono_EditCustomer = (EditText) findViewById(R.id.txtTelefono_EditCustomer);
        txtEmail_EditCustomer = (EditText) findViewById(R.id.txtEmail_EditCustomer);

        btnGuardarModificaion_EditCustomer = (Button) findViewById(R.id.btnGuardarModificaion_EditCustomer);

        SharedPreferences mPrefs = getSharedPreferences(getString(R.string.keypreference), MODE_PRIVATE); //add key

        token = mPrefs.getString("token", "");

        btnGuardarModificaion_EditCustomer.setOnClickListener(btnGuardarModificacion_OnClickListener);

        getTypeDocumens();
        getDistritos();

        customer = Customer.listAll(Customer.class).get(0);

        txtNroDocumento_EditCustomer.setText(customer.getNrodocumento());
        txtNombre_EditCustomer.setText(customer.getName());
        txtDireccion_EditCustomer.setText(customer.getAddress());
        txtTelefono_EditCustomer.setText(customer.getPhone());
        txtEmail_EditCustomer.setText(customer.getEmail());
    }

    public void isStack(){
        if (isTaskRoot()) {
            //SI ES LA ULTIA ACTIVIDAD
            Intent intent = new Intent(EditCustomerActivity.this, MainCustomerActivity.class);
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
    public boolean onSupportNavigateUp(){
        isStack();
        finish();
        return true;
    }


    public void getDistritos() {
        arrayDistritos = District.listAll(District.class);
        if (arrayDistritos.size() > 0) {
            String[] documentos = new String[arrayDistritos.size()];

            for (int i = 0; i < arrayDistritos.size(); i++) {
                documentos[i] = arrayDistritos.get(i).getName();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditCustomerActivity.this, android.R.layout.simple_spinner_item, documentos);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboDistrito_EditCustomer.setAdapter(adapter);
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
                            FunctionsGeneral.showMessageErrorUser(EditCustomerActivity.this, "Error!!!");
                        }
                    });
        }
    }

    public void getTypeDocumens() {
        arrayTipoDocumentos = TypeDocument.listAll(TypeDocument.class);
        if (arrayTipoDocumentos.size() > 0) {
            String[] documentos = new String[arrayTipoDocumentos.size()];
            for (int i = 0; i < arrayTipoDocumentos.size(); i++) {
                documentos[i] = arrayTipoDocumentos.get(i).getDescription();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditCustomerActivity.this, android.R.layout.simple_spinner_item, documentos);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboTipoDocumento_EditCustomer.setAdapter(adapter);
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
                            FunctionsGeneral.showMessageErrorUser(EditCustomerActivity.this, "Error!!!");
                        }
                    });
        }
    }

    View.OnClickListener btnGuardarModificacion_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (arrayTipoDocumentos.size() == 0) {
                FunctionsGeneral.showMessageErrorUser(EditCustomerActivity.this, "Tipo de Documentos incompletos");
                return;
            }

            if (arrayDistritos.size() == 0) {
                FunctionsGeneral.showMessageErrorUser(EditCustomerActivity.this, "Disttritos incompletos");
                return;
            }

            if (txtNroDocumento_EditCustomer.getText().toString().trim().length() == 0) {
                FunctionsGeneral.showMessageToast(EditCustomerActivity.this, "Ingrese Nro de Documento");
                return;
            }

            if (txtNombre_EditCustomer.getText().toString().trim().length() == 0) {
                FunctionsGeneral.showMessageToast(EditCustomerActivity.this, "Ingrese un Nombre");
                return;
            }

            customer.setItypedocument(arrayTipoDocumentos.get(cboTipoDocumento_EditCustomer.getSelectedItemPosition()).getIdtypedocument());
            customer.setIddistrict(arrayDistritos.get(cboDistrito_EditCustomer.getSelectedItemPosition()).getIddistrict());
            customer.setNrodocumento(txtNroDocumento_EditCustomer.getText().toString().trim());
            customer.setName(txtNombre_EditCustomer.getText().toString().trim());
            customer.setAddress(txtDireccion_EditCustomer.getText().toString().trim());
            customer.setPhone(txtTelefono_EditCustomer.getText().toString().trim());
            customer.setEmail(txtEmail_EditCustomer.getText().toString().trim());


            final ProgressDialog mProgressDialog = new ProgressDialog(EditCustomerActivity.this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage("Guardando...");
            mProgressDialog.show();

            AndroidNetworking.post(MechanicalApi.editCustomer())
                    .addBodyParameter("idcustomer", String.valueOf(customer.getIdcustomer()))
                    .addBodyParameter("itypedocument", String.valueOf(customer.getItypedocument()))
                    .addBodyParameter("nrodocument", customer.getNrodocumento())
                    .addBodyParameter("name", customer.getName())
                    .addBodyParameter("address", customer.getAddress())
                    .addBodyParameter("iddistrict", String.valueOf(customer.getIddistrict()))
                    .addBodyParameter("phone", customer.getPhone())
                    .addBodyParameter("email", customer.getEmail())
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
                                    FunctionsGeneral.showMessageErrorUser(EditCustomerActivity.this, response.getString("message"));
                                } else {
                                    customer.save();
                                    FunctionsGeneral.showMessageToast(EditCustomerActivity.this, "Se guardaron los datos");
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
                                    FunctionsGeneral.showMessageErrorUser(EditCustomerActivity.this, "Error!!!");
                                else
                                    FunctionsGeneral.showMessageErrorUser(EditCustomerActivity.this, "Problemas con el servicio");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }
    };

}
