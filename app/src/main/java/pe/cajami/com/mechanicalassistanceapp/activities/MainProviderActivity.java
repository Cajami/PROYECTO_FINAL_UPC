package pe.cajami.com.mechanicalassistanceapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pe.cajami.com.mechanicalassistanceapp.R;

public class MainProviderActivity extends AppCompatActivity {

    Button btnEmergenciasXCotizar,btnEmergenciaAsignada,btnVerHistorialEmergencias, btnEditarProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_provider);

        btnEmergenciasXCotizar = (Button) findViewById(R.id.btnEmergenciasXCotizar);
        btnEmergenciaAsignada = (Button) findViewById(R.id.btnEmergenciaAsignada);
        btnVerHistorialEmergencias = (Button) findViewById(R.id.btnVerHistorialEmergencias);
        btnEditarProveedor = (Button) findViewById(R.id.btnEditarProveedor);

        btnEditarProveedor.setOnClickListener(btnEditarProveedor_OnClickListener);
    }

    View.OnClickListener btnEditarProveedor_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainProviderActivity.this,EditProviderActivity.class);
            startActivity(intent);
        }
    };
}
