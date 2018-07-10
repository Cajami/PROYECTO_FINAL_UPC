package pe.cajami.com.mechanicalassistanceapp.activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
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

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;
import pe.cajami.com.mechanicalassistanceapp.models.Brand;
import pe.cajami.com.mechanicalassistanceapp.models.Car;
import pe.cajami.com.mechanicalassistanceapp.models.Customer;
import pe.cajami.com.mechanicalassistanceapp.models.District;
import pe.cajami.com.mechanicalassistanceapp.models.TypeDocument;

public class EditCustomerActivity extends AppCompatActivity {

    Spinner cboTipoDocumento_EditCustomer, cboDistrito_EditCustomer, cboMarca_EditCustomer;
    EditText txtNroDocumento_EditCustomer, txtNombre_EditCustomer, txtDireccion_EditCustomer, txtTelefono_EditCustomer, txtEmail_EditCustomer, txtModelo_EditCustomer, txtAnio_EditCustomer;
    Button btnGuardarModificaion_EditCustomer;

    List<TypeDocument> arrayTipoDocumentos = new ArrayList<>();
    List<District> arrayDistritos = new ArrayList<>();
    List<Brand> arrayMarcas = new ArrayList<>();

    Customer customer;
    Car car = null;
    List<Car> carCustomer;

    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        cboTipoDocumento_EditCustomer = (Spinner) findViewById(R.id.cboTipoDocumento_EditCustomer);
        cboDistrito_EditCustomer = (Spinner) findViewById(R.id.cboDistrito_EditCustomer);
        cboMarca_EditCustomer = (Spinner) findViewById(R.id.cboMarca_EditCustomer);

        txtNroDocumento_EditCustomer = (EditText) findViewById(R.id.txtNroDocumento_EditCustomer);
        txtNombre_EditCustomer = (EditText) findViewById(R.id.txtNombre_EditCustomer);
        txtDireccion_EditCustomer = (EditText) findViewById(R.id.txtDireccion_EditCustomer);
        txtTelefono_EditCustomer = (EditText) findViewById(R.id.txtTelefono_EditCustomer);
        txtEmail_EditCustomer = (EditText) findViewById(R.id.txtEmail_EditCustomer);
        txtModelo_EditCustomer = (EditText) findViewById(R.id.txtModelo_EditCustomer);
        txtAnio_EditCustomer = (EditText) findViewById(R.id.txtAnio_EditCustomer);

        btnGuardarModificaion_EditCustomer = (Button) findViewById(R.id.btnGuardarModificaion_EditCustomer);

        SharedPreferences mPrefs = getSharedPreferences(getString(R.string.keypreference), MODE_PRIVATE); //add key

        token = mPrefs.getString("token", "");

        btnGuardarModificaion_EditCustomer.setOnClickListener(btnGuardarModificacion_OnClickListener);

        customer = Customer.listAll(Customer.class).get(0);
        carCustomer = Car.listAll(Car.class);

        txtNroDocumento_EditCustomer.setText(customer.getNrodocumento());
        txtNombre_EditCustomer.setText(customer.getName());
        txtDireccion_EditCustomer.setText(customer.getAddress());
        txtTelefono_EditCustomer.setText(customer.getPhone());
        txtEmail_EditCustomer.setText(customer.getEmail());

        if (carCustomer.size() > 0) {
            txtModelo_EditCustomer.setText(carCustomer.get(0).getModel());
            txtAnio_EditCustomer.setText(String.valueOf(carCustomer.get(0).getYear()));
        }

