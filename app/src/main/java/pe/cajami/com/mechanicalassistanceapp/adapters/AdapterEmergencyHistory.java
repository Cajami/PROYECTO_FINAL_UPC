package pe.cajami.com.mechanicalassistanceapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.api.EmergencyHistory;
import pe.cajami.com.mechanicalassistanceapp.api.FunctionsGeneral;

public class AdapterEmergencyHistory extends RecyclerView.Adapter<AdapterEmergencyHistory.ViewHolder> {

    List<EmergencyHistory> emergencyHistories;

    public AdapterEmergencyHistory(List<EmergencyHistory> emergencyHistories) {
        this.emergencyHistories = emergencyHistories;
    }

    public AdapterEmergencyHistory setEmergencyHistories(List<EmergencyHistory> emergencyHistories) {
        this.emergencyHistories = emergencyHistories;
        return this;
    }

    @NonNull
    @Override
    public AdapterEmergencyHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_emergency_history, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEmergencyHistory.ViewHolder viewHolder, int i) {
        EmergencyHistory emergencyHistory = emergencyHistories.get(i);
        viewHolder.updateViewFrom(emergencyHistory, i);
    }

    @Override
    public int getItemCount() {
        return emergencyHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblDistrito, lblTipoEmergencia, lblProveedor, lblFechaAtencion, lblCalificacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lblDistrito = (TextView) itemView.findViewById(R.id.lblDistrito);
            lblTipoEmergencia = (TextView) itemView.findViewById(R.id.lblTipoEmergencia);
            lblProveedor = (TextView) itemView.findViewById(R.id.lblProveedor);
            lblFechaAtencion = (TextView) itemView.findViewById(R.id.lblFechaAtencion);
            lblCalificacion = (TextView) itemView.findViewById(R.id.lblCalificacion);
        }

        public void updateViewFrom(EmergencyHistory emergencyHistory, int position) {
            lblDistrito.setText(emergencyHistory.getDistrict());
            lblTipoEmergencia.setText(emergencyHistory.getDetalleFlaw());
            lblProveedor.setText(emergencyHistory.getNameProvider());
            lblFechaAtencion.setText(FunctionsGeneral.getDateToString(emergencyHistory.getDatefinish()));
            lblCalificacion.setText(String.valueOf(emergencyHistory.getScore()));
        }
    }
}
