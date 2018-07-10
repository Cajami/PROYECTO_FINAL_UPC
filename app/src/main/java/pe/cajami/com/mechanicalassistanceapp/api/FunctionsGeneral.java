package pe.cajami.com.mechanicalassistanceapp.api;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.widget.Toast;

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

}
