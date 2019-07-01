package com.example.dai_tp4;

import android.app.Activity;
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

import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResultadosPuntoTres extends Activity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String Categoria;
    int Radio;
    LatLng puntoCentral;
    String direccion;
    ArrayList<String>listaPropiedades =new ArrayList();
    Double latitud;
    Double longitud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_punto_tres);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Bundle DatosRecibidos=this.getIntent().getExtras();
        latitud=DatosRecibidos.getDouble("latitud");
        longitud=DatosRecibidos.getDouble("longitud");
        Categoria=DatosRecibidos.getString("categoria");
        Radio=DatosRecibidos.getInt("radio");
        tareaAsincronica miTarea=new tareaAsincronica();
        miTarea.execute();

    }
   private class tareaAsincronica extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Log.d("mostrar",""+Radio);
                URL miRuta= new URL("http://epok.buenosaires.gob.ar/reverseGeocoderLugares/?x=-34.606353&y=-58.435696&categorias=" + Categoria + "&radio=" + Radio);
                HttpURLConnection MiConexion=(HttpURLConnection) miRuta.openConnection();

                if(MiConexion.getResponseCode()==200)
                {
                    Log.d("Conexion", "Exitosa");
                    InputStream cuerpoRespuesta=MiConexion.getInputStream();
                    InputStreamReader lectorRespuesta= new InputStreamReader(cuerpoRespuesta, "UTF-8");
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
            try {
                URL miUrl = new URL("http://servicios.usig.buenosaires.gob.ar/normalizar/?direccion=" +
                        direccion + ", caba&geocodificar=true");
                Log.d("Coord", "rquestCoord: " + "http://servicios.usig.buenosaires.gob.ar/normalizar/?direccion=" +
                        direccion + ", caba&geocodificar=true");
                HttpURLConnection conexion = (HttpURLConnection) miUrl.openConnection();
                if(conexion.getResponseCode()==200){
                    Log.d("API", "Me conecte con la 2da API");
                    InputStream cadenaRespuesta;
                    cadenaRespuesta = conexion.getInputStream();
                    InputStreamReader lectorCadena;
                    lectorCadena = new InputStreamReader(cadenaRespuesta, "UTF-8");
                    procesarCoordenadas(lectorCadena);
                }
            }
            catch (Exception e){
                Log.d("API", e.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            Log.d("procesar","termino");
            marcarLugar();
        }
    }
    private void procesarCoordenadas(InputStreamReader lectorRespuesta){
        JsonReader JotaSONReader;
        JotaSONReader = new JsonReader(lectorRespuesta);
        try {
            Log.d("Coord", "Empiezo a leer el Json");
            JotaSONReader.beginObject();
            while (JotaSONReader.hasNext()) {
                String nombreActual;
                nombreActual = JotaSONReader.nextName();
                Log.d("Coord", "El nombre actual es: " + nombreActual);
                if (nombreActual.equals("direccionesNormalizadas"))
                {
                    JotaSONReader.beginArray();
                    while (JotaSONReader.hasNext())
                    {
                        Log.d("Coord", "Estoy en el array");
                        JotaSONReader.beginObject();
                        while (JotaSONReader.hasNext()) {
                            Log.d("Coord", "Estoy en el object de direcciones");
                            nombreActual = JotaSONReader.nextName();
                            Log.d("Coord", "El nombre actual (array) es: " + nombreActual);
                            if (nombreActual.equals("coordenadas")) {
                                Log.d("Coord", "Estoy en coordenadas");
                                JotaSONReader.beginObject();
                                while (JotaSONReader.hasNext()) {
                                    nombreActual = JotaSONReader.nextName();
                                    Log.d("Coord", "El nombre actual (coordenadas) es: " + nombreActual);
                                    if (nombreActual.equals("x")) {
                                        Log.d("Coord", "Estoy en latitud(x)");
                                        String coordX = JotaSONReader.nextString();
                                        listaPropiedades.add("" + coordX);
                                        Log.d("???", "Latitud: " + coordX);
                                    }
                                    else
                                    {
                                        if (nombreActual.equals("y")) {
                                            Log.d("Coord", "Estoy en longitud(y)");
                                            String coordY = JotaSONReader.nextString();
                                            listaPropiedades.add("" + coordY);
                                            Log.d("???", "Latitud: " + coordY);
                                        }
                                        else
                                        {
                                            JotaSONReader.skipValue();
                                        }
                                    }
                                }
                                JotaSONReader.endObject();
                            }
                            else
                            {
                                JotaSONReader.skipValue();
                            }
                        }
                    }
                    JotaSONReader.endArray();
                }
                else
                {
                    JotaSONReader.skipValue();
                }
            }
            JotaSONReader.endObject();
        }catch (Exception e)
        {
            Log.d("Coord", "Error JotaSON");
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
                    while(JSONLeido.hasNext())
                    {
                        JSONLeido.beginObject();
                        Log.d("mostrar","entro2");
                        while(JSONLeido.hasNext())
                        {
                            NombreElemtoActual=JSONLeido.nextName();
                            Log.d("mostrar",""+NombreElemtoActual);
                            if(NombreElemtoActual.equals("direccionNormalizada")){

                                String valorElementoActual=JSONLeido.nextString();
                                Log.d("mostrar",valorElementoActual);
                                direccion=valorElementoActual;
                            }
                            else{
                                JSONLeido.skipValue();
                            }
                        }
                        JSONLeido.endObject();
                    }
                    Log.d("mostrar","no entro al while");
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
    public void marcarLugar()
    {
        double lat=Double.parseDouble(listaPropiedades.get(0));
        double longi=Double.parseDouble(listaPropiedades.get(1));
        LatLng ubicacion=new LatLng(lat,longi);
        MarkerOptions marquitaCente;
        marquitaCente=new MarkerOptions();
        marquitaCente.position(ubicacion);
        marquitaCente.title("Parque Centenario");
        mMap.addMarker(marquitaCente);
    }
}
