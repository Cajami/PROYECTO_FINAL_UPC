package pe.cajami.com.mechanicalassistanceapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.models.Customer;
import pe.cajami.com.mechanicalassistanceapp.models.Request;

public class MainCustomerActivity extends AppCompatActivity {

    Button btnEmergenciaPendiente_MainUsuer,
            btnVerHistorialEmergencias_MainUsuer,
            btnVerProveedores_MainUsuer,
            btnEditarUsuario_MainUsuer,
            btnRegistrarEmergencia_MainUsuer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);

        btnEmergenciaPendiente_MainUsuer = (Button) findViewById(R.id.btnEmergenciaPendiente_MainUsuer);
        btnVerHistorialEmergencias_MainUsuer = (Button) findViewById(R.id.btnVerHistorialEmergencias_MainUsuer);
        btnVerProveedores_MainUsuer = (Button) findViewById(R.id.btnVerProveedores_MainUsuer);
        btnEditarUsuario_MainUsuer = (Button) findViewById(R.id.btnEditarUsuario_MainUsuer);
        btnRegistrarEmergencia_MainUsuer = (Button) findViewById(R.id.btnRegistrarEmergencia_MainUsuer);

        btnEditarUsuario_MainUsuer.setOnClickListener(btnEditarUsuario_OnClickListener);
        btnEmergenciaPendiente_MainUsuer.setOnClickListener(btnEmergenciaPendienteOnClickListener);
        btnRegistrarEmergencia_MainUsuer.setOnClickListener(btnRegistrarEmergencia_OnClickListener);

        btnVerHistorialEmergencias_MainUsuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainCustomerActivity.this, EmergencyHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    View.OnClickListener btnEditarUsuario_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainCustomerActivity.this, EditCustomerActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener btnRegistrarEmergencia_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Customer customer = Customer.listAll(Customer.class).get(0);

            if (customer.getName() == null) {
                FunctionsGeneral.showMessageErrorUser(MainCustomerActivity.this, "Debe registrar sus datos antes de reportar emergencias");
                return;
            }

            List<Request> requests = Request.find(Request.class, "idstate = ? or idstate = ?", new String[]{"P", "C"});
            if (requests.size() > 0) {
                FunctionsGeneral.showMessageAlertUser(MainCustomerActivity.this, getString(R.string.tagMechanical), "Ya cuenta con una emergencia reportada", null);
            } else {

                Intent intent = new Intent(MainCustomerActivity.this, RegisterEmergencyActivity.class);
                startActivity(intent);
            }
        }
    };

    View.OnClickListener btnEmergenciaPendienteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;

            List<Request> requests = Request.find(Request.class, "idstate = ?", "P");

            if (requests.size() == 0) {
                requests = Request.find(Request.class, "idstate = ?", "C");
                if (requests.size() == 0) {
                    FunctionsGeneral.showMessageAlertUser(MainCustomerActivity.this, "Sin Datos", "No cuenta con Emergencias Pendientes Registradas", null);
                    return;
                } else
                    intent = new Intent(MainCustomerActivity.this, EmergencyMapsActivity.class);
            } else
                intent = new Intent(MainCustomerActivity.this, EmergencyPendingActivity.class);

            startActivity(intent);
        }
    };
}
