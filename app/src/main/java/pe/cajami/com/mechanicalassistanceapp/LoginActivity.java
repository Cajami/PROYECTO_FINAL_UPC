package pe.cajami.com.mechanicalassistanceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import pe.cajami.com.mechanicalassistanceapp.activities.MainClientActivity;
import pe.cajami.com.mechanicalassistanceapp.activities.RegisterUserActivity;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.MechanicalApi;

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

                final ProgressDialog mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setMessage("Conectanose...");
                mProgressDialog.show();

                AndroidNetworking.post(MechanicalApi.loginApi())
                        .addBodyParameter("user", txtUsuario_Login.getText().toString().trim())
                        .addBodyParameter("clave", txtClave_Login.getText().toString().trim())
                        .setTag(getString(R.string.tagMechanical))
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                mProgressDialog.dismiss();

                                try{
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("token",response.getString("token"));
                                    editor.apply();

                                    Intent intent = new Intent(LoginActivity.this, MainClientActivity.class);
                                    startActivity(intent);
                                    finish();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                mProgressDialog.dismiss();
                                try{
                                    if (error.getErrorCode()==400)
                                        FunctionsGeneral.showMessageErrorUser(LoginActivity.this,"Usuario o Clave incorrectos");
                                    else
                                        FunctionsGeneral.showMessageErrorUser(LoginActivity.this,"Problemas para conectarnos con el servicio");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });

    }
}
