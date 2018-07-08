package pe.cajami.com.mechanicalassistanceapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;

public class RegisterUserActivity extends AppCompatActivity {

    Spinner cboTipoUsuario_RegisterUser, cboTipoDocumento_RegisterUser;
    EditText txtNroDocumento_RegisterUser, txtUsuario_RegisterUser, txtClave_RegisterUser, txtClaveRepetir_RegisterUser;
    Button btnRegistrar_RegisterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        cboTipoUsuario_RegisterUser = (Spinner) findViewById(R.id.cboTipoUsuario_RegisterUser);
        cboTipoDocumento_RegisterUser = (Spinner) findViewById(R.id.cboTipoDocumento_RegisterUser);
        txtNroDocumento_RegisterUser = (EditText) findViewById(R.id.txtNroDocumento_RegisterUser);
        txtUsuario_RegisterUser = (EditText) findViewById(R.id.txtUsuario_RegisterUser);
        txtClave_RegisterUser = (EditText) findViewById(R.id.txtClave_RegisterUser);
        txtClaveRepetir_RegisterUser = (EditText) findViewById(R.id.txtClaveRepetir_RegisterUser);

        btnRegistrar_RegisterUser = (Button) findViewById(R.id.btnRegistrar_RegisterUser);

        btnRegistrar_RegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iTipoUsuario = 0;
                int iTipoDocumento = 0;

                if (cboTipoUsuario_RegisterUser.getSelectedItem().toString().equalsIgnoreCase("CLIENTE"))
                    iTipoUsuario = 1;
                else
                    iTipoUsuario = 2;

                if (cboTipoDocumento_RegisterUser.getSelectedItem().toString().equalsIgnoreCase("DNI"))
                    iTipoDocumento = 1;
                else
                    iTipoDocumento = 2;

                if (txtNroDocumento_RegisterUser.getText().toString().length() == 0) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Debe ingresar Nro de Documento");
                    return;
                }

                if (txtUsuario_RegisterUser.getText().toString().length() == 0) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Debe ingresar un Usuario");
                    return;
                }


                if (txtClave_RegisterUser.getText().toString().length() == 0) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Debe ingresar una clave");
                    return;
                }

                if (txtClaveRepetir_RegisterUser.getText().toString().length() == 0) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Debe repetir la clave");
                    return;
                }

                if (!txtClave_RegisterUser.getText().toString().equalsIgnoreCase(txtClave_RegisterUser.getText().toString())) {
                    FunctionsGeneral.showMessageToast(RegisterUserActivity.this, "Las claves deben ser iguales");
                    return;
                }

                btnRegistrar_RegisterUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RegisterUserActivity.this,MainClientActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }
}
