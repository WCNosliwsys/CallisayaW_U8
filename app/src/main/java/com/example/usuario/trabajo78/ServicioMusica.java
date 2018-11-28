package com.example.usuario.trabajo78;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by usuario on 10/11/2016.
 */

public class ServicioMusica extends Service {
    MediaPlayer reproductor;
    private static final int ID_NOTIFICACION_CREAR = 1;
    @Override
    public void onCreate() {
        Toast.makeText(this, "Servicio creado", Toast.LENGTH_SHORT).show();
        reproductor = MediaPlayer.create(this, R.raw.audio);
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        NotificationCompat.Builder notific = new NotificationCompat.Builder(this)
                .setContentTitle("Creando Servicio de Música")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("información adicional")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        android.R.drawable.ic_media_play))
                .setWhen(System.currentTimeMillis() + 1000 * 60 * 60)
                .setContentInfo("más info")
                .setTicker("Texto en barra de estado")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setVibrate(new long[] { 0,100,200,300 })
                .setLights(Color.GREEN, 3000, 1000);

        PendingIntent intencionPendiente = PendingIntent.getActivity( this, 0, new Intent(this, StopService.class), 0);
        notific.setContentIntent(intencionPendiente);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_NOTIFICACION_CREAR, notific.build());

        Toast.makeText(this, "Servicio arrancado " + idArranque, Toast.LENGTH_SHORT).show();
        reproductor.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ID_NOTIFICACION_CREAR);
        reproductor.stop();
    }

    @Override
    public IBinder onBind(Intent intencion) {
        return null;
    }
}
