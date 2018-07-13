package pe.cajami.com.mechanicalassistanceapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.activities.EmergencyPostulateActivity;
import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.models.Request;

public class AdapterEmergencyFree extends RecyclerView.Adapter<AdapterEmergencyFree.ViewHolder> {

    List<Request> requests;

    public AdapterEmergencyFree(List<Request> requests) {
        this.requests = requests;
    }

    public AdapterEmergencyFree setRequests(List<Request> requests) {
        this.requests = requests;
        return this;
    }

    @NonNull
    @Override
    public AdapterEmergencyFree.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_emergency_free, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEmergencyFree.ViewHolder viewHolder, int i) {
        Request article = requests.get(i);
        viewHolder.updateViewFrom(article, i);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView lblDistrito, lblTipoEmergencia, lblDetalle, lblNumeracion;
        private CardView emergencyCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblDistrito = (TextView) itemView.findViewById(R.id.lblDistrito);
            lblTipoEmergencia = (TextView) itemView.findViewById(R.id.lblTipoEmergencia);
            lblDetalle = (TextView) itemView.findViewById(R.id.lblDetalle);
            lblNumeracion = (TextView) itemView.findViewById(R.id.lblNumeracion);
            emergencyCardView = (CardView) itemView.findViewById(R.id.emergencyCardView);
        }

        public void updateViewFrom(final Request request, int position) {
            lblDistrito.setText(request.getDistrict());
            lblTipoEmergencia.setText(request.getDescription());
            lblDetalle.setText(request.getDetails());
            lblNumeracion.setText(String.format("%03d", (position + 1)));

            emergencyCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, EmergencyPostulateActivity.class);
                    intent.putExtras(request.toBundle());
                    context.startActivity(intent);
                }
            });
        }
    }
}
