package com.example.dai_tp4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActividadMapas extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap MiGoogleMaps;
    Location LocacionActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_mapas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void MoverAUbicacionActual(View vista) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        MiGoogleMaps.setMyLocationEnabled(true);
        LocacionActual.setLongitude(MiGoogleMaps.getMyLocation().getLongitude());
        LocacionActual.setLatitude(MiGoogleMaps.getMyLocation().getLatitude());
        ModificicarLatLng();

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MiGoogleMaps = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        MiGoogleMaps.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        MiGoogleMaps.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    */
    }
    public void ModificicarLatLng()
    {
        LatLng Ubicacion=new LatLng(LocacionActual.getLatitude(),LocacionActual.getLongitude());
        CameraUpdate UbicacionActual=CameraUpdateFactory.newLatLng(Ubicacion);
        Log.d("ubicacion","cambiar");
        MiGoogleMaps.moveCamera(UbicacionActual);
    }
    @Override
    public void onLocationChanged(Location location) {
        if(location!=null)
        {
            LocacionActual.setLatitude(location.getLatitude());
            LocacionActual.setLongitude(location.getLongitude());
            ModificicarLatLng();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
