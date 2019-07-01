package com.example.dai_tp4;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ParteDosPuntoTres extends Activity {

    SeekBar seekbar;
    TextView ValorSeekBar;
    ListView MiListaCatgorias;
    ArrayList<String> ListaCategorias=new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    String cat;
    Double longitud;
    Double lati;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parte_dos_punto_tres);
        MiListaCatgorias=findViewById(R.id.listaDeCategorias);
        seekbar=findViewById(R.id.SeekBarRadio);
        Bundle DatosRecibidos=this.getIntent().getExtras();
        longitud=Double.parseDouble(DatosRecibidos.getString("longitud"));
        lati=Double.parseDouble(DatosRecibidos.getString("latitud"));
        ValorSeekBar=findViewById(R.id.SeguimientoSeekBar);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,ListaCategorias);
        tareaAsincronica miTarea=new tareaAsincronica();
        miTarea.execute();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ValorSeekBar.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        MiListaCatgorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cat=ListaCategorias.get(position);
                CambiarActivity();

            }
        });
        ;
    }
    public void  CambiarActivity()
    {
        Intent irAPuntoTres=new Intent(this,ResultadosPuntoTres.class);
        Bundle paqueteDeDatos=new Bundle();
        paqueteDeDatos.putDouble("latitud",lati);
        paqueteDeDatos.putDouble("longitud",longitud);
        paqueteDeDatos.putInt("radio",seekbar.getProgress());
        paqueteDeDatos.putString("categoria",cat);
        irAPuntoTres.putExtras(paqueteDeDatos);
        startActivity(irAPuntoTres);
    }
    private class tareaAsincronica extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL miRuta= new URL("http://epok.buenosaires.gob.ar/getCategorias");
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
                Log.d("Conexion","se desconecto");

            } catch (Exception e) {
                Log.d("TryCatch1", "Error en el primer try catch   " +e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            Log.d("onPostExecute","llega al onPostExecute");
            super.onPostExecute(aVoid);
            arrayAdapter.notifyDataSetChanged();
            MiListaCatgorias.setAdapter(arrayAdapter);
        }
    }
    public void procesarJSONLeido(InputStreamReader streamLeido)
    {
        JsonReader JSONLeido=new JsonReader(streamLeido);
        try {
            JSONLeido.beginObject();
            while(JSONLeido.hasNext()){
                String NombreElemtoActual=JSONLeido.nextName();

                if(NombreElemtoActual.equals("cantidad_de_categorias"))
                {
                    int cantidadCategorias=JSONLeido.nextInt();
                }
                else
                {
                    JSONLeido.beginArray();
                    while(JSONLeido.hasNext()){
                        JSONLeido.beginObject();
                        while(JSONLeido.hasNext()){
                            NombreElemtoActual=JSONLeido.nextName();
                            if(NombreElemtoActual.equals("nombre_normalizado")){
                                String valorElementoActual=JSONLeido.nextString();
                                Log.d("leolog",valorElementoActual);
                                ListaCategorias.add(valorElementoActual);
                            }
                            else{
                                JSONLeido.skipValue();
                            }
                        }
                        JSONLeido.endObject();
                    }
                    JSONLeido.endArray();
                    Log.d("Array",""+ListaCategorias.size());
                }
            }

        }
        catch (Exception e)
        {
            Log.d("errores",""+e.getLocalizedMessage());
        }
    }
}
