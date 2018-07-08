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
