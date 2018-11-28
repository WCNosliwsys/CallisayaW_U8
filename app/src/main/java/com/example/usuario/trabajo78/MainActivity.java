package com.example.usuario.trabajo78;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private static MainActivity ins;
    private GoogleMap mapa;
    private LocationManager manejador;
    private Location mejorLocaliz;
private int permiso1;
    private View vista;
    private double MiLatiude=0;
    private double MiLongitude=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ins = this;
        setContentView(R.layout.activity_main); // Obtenemos el mapa de forma asíncrona (notificará cuando esté listo)
        vista = findViewById(R.id.content);

        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        actualizarLoc();


        getgooglemap();
    }
    public static MainActivity  getInstace(){
        return ins;
    }

    void getgooglemap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

    }


    public void actualizarLoc() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            }
            if (manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            }
        } else {
            permiso1 = 1;
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso" + " de localización no podemos ubicarlo en el mapa.", 0);
        }
    }

    public void configmap() {
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MiLatiude, MiLongitude), 15));
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(false);
            mapa.getUiSettings().setCompassEnabled(true);
           /* if (mapa.getMyLocation() != null)
                mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()), 15));*/
        } else {
            permiso1 = 2;
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso" + " de localización no podemos ubicarlo en el mapa.", 0);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

     //   mapa.addMarker(new MarkerOptions().position(new LatLng(MiLatiude, MiLongitude)).title("UPV").snippet("Universidad Politécnica de Valencia").icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_compass)).anchor(0.5f, 0.5f));
       // mapa.setOnMapClickListener(this);
        configmap();
    }

/*       @Override
    public void onMapClick(LatLng puntoPulsado) {
        mapa.addMarker(new MarkerOptions().position(puntoPulsado).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }*/
//
    @Override protected void onResume() {
        super.onResume();
        activarProveedores();
        getgooglemap();
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
            permiso1=3;
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso"+ " de localización no podemos ubicarlo en el mapa.", 0);
        }
    }
    void removeractualizacion(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            manejador.removeUpdates(this);
           // Toast.makeText(this, "se removio updates", Toast.LENGTH_SHORT).show();
        }
        else{
            permiso1=4;
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso"+ " de localización no podemos ubicarlo en el mapa.", 0);

        }
    }
    @Override protected void onPause() {

        super.onPause();
        removeractualizacion();
    }
   //location listener
   @Override public void onLocationChanged(Location location) {

       actualizaMejorLocaliz(location);
      // Toast.makeText(this, "Latitude " + MiLatiude + " Longitude "+MiLongitude, Toast.LENGTH_SHORT).show();
      /* if(MiLongitude>-70.2686){
           Toast.makeText(this, "Latitude " + MiLatiude + " Longitude "+MiLongitude + "Se cumplio la condición Longitude > -70.2686", Toast.LENGTH_SHORT).show();
       }*/
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

/*    void solicitarPermiso(String permiso, String justificacion, int codigo) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(vista, justificacion, Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }*/

    void solicitarPermiso(final String permiso, String justificacion, final int codigo) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {
            new AlertDialog.Builder(this).setTitle("Solicitud de permiso").setMessage(justificacion).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permiso}, codigo);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permiso}, codigo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
             //   Toast.makeText(this, "El valor de permiso1 es "+ permiso1, Toast.LENGTH_SHORT).show();
                if (permiso1 == 1) {
                    actualizarLoc();
                }
                if (permiso1 == 2) {
                    getgooglemap();
                }
                if (permiso1 == 3) {
                    activarProveedores();
                }
                if (permiso1 == 4) {
                    removeractualizacion();
                }

                //getgooglemap();
            } else {
                Snackbar.make(vista, "Sin el permiso, no puedo realizar la" + "acción", Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    public void addMarker(String numero) {
        Toast.makeText(this, "intente hacer un marker", Toast.LENGTH_SHORT).show();
        mapa.addMarker(new MarkerOptions().position(new LatLng(MiLatiude, MiLongitude)).title(numero).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

}
