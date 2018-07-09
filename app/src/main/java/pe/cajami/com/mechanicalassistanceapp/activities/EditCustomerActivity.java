package pe.cajami.com.mechanicalassistanceapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        getTypeDocumens();
        getDistritos();

        Customer customer = Customer.listAll(Customer.class).get(0);

        txtNroDocumento_EditCustomer.setText(customer.getNrodocumento());
        txtNombre_EditCustomer.setText(customer.getName());
        txtDireccion_EditCustomer.setText(customer.getAddress());
        txtTelefono_EditCustomer.setText(customer.getPhone());
        txtEmail_EditCustomer.setText(customer.getEmail());
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


}
