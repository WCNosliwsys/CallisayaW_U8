package com.example.usuario.trabajo78;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by usuario on 14/11/2016.
 */

public class ServicioCirculoPolar extends Service implements LocationListener {
    private LocationManager manejador;
    private Location mejorLocaliz;

    private double MiLatiude=0;
   private double MiLongitude=0;
    private static final int ID_NOTIFICACION_CREAR = 1;
    @Override
    public void onCreate() {
        Toast.makeText(this, "Servicio Circulo Polar creado", Toast.LENGTH_SHORT).show();
        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        actualizarLoc();
        activarProveedores();
       }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        actualizarLoc();

        Toast.makeText(this, "Servicio arrancado " + idArranque, Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
    public void actualizarLoc() {
        Toast.makeText(this, "Actualizando localizacion", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            }
            if (manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            }
        } else {
            Toast.makeText(this, "No hay permiso", Toast.LENGTH_SHORT).show();
            /*permiso1 = 1;
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso" + " de localización no podemos ubicarlo en el mapa.", 0);*/
        }
    }
    //location listener
    @Override public void onLocationChanged(Location location) {

        actualizaMejorLocaliz(location);
     //   Toast.makeText(this, "Latitude " + MiLatiude + " Longitude "+MiLongitude , Toast.LENGTH_SHORT).show();
        if(MiLatiude>66.55)
        {

            NotificationCompat.Builder notific = new NotificationCompat.Builder(this)
                    .setContentTitle("Entraste al Circulo Polar Artico")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("Estas en el Circulo Polar Artico")
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            android.R.drawable.ic_media_play))
                    .setWhen(System.currentTimeMillis() + 1000 * 60 * 60)
                    .setContentInfo("Ver Mapa")
                    .setTicker("TEntraste al Circulo Polar Artico")
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setVibrate(new long[] { 0,100,200,300 })
                    .setLights(Color.GREEN, 3000, 1000);

            PendingIntent intencionPendiente = PendingIntent.getActivity( this, 0, new Intent(this, MainActivity.class), 0);
            notific.setContentIntent(intencionPendiente);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ID_NOTIFICACION_CREAR, notific.build());
        }

    }
    @Override public void onProviderDisabled(String proveedor) {

        activarProveedores();
    }
    @Override public void onProviderEnabled(String proveedor) {

        activarProveedores();
    }
    @Override
    public void onStatusChanged(String proveedor, int estado, Bundle extras) {

        activarProveedores();
    }
    private void activarProveedores() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        20 * 1000, 5, this);
            }
            if (manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                manejador.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        10 * 1000, 10, this);
            }
        }
        else{
           /* permiso1=3;
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso"+ " de localización no podemos ubicarlo en el mapa.", 0);*/
        }
    }
    private static final long DOS_MINUTOS = 2 * 60 * 1000;
    private void actualizaMejorLocaliz(Location localiz) {
        if (localiz != null && (mejorLocaliz == null
                || localiz.getAccuracy() < 2*mejorLocaliz.getAccuracy()
                || localiz.getTime() - mejorLocaliz.getTime() > DOS_MINUTOS)) {
            mejorLocaliz = localiz;
            MiLatiude=localiz.getLatitude();
            MiLongitude=localiz.getLongitude();

        }
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();

    }

    @Override
    public IBinder onBind(Intent intencion) {
        return null;
    }
}
