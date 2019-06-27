package com.example.dai_tp4;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ParteDosPuntoTres extends Fragment {
    View VistaADevolver;
    SeekBar seekbar;
    TextView ValorSeekBar;
    ListView MiListaCatgorias;
    ArrayList<String> ListaCategorias;
    ArrayAdapter arrayAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup grupoView, Bundle datosRecibidos) {

        VistaADevolver=inflater.inflate(R.layout.parte_dos_punto_tres,grupoView,false);
        MiListaCatgorias=VistaADevolver.findViewById(R.id.listaDeCategorias);
        seekbar=VistaADevolver.findViewById(R.id.SeekBarRadio);
        ValorSeekBar=VistaADevolver.findViewById(R.id.SeguimientoSeekBar);
        arrayAdapter=new ArrayAdapter((MainActivity)getActivity(),android.R.layout.simple_list_item_1,ListaCategorias);
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
                String Cat=ListaCategorias.get(position);
                MainActivity mainActivity=(MainActivity) getActivity();
                mainActivity.PasarAResultado(Cat);

            }
        });
        return VistaADevolver;
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
            if(ListaCategorias.isEmpty()) {
                Toast Vacio = Toast.makeText(getActivity(), "esta vacio", Toast.LENGTH_SHORT);
                Vacio.show();
            }
            else{
                Toast Vacio = Toast.makeText(getActivity(), "termino de cargar", Toast.LENGTH_SHORT);
                Log.d("Termino","termino de cargar");
                Vacio.show();
            }
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
                            if(NombreElemtoActual.equals("nombre")){
                                String valorElementoActual=JSONLeido.nextString();
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
