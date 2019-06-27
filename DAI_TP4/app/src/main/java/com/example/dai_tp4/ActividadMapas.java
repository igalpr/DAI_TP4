package com.example.dai_tp4;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class ActividadMapas extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap MiGoogleMaps;
    public LatLng ClickDelUsuario=null;
    public LatLng getClickDelUsuario(){
        return ClickDelUsuario;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_mapas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void MoverAUbicacionActual(View vista) {
        LatLng ubicacionCente;
        ubicacionCente=new LatLng(-34.606353, -58.435696);
        MarkerOptions marquitaCente;
        marquitaCente=new MarkerOptions();
        marquitaCente.position(ubicacionCente);
        marquitaCente.title("Parque Centenario");
        CameraUpdate Centenerio=CameraUpdateFactory.newLatLng(ubicacionCente);
        MiGoogleMaps.addMarker(marquitaCente);
        CameraUpdate hacerZoom=CameraUpdateFactory.zoomTo(12);
        MiGoogleMaps.moveCamera(Centenerio);
        MiGoogleMaps.animateCamera(hacerZoom, 1000, null);


    }

    GoogleMap.OnMapClickListener escuchadorMapa=new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            MarkerOptions MarcaClick;
            MarcaClick=new MarkerOptions();
            MarcaClick.position(latLng);
            MarcaClick.title("click del usuario");
            CameraUpdate Centenerio=CameraUpdateFactory.newLatLng(latLng);
            MiGoogleMaps.addMarker(MarcaClick);
            CameraUpdate hacerZoom=CameraUpdateFactory.zoomTo(12);
            MiGoogleMaps.moveCamera(Centenerio);
            MiGoogleMaps.animateCamera(hacerZoom, 1000, null);
            ClickDelUsuario=latLng;
            Intent CambiarAView;
            CambiarAView = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(CambiarAView);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MiGoogleMaps = googleMap;

        MiGoogleMaps.setOnMapClickListener(escuchadorMapa);
        LatLng ubicacionCente;
        ubicacionCente=new LatLng(-34.606353, -58.435696);
        MarkerOptions marquitaCente;
        marquitaCente=new MarkerOptions();
        marquitaCente.position(ubicacionCente);
        marquitaCente.title("Parque Centenario");
        CameraUpdate Centenerio=CameraUpdateFactory.newLatLng(ubicacionCente);
        MiGoogleMaps.addMarker(marquitaCente);
        CameraUpdate hacerZoom=CameraUpdateFactory.zoomTo(12);
        MiGoogleMaps.moveCamera(Centenerio);
        MiGoogleMaps.animateCamera(hacerZoom, 1000, null);
    }

}