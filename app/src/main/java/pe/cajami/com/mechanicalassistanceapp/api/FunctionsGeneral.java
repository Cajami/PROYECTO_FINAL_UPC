package pe.cajami.com.mechanicalassistanceapp.api;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import pe.cajami.com.mechanicalassistanceapp.R;

public class FunctionsGeneral {

    public static void showMessageToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showMessageErrorUser(Context context, String mensaje) {
        MostrarAlertDialog(context, "(ERROR)", mensaje, R.drawable.ic_error_outline_black_24dp);
    }

    public static void showMessageAlertUser(Context context, String title, String message, DialogInterface.OnClickListener respuestaAceptar) {
        if (respuestaAceptar == null) {
            respuestaAceptar = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            };
        }
        MostrarAlertDialog(context, title, message, R.drawable.info, "Aceptar", "", respuestaAceptar, null);
    }

    public static void showMessageConfirmationUser(Context context, String message,
                                                   DialogInterface.OnClickListener respuestaOK,
                                                   DialogInterface.OnClickListener respuestaCancelar) {

        if (respuestaCancelar == null) {
            respuestaCancelar = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            };
        }
        MostrarAlertDialog(context, "¿Está seguro?", message, R.drawable.question, "SI", "NO", respuestaOK, respuestaCancelar);
    }

    private static void MostrarAlertDialog(Context context, String title, String message, int icon,
                                           String positiveButton, String cancelButton,
                                           DialogInterface.OnClickListener respuestaOK,
                                           DialogInterface.OnClickListener respuestaNO) {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(Html.fromHtml(message))
                .setIcon(icon)
                .setCancelable(false)
                .setPositiveButton(positiveButton, respuestaOK);

        if (respuestaNO != null)
            dialogo.setNegativeButton(cancelButton, respuestaNO);

        dialogo.show();
    }

    private static void MostrarAlertDialog(
            Context context,
            String titulo,
            String mensaje,
            int icono) {
        new AlertDialog.Builder(context)
                .setMessage(Html.fromHtml(mensaje))
                .setIcon(icono)
                .setTitle(titulo)
                .setCancelable(false)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();

    }

    public static AddressLocation getAddrees(Context context, double latitude,double longitude){
        AddressLocation address = new AddressLocation();
        Geocoder geocoder;
        List<android.location.Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address.setAddress(addresses.get(0).getAddressLine(0)); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            address.setCity(addresses.get(0).getLocality());
            address.setState(addresses.get(0).getAdminArea());
            address.setCountry(addresses.get(0).getCountryName());
            address.setPostalCode(addresses.get(0).getPostalCode());
            address.setKnownName(addresses.get(0).getFeatureName()); // Only if available else return NULL
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

}
