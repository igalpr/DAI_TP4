package com.example.dai_tp4;

import android.Manifest;
import android.content.Context;
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
        /*Log.d("mapas","entro");

        Log.d("mapas","entro");
        Location myLocation = MiGoogleMaps.getMyLocation();  //Nullpointer exception.........
        LatLng myLatLng = new LatLng(40.8957973,-3.022119);

        CameraPosition myPosition = new CameraPosition.Builder()
                .target(myLatLng).zoom(17).bearing(90).tilt(30).build();
        MiGoogleMaps.animateCamera(
                CameraUpdateFactory.newCameraPosition(myPosition));
        */
        LatLng ubicacionBuenosAires;
        ubicacionBuenosAires=new LatLng(-34.65,-58.45);

        CameraUpdate iraBuenosAires=CameraUpdateFactory.newLatLng(ubicacionBuenosAires);
        CameraUpdate hacerZoom=CameraUpdateFactory.zoomTo(12);

        MiGoogleMaps.moveCamera(iraBuenosAires);
        MiGoogleMaps.animateCamera(hacerZoom, 1000, null);

        LatLng ubicacionCente;
        ubicacionCente=new LatLng(-34.606353, -58.435696);
        MarkerOptions marquitaCente;
        marquitaCente=new MarkerOptions();
        marquitaCente.position(ubicacionCente);
        marquitaCente.title("Parque Centenario");

        MiGoogleMaps.addMarker(marquitaCente);
    }

    GoogleMap.OnMapClickListener escuchadorMapa=new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            Log.d("Manito","Se toco en latitud: "+latLng.latitude+" Y Longitus: "+latLng.longitude);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MiGoogleMaps = googleMap;

        MiGoogleMaps.setOnMapClickListener(escuchadorMapa);
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        MiGoogleMaps.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        MiGoogleMaps.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    */
    }

}