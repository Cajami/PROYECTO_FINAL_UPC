package pe.cajami.com.mechanicalassistanceapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pe.cajami.com.mechanicalassistanceapp.R;

public class AdapterEmergencyFree extends RecyclerView.Adapter<AdapterEmergencyFree.ViewHolder>{

    @NonNull
    @Override
    public AdapterEmergencyFree.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEmergencyFree.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView lblDistrito,lblTipoEmergencia,lblDetalle,lblNumeracion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblDistrito = (TextView) itemView.findViewById(R.id.lblDistrito);
            lblTipoEmergencia = (TextView) itemView.findViewById(R.id.lblTipoEmergencia);
            lblDetalle = (TextView) itemView.findViewById(R.id.lblDetalle);
            lblNumeracion = (TextView) itemView.findViewById(R.id.lblNumeracion);
        }
    }
}
