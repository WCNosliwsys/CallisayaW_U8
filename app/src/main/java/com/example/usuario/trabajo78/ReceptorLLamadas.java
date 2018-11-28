package com.example.usuario.trabajo78;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by usuario on 14/11/2016.
 */

public class ReceptorLLamadas extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String estado = "", numero = "";
        Bundle extras = intent.getExtras();
        if (extras != null) {
            estado = extras.getString(TelephonyManager.EXTRA_STATE);
            if (estado.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                numero = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                String info = estado + " " + numero;
                Log.d("ReceptorAnuncio", info + " intent=" + intent);
// Creamos Notificación
                NotificationCompat.Builder notificacion = new NotificationCompat.Builder(context).setContentTitle("Llamada entrante ")
                        .setContentText(info).setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
                ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, notificacion.build());


                try {
                    Log.d("ReceptorAnuncio", " entramos");
                    MainActivity.getInstace().addMarker(numero);
                } catch (Exception e) {

                }

            }
        }
    }
}