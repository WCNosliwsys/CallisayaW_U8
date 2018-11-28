package com.example.usuario.trabajo78;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by usuario on 14/11/2016.
 */

public class ReceptorSms extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServicioMusica.class));
    }
}
