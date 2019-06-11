package com.example.dai_tp4;

import android.app.Fragment;
import android.content.Intent;
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class fragmentPorCategoria extends Fragment {
    View VistaDevolver;
    public ListView MiListaCatgorias;
    public ArrayAdapter arrayAdapter;
    public ArrayList<String> ListaCategorias;
    public View onCreateView(LayoutInflater inflater, ViewGroup grupoView, Bundle datosRecibidos)
    {
        VistaDevolver=inflater.inflate(R.layout.buscar_por_categoria,grupoView,false);
        ListaCategorias=new ArrayList<>();
        MiListaCatgorias=VistaDevolver.findViewById(R.id.ListaBuscarPorCategorias);
        arrayAdapter=new ArrayAdapter((MainActivity)getActivity(),android.R.layout.simple_list_item_1,ListaCategorias);
        tareaAsincronica miTarea=new tareaAsincronica();
        miTarea.execute();
        MiListaCatgorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Cat=ListaCategorias.get(position);
                MainActivity mainActivity=(MainActivity) getActivity();
                mainActivity.PasarAResultado(Cat);

            }
        });
        return VistaDevolver;
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

            } catch (Exception e) {
                Log.d("TryCatch1", "Error en el primer try catch   " +e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

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
                }
            }

        }
        catch (Exception e)
        {

        }
    }
}
