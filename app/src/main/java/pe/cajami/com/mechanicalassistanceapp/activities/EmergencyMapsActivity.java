package pe.cajami.com.mechanicalassistanceapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.api.GMapV2Direction;
import pe.cajami.com.mechanicalassistanceapp.models.Request;
import pe.cajami.com.mechanicalassistanceapp.models.RequestHistory;

public class EmergencyMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Request request;
    RequestHistory requestHistory;

    Button btnFinalizarEmergencia;
    GMapV2Direction md = new GMapV2Direction();

    static final int MAP_CONTACT_REQUEST = 1;  // The request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_maps);

       List<Request> requests = Request.find(Request.class, "idstate = ?", "C");

        if (requests.size() == 0) {
            FunctionsGeneral.showMessageAlertUser(EmergencyMapsActivity.this, getString(R.string.tagMechanical),
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
            FunctionsGeneral.showMessageAlertUser(EmergencyMapsActivity.this, getString(R.string.tagMechanical),
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


        btnFinalizarEmergencia = (Button) findViewById(R.id.btnFinalizarEmergencia);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnFinalizarEmergencia.setOnClickListener(btnFinalizarEmergenciaOnClickListener);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng requestLatLng = new LatLng(request.getLatitude(), request.getLongitude());
        mMap.addMarker(new MarkerOptions().position(requestLatLng).title("Marker in Sydney"));

        LatLng requestHistoryLatLng = new LatLng(requestHistory.getLatitude(), requestHistory.getLongitude());
        mMap.addMarker(new MarkerOptions().position(requestHistoryLatLng).title("Marker in Sydney"));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(requestHistoryLatLng, 16));

        new ClassDocumentLatLng(md.getUrl(requestLatLng, requestHistoryLatLng, GMapV2Direction.MODE_DRIVING)).execute();
    }

    View.OnClickListener btnFinalizarEmergenciaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(EmergencyMapsActivity.this,EmergencyFinishActivity.class);
            startActivityForResult(intent, MAP_CONTACT_REQUEST);
            //    setResult(RESULT_OK, intent);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == MAP_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    class ClassDocumentLatLng extends AsyncTask<String, Void, Document> {

        private String url;

        public ClassDocumentLatLng(String url) {
            this.url = url;
        }

        protected Document doInBackground(String... urls) {
            try {
                URL urlObj = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
                urlConnection.connect();

                if (urlConnection.getResponseCode() != 200) {
                    return null;
                }
                InputStream is = urlConnection.getInputStream();
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(is);
                return doc;
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(Document doc) {
            if (doc == null)
                return;

            ArrayList<LatLng> directionPoint = md.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            Polyline polylin = mMap.addPolyline(rectLine);
        }
    }
}
