package pe.cajami.com.mechanicalassistanceapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pe.cajami.com.mechanicalassistanceapp.LoginActivity;
import pe.cajami.com.mechanicalassistanceapp.R;

public class MainProviderActivity extends AppCompatActivity {

    Button btnEmergenciasXCotizar,btnEmergenciaAsignada,btnVerHistorialEmergencias, btnEditarProveedor;
    TextView lblCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_provider);

        btnEmergenciasXCotizar = (Button) findViewById(R.id.btnEmergenciasXCotizar);
        btnEmergenciaAsignada = (Button) findViewById(R.id.btnEmergenciaAsignada);
        btnVerHistorialEmergencias = (Button) findViewById(R.id.btnVerHistorialEmergencias);
        btnEditarProveedor = (Button) findViewById(R.id.btnEditarProveedor);
        lblCerrarSesion = (TextView) findViewById(R.id.lblCerrarSesion);

        btnEditarProveedor.setOnClickListener(btnEditarProveedor_OnClickListener);
        btnEmergenciasXCotizar.setOnClickListener(btnEmergenciasXCotizarOnClickListener);

        lblCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               SharedPreferences sharedPreferences  = getApplicationContext().getSharedPreferences(getString(R.string.keypreference), MODE_PRIVATE);

                sharedPreferences.edit().clear().commit();

                Intent intent = new Intent(MainProviderActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    View.OnClickListener btnEditarProveedor_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainProviderActivity.this,EditProviderActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener btnEmergenciasXCotizarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainProviderActivity.this,EmergencyFreeActivity.class);
            startActivity(intent);
        }
    };
}
