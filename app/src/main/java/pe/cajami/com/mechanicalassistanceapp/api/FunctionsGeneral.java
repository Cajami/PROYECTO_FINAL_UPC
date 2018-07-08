package pe.cajami.com.mechanicalassistanceapp.api;

import android.content.Context;
import android.widget.Toast;

public class FunctionsGeneral {

    public  static void showMessageToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
