package pe.cajami.com.mechanicalassistanceapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import pe.cajami.com.mechanicalassistanceapp.R;

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
    }
}
