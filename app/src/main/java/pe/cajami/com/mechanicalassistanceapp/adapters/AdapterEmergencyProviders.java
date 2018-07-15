package pe.cajami.com.mechanicalassistanceapp.adapters;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;
import pe.cajami.com.mechanicalassistanceapp.models.RequestHistory;

public class AdapterEmergencyProviders extends RecyclerView.Adapter<AdapterEmergencyProviders.ViewHolder> {

    List<RequestHistory> requestHistories;
    private AdapterCallback callback;

    public interface AdapterCallback {
        void onClickCallback(RequestHistory item, int position);
    }

    public AdapterEmergencyProviders(List<RequestHistory> requestHistories, AdapterCallback adapterCallback) {
        this.requestHistories = requestHistories;
        this.callback = adapterCallback;
    }

    public AdapterEmergencyProviders setRequestHistories(List<RequestHistory> requestHistories) {
        this.requestHistories = requestHistories;
        return this;
    }

    @NonNull
    @Override
    public AdapterEmergencyProviders.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_emergency_pending, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEmergencyProviders.ViewHolder viewHolder, int i) {
        RequestHistory requestHistory = requestHistories.get(i);
        viewHolder.updateViewFrom(requestHistory, i);
    }

    @Override
    public int getItemCount() {
        return requestHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblNumeracion, lblProveedor, lblPuntuacion, lblDistancia;
        CardView emergencyCardView;
        Location locationA, locationB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblNumeracion = (TextView) itemView.findViewById(R.id.lblNumeracion);
            lblProveedor = (TextView) itemView.findViewById(R.id.lblProveedor);
            lblPuntuacion = (TextView) itemView.findViewById(R.id.lblPuntuacion);
            lblDistancia = (TextView) itemView.findViewById(R.id.lblDistancia);
            emergencyCardView = (CardView) itemView.findViewById(R.id.emergencyCardView);
        }

        public void updateViewFrom(final RequestHistory requestHistory, final int position) {
            lblNumeracion.setText(String.format("%03d", (position + 1)));
            lblProveedor.setText(requestHistory.getNameProvider());
            lblPuntuacion.setText(String.valueOf(requestHistory.getScoreProvider()));

            locationA = new Location("punto A");
            locationA.setLatitude(requestHistory.getLatitudeParent());
            locationA.setLongitude(requestHistory.getLongitudeParent());
            locationB = new Location("punto B");
            locationB.setLatitude(requestHistory.getLatitude());
            locationB.setLongitude(requestHistory.getLongitude());
            lblDistancia.setText(String.format("%.2f", locationA.distanceTo(locationB) / 1000) + " kilometros");

            emergencyCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onClickCallback(requestHistory, position);
                }
            });
        }
    }
}
