package pe.cajami.com.mechanicalassistanceapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import pe.cajami.com.mechanicalassistanceapp.R;

public class RegisterEmergencyActivity extends AppCompatActivity {

    Spinner cboTipoEmergencia_RegisterEmergency;
    EditText txtDetalle_RegisterEmergency;
    Button btnRegistrarEmergencia_RegisterEmergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_emergency);

        cboTipoEmergencia_RegisterEmergency = (Spinner) findViewById(R.id.cboTipoEmergencia_RegisterEmergency);
        txtDetalle_RegisterEmergency = (EditText) findViewById(R.id.txtDetalle_RegisterEmergency);
        btnRegistrarEmergencia_RegisterEmergency = (Button) findViewById(R.id.btnRegistrarEmergencia_RegisterEmergency);

        btnRegistrarEmergencia_RegisterEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iTipoEmergencia = 0;

                if (cboTipoEmergencia_RegisterEmergency.getSelectedItem().toString().equalsIgnoreCase("PONCHADO LLANTA"))
                    iTipoEmergencia = 1;
                else
                    iTipoEmergencia = 2;


            }
        });

    }
}
