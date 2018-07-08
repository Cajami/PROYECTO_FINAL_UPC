package pe.cajami.com.mechanicalassistanceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pe.cajami.com.mechanicalassistanceapp.activities.MainClientActivity;
import pe.cajami.com.mechanicalassistanceapp.activities.RegisterUserActivity;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;

public class LoginActivity extends AppCompatActivity {

    TextView lblRegistrarse_Login;
    EditText txtUsuario_Login,txtClave_Login;
    Button btnIngresar_Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lblRegistrarse_Login = (TextView) findViewById(R.id.lblRegistrarse_Login);
        txtUsuario_Login = (EditText) findViewById(R.id.txtUsuario_Login);
        txtClave_Login = (EditText) findViewById(R.id.txtClave_Login);
        btnIngresar_Login = (Button) findViewById(R.id.btnIngresar_Login);

        lblRegistrarse_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });

        btnIngresar_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtUsuario_Login.getText().toString().length()==0){
                    FunctionsGeneral.showMessageToast(LoginActivity.this,"Debe in gresar un Usuario");
                    return;
                }

                if (txtClave_Login.getText().toString().length()==0){
                    FunctionsGeneral.showMessageToast(LoginActivity.this,"Debe in gresar una Clave");
                    return;
                }

                Intent intent = new Intent(LoginActivity.this, MainClientActivity.class);
                startActivity(intent);
            }
        });

    }
}