        getTypeDocumens();
        getDistritos();
        getMarcas();
    }

    public void isStack() {
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
                if (arrayDistritos.get(i).getIddistrict() == customer.getIddistrict())
                    position = i;
                documentos[i] = arrayDistritos.get(i).getName();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditCustomerActivity.this, android.R.layout.simple_spinner_item, documentos);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboDistrito_EditCustomer.setAdapter(adapter);

            if (position != -1)
                cboDistrito_EditCustomer.setSelection(position);
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
            int position = -1;
            for (int i = 0; i < arrayTipoDocumentos.size(); i++) {
                if (arrayTipoDocumentos.get(i).getIdtypedocument() == customer.getItypedocument())
                    position = i;
                documentos[i] = arrayTipoDocumentos.get(i).getDescription();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditCustomerActivity.this, android.R.layout.simple_spinner_item, documentos);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboTipoDocumento_EditCustomer.setAdapter(adapter);

            if (position != -1)
                cboTipoDocumento_EditCustomer.setSelection(position);
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

    public void getMarcas() {
        arrayMarcas = Brand.listAll(Brand.class);
        if (arrayMarcas.size() > 0) {
            String[] marcas = new String[arrayMarcas.size()];
            int position = -1;
            for (int i = 0; i < arrayMarcas.size(); i++) {
                if (carCustomer.size() > 0)
                    if (arrayMarcas.get(i).getIdbrand() == carCustomer.get(0).getIdbrand())
                        position = i;
                marcas[i] = arrayMarcas.get(i).getName();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditCustomerActivity.this, android.R.layout.simple_spinner_item, marcas);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboMarca_EditCustomer.setAdapter(adapter);

            if (position != -1)
                cboMarca_EditCustomer.setSelection(position);
        } else {
            AndroidNetworking.get(MechanicalApi.getBrands())
                    .setTag(getString(R.string.tagMechanical))
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray lista = response.getJSONArray("response");
                                String[] marcas = new String[lista.length()];

                                Brand item = null;
                                for (int i = 0; i < lista.length(); i++) {
                                    item = new Brand();
                                    item.setIdbrand(Integer.parseInt(lista.getJSONObject(i).getString("idbrand")));
                                    item.setName(lista.getJSONObject(i).getString("name"));
                                    item.setDescription(lista.getJSONObject(i).getString("description"));
                                    marcas[i] = item.getDescription();
                                    item.save();
                                }

                                getMarcas();
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
            if (txtNroDocumento_EditCustomer.getText().toString().trim().length() == 0) {
                FunctionsGeneral.showMessageToast(EditCustomerActivity.this, "Ingrese Nro de Documento");
                return;
            }

            if (txtNombre_EditCustomer.getText().toString().trim().length() == 0) {
                FunctionsGeneral.showMessageToast(EditCustomerActivity.this, "Ingrese un Nombre");
                return;
            }

            if (txtModelo_EditCustomer.getText().toString().trim().length() == 0) {
                FunctionsGeneral.showMessageToast(EditCustomerActivity.this, "Ingrese un Modelo de su auto");
                return;
            }

            if (txtAnio_EditCustomer.getText().toString().length() != 4) {
                FunctionsGeneral.showMessageToast(EditCustomerActivity.this, "Ingrese año de fabricación de su auto");
                return;
            }

            customer.setItypedocument(arrayTipoDocumentos.get(cboTipoDocumento_EditCustomer.getSelectedItemPosition()).getIdtypedocument());
            customer.setIddistrict(arrayDistritos.get(cboDistrito_EditCustomer.getSelectedItemPosition()).getIddistrict());
            customer.setNrodocumento(txtNroDocumento_EditCustomer.getText().toString().trim());
            customer.setName(txtNombre_EditCustomer.getText().toString().trim());
            customer.setAddress(txtDireccion_EditCustomer.getText().toString().trim());
            customer.setPhone(txtTelefono_EditCustomer.getText().toString().trim());
            customer.setEmail(txtEmail_EditCustomer.getText().toString().trim());

            if (carCustomer.size() == 0) {
                car = new Car();
                car.setIdcustomer(customer.getIdcustomer());
            } else
                car = carCustomer.get(0);

            car.setIdbrand(arrayMarcas.get(cboMarca_EditCustomer.getSelectedItemPosition()).getIdbrand());
            car.setIdcustomer(customer.getIdcustomer());
            car.setModel(txtModelo_EditCustomer.getText().toString().trim());
            car.setYear(Integer.parseInt(txtAnio_EditCustomer.getText().toString().trim()));

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

                    .addBodyParameter("idcar", String.valueOf(car.getIdcar()))
                    .addBodyParameter("idbrand", String.valueOf(car.getIdbrand()))
                    .addBodyParameter("model", car.getModel())
                    .addBodyParameter("year", String.valueOf(car.getYear()))

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
                                    if (response.has("car")) {
                                        /* SE HA INSERTADO UN CARRO*/
                                        car.setIdcar(Integer.parseInt(response.getJSONObject("car").getString("idcar")));
                                    }

                                    customer.save();
                                    car.save();
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
