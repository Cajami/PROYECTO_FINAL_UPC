package pe.cajami.com.mechanicalassistanceapp.activities;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.models.Flaw;
import pe.cajami.com.mechanicalassistanceapp.models.Request;

public class EmergencyPendingActivity extends AppCompatActivity {

    List<Request> requests;
    TextView lblTipoEmergencia, lblDetalle, lblFechaRegistro;
    ImageView btnRefrescar;
    RecyclerView rvProveedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_pending);

        requests = Request.find(Request.class, "idstate = ?", "P");

        if (requests.size() == 0) {
            FunctionsGeneral.showMessageAlertUser(EmergencyPendingActivity.this, "Sn Datos", "No cuenta con Emergencias Pendientes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
        }

        List<Flaw> arrayFallas = Flaw.listAll(Flaw.class);

        lblTipoEmergencia = (TextView) findViewById(R.id.lblTipoEmergencia);
        lblDetalle = (TextView) findViewById(R.id.lblDetalle);
        lblFechaRegistro = (TextView) findViewById(R.id.lblFechaRegistro);

        btnRefrescar = (ImageView) findViewById(R.id.btnRefrescar);
        rvProveedores = (RecyclerView) findViewById(R.id.rvProveedores);

        for (int i = 0; i < arrayFallas.size(); i++) {
            if (requests.get(0).getIdflaw()==arrayFallas.get(0).getIdflaw()){
                lblTipoEmergencia.setText(arrayFallas.get(0).getDescription());
                break;
            }
        }

        lblDetalle.setText(requests.get(0).getDetails());
        lblFechaRegistro.setText(FunctionsGeneral.getDateToString(requests.get(0).getDate()));
    }
}
