package com.example.dai_tp4;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ResultadosPuntoTres extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String Categoria;
    int Radio;
    LatLng puntoCentral;
    ArrayList<LatLng> puntos;
    ArrayList<String> Direcciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_punto_tres);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Bundle DatosRecibidos=this.getIntent().getExtras();
        double latitud=DatosRecibidos.getDouble("latitud");
        double longitud=DatosRecibidos.getDouble("longitud");
        puntoCentral=new LatLng(latitud,longitud);
        Categoria=DatosRecibidos.getString("categoria");
        Radio=DatosRecibidos.getInt("radio");
        tareaAsincronica miTarea=new tareaAsincronica();
        miTarea.execute();
        mapFragment.getMapAsync(this);
    }
    private class tareaAsincronica extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL miRuta= new URL("http://epok.buenosaires.gob.ar/reverseGeocoderLugares/?x="+puntoCentral.latitude+"&y="+puntoCentral.longitude+"&categorias="+Categoria+"&radio="+Radio);
                HttpURLConnection MiConexion=(HttpURLConnection) miRuta.openConnection();

                if(MiConexion.getResponseCode()==200)
                {
                    Log.d("Conexion", "Exitosa");
                    InputStream cuerpoRespuesta=MiConexion.getInputStream();
                    InputStreamReader lectorRespuesta= new InputStreamReader(cuerpoRespuesta, "UTf-8");
                    procesarJSONLeido(lectorRespuesta);
                }
                else
                {
                    Log.d("Conexion", "Error en la conexion");
                }
                MiConexion.disconnect();

            } catch (Exception e) {
                Log.d("TryCatch1", "Error en el primer try catch   " +e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            Log.d("procesar","termino");
            if(puntos.size()==0)
            {
                Toast toast1 =Toast.makeText(getApplicationContext(), "Su búsqueda no arrojó resultados", Toast.LENGTH_SHORT);
                toast1.show();
            }

            Log.d("mostrar",""+puntos.size());
            SegundaTareaAsincronica miTarea=new SegundaTareaAsincronica();
            miTarea.execute();
        }
    }
    public void procesarJSONLeido(InputStreamReader streamLeido)
    {
        Log.d("procesar","llego");
        JsonReader JSONLeido=new JsonReader(streamLeido);

        try {
            JSONLeido.beginObject();
            Log.d("mostrar","el momento de la verdad");
            while(JSONLeido.hasNext()){
                Log.d("mostrar","entro al while");
                String NombreElemtoActual=JSONLeido.nextName();
                Log.d("mostrar",NombreElemtoActual);
                if(NombreElemtoActual.equals("instancias"))
                {
                    Log.d("mostrar","entro");
                    JSONLeido.beginArray();
                    while(JSONLeido.hasNext()){
                        JSONLeido.beginObject();
                        while(JSONLeido.hasNext()){
                            NombreElemtoActual=JSONLeido.nextName();

                            if(NombreElemtoActual.equals("direccionNormalizada")){

                                String valorElementoActual=JSONLeido.nextString();
                                Log.d("mostrar",valorElementoActual);
                                Direcciones.add(valorElementoActual);
                            }
                            else{
                                JSONLeido.skipValue();
                            }
                        }
                        JSONLeido.endObject();
                    }
                    JSONLeido.endArray();
                }
                else{
                    JSONLeido.skipValue();
                }

            }

        }
        catch (Exception e)
        {
            Log.d("error",""+e.getLocalizedMessage());
        }
    }
    private class SegundaTareaAsincronica extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            for (String Direccion : Direcciones) {
                try {
                    URL miRuta = new URL("http://servicios.usig.buenosaires.gob.ar/normalizar/?direccion=" + Direccion);
                    HttpURLConnection MiConexion = (HttpURLConnection) miRuta.openConnection();

                    if (MiConexion.getResponseCode() == 200) {
                        Log.d("Conexion", "Exitosa");
                        InputStream cuerpoRespuesta = MiConexion.getInputStream();
                        InputStreamReader lectorRespuesta = new InputStreamReader(cuerpoRespuesta, "UTf-8");
                        procesarSegundoJSONLeido(lectorRespuesta);
                    } else {
                        Log.d("Conexion", "Error en la conexion");
                    }
                    MiConexion.disconnect();

                } catch (Exception e) {
                    Log.d("TryCatch1", "Error en el primer try catch   " + e.getMessage());
                }
                return null;
            }
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            Log.d("procesar","termino");
            if(puntos.size()==0)
            {
                Toast toast1 =Toast.makeText(getApplicationContext(), "Su búsqueda no arrojó resultados", Toast.LENGTH_SHORT);
                toast1.show();
            }

            Log.d("mostrar",""+puntos.size());
        }
    }
    public void procesarSegundoJSONLeido(InputStreamReader streamLeido)
    {
        Log.d("procesar","llego");
        JsonReader JSONLeido=new JsonReader(streamLeido);

        try {
            JSONLeido.beginObject();
            Log.d("mostrar","el momento de la verdad");
            while(JSONLeido.hasNext()){
                Log.d("mostrar","entro al while");
                String NombreElemtoActual=JSONLeido.nextName();
                Log.d("mostrar",NombreElemtoActual);
                if(NombreElemtoActual.equals("direccionesNormalizadas"))
                {
                    Log.d("mostrar","entro");
                    JSONLeido.beginArray();
                    while(JSONLeido.hasNext()){
                        JSONLeido.beginObject();
                        while(JSONLeido.hasNext()){
                            NombreElemtoActual=JSONLeido.nextName();

                            if(NombreElemtoActual.equals("coordenadas")){

                                JSONLeido.beginObject();
                                while(JSONLeido.hasNext()) {
                                    NombreElemtoActual=JSONLeido.nextName();

                                    if(NombreElemtoActual.equals("x")) {
                                        String valorElementoActual = JSONLeido.nextString();
                                        Log.d("mostrar", valorElementoActual);
                                        Direcciones.add(valorElementoActual.);
                                    }
                                }
                            }
                            else{
                                JSONLeido.skipValue();
                            }
                        }
                        JSONLeido.endObject();
                    }
                    JSONLeido.endArray();
                }
                else{
                    JSONLeido.skipValue();
                }

            }

        }
        catch (Exception e)
        {
            Log.d("error",""+e.getLocalizedMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ubicacionCente;
        ubicacionCente=new LatLng(-34.606353, -58.435696);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacionCente));

    }
}
