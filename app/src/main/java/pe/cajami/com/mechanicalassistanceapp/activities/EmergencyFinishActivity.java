package pe.cajami.com.mechanicalassistanceapp.activities;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.models.Flaw;
import pe.cajami.com.mechanicalassistanceapp.models.Request;
import pe.cajami.com.mechanicalassistanceapp.models.RequestHistory;

public class EmergencyFinishActivity extends AppCompatActivity {

    SeekBar seekBar;
    TextView lblCalificacion, lblTipoEmergencia, lblDetalle, lblFechaRegistro;
    Request request;
    RequestHistory requestHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_finish);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        lblCalificacion = (TextView) findViewById(R.id.lblCalificacion);
        lblTipoEmergencia = (TextView) findViewById(R.id.lblTipoEmergencia);
        lblDetalle = (TextView) findViewById(R.id.lblDetalle);
        lblFechaRegistro = (TextView) findViewById(R.id.lblFechaRegistro);

        List<Request> requests = Request.find(Request.class, "idstate = ?", "C");

        if (requests.size() == 0) {
            FunctionsGeneral.showMessageAlertUser(EmergencyFinishActivity.this, getString(R.string.tagMechanical),
                    "No se encontró Emergencia en curso",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }
            );
            return;
        }
        request = requests.get(0);

        List<RequestHistory> requestHistories = Request.find(RequestHistory.class, "idstate = ? and idrequest = ?",
                new String[]{"C", String.valueOf(request.getIdrequest())});

        if (requestHistories.size() == 0) {
            FunctionsGeneral.showMessageAlertUser(EmergencyFinishActivity.this, getString(R.string.tagMechanical),
                    "No se encontró proveedor asociado a Emergencia en curso",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }
            );
            return;
        }
        requestHistory = requestHistories.get(0);

        List<Flaw> arrayFallas = Flaw.listAll(Flaw.class);

        for (int i = 0; i < arrayFallas.size(); i++) {
            if (requests.get(0).getIdflaw() == arrayFallas.get(0).getIdflaw()) {
                lblTipoEmergencia.setText(arrayFallas.get(0).getDescription());
                break;
            }
        }

        lblDetalle.setText(requests.get(0).getDetails());
        lblFechaRegistro.setText(FunctionsGeneral.getDateToString(requests.get(0).getDate()));


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i + 1;
                lblCalificacion.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lblCalificacion.setText(progress + "");
            }
        });
    }
}
