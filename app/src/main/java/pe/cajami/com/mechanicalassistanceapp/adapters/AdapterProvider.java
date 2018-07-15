package pe.cajami.com.mechanicalassistanceapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import pe.cajami.com.mechanicalassistanceapp.R;
import pe.cajami.com.mechanicalassistanceapp.models.Provider;

public class AdapterProvider extends RecyclerView.Adapter<AdapterProvider.ViewHolder> {

    List<Provider> providers;

    public AdapterProvider(List<Provider> providers) {
        this.providers = providers;
    }

    public AdapterProvider setProviders(List<Provider> providers) {
        this.providers = providers;
        return this;
    }

    @NonNull
    @Override
    public AdapterProvider.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AdapterProvider.ViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_list_provider, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProvider.ViewHolder viewHolder, int i) {
        Provider article = providers.get(i);
        viewHolder.updateViewFrom(article, i);
    }

    @Override
    public int getItemCount() {
        return providers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblNumeracion, lblTipoDocumento, lblNroDocumento, lblNameProveedor, lblPuntuacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblNumeracion = (TextView) itemView.findViewById(R.id.lblNumeracion);
            lblTipoDocumento = (TextView) itemView.findViewById(R.id.lblTipoDocumento);
            lblNroDocumento = (TextView) itemView.findViewById(R.id.lblNroDocumento);
            lblNameProveedor = (TextView) itemView.findViewById(R.id.lblNameProveedor);
            lblPuntuacion = (TextView) itemView.findViewById(R.id.lblPuntuacion);
        }

        public void updateViewFrom(Provider provider, int position) {
            lblNumeracion.setText(String.format("%03d", (position + 1)));
            lblTipoDocumento.setText(provider.getDescriptionTypeDocument());
            lblNroDocumento.setText(provider.getNrodocument());
            lblNameProveedor.setText(provider.getName());
            lblPuntuacion.setText(String.valueOf(provider.getScore()));
        }
    }
}
