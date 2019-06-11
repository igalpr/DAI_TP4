package com.example.dai_tp4;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FragmentMostrarResultado extends Fragment {
    public ListView listView;
    public ArrayAdapter arrayAdapter;
    public ArrayList<String> ListaACargar;
    String Filtro;
    View VistaADevolver;
    public View onCreateView(LayoutInflater inflater, ViewGroup grupoView, Bundle datosRecibidos) {
        VistaADevolver=inflater.inflate(R.layout.mostrar_resultados,null);
        ListaACargar=new ArrayList<>();
        listView=VistaADevolver.findViewById(R.id.ListaMostrarEnResultados);
        arrayAdapter=new ArrayAdapter((MainActivity)getActivity(),android.R.layout.simple_list_item_1,ListaACargar);
        tareaAsincronica miTarea=new tareaAsincronica();
        MainActivity mainActivity=(MainActivity)getActivity();
        Filtro=mainActivity.getFiltro();
        miTarea.execute();
        return VistaADevolver;
    }
    private class tareaAsincronica extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL miRuta= new URL("http://epok.buenosaires.gob.ar/buscar/?texto="+Filtro);
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
            if(ListaACargar.size()==0)
            {
                ListaACargar.add("Su búsqueda no arrojó resultados");
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setAdapter(arrayAdapter);
            Log.d("mostrar",""+ListaACargar.size());
        }
    }
    public void procesarJSONLeido(InputStreamReader streamLeido)
    {
        Log.d("procesar","llego");
        JsonReader JSONLeido=new JsonReader(streamLeido);

        try {
            JSONLeido.beginObject();
            while(JSONLeido.hasNext()){
                String NombreElemtoActual=JSONLeido.nextName();

                if(NombreElemtoActual.equals("instancias"))
                {
                    Log.d("mostrar","entro");
                    JSONLeido.beginArray();
                    while(JSONLeido.hasNext()){
                        JSONLeido.beginObject();
                        while(JSONLeido.hasNext()){
                            NombreElemtoActual=JSONLeido.nextName();
                            if(NombreElemtoActual.equals("nombre")){

                                String valorElementoActual=JSONLeido.nextString();
                                Log.d("mostrar",valorElementoActual);
                                ListaACargar.add(valorElementoActual);
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
            Log.d("mostrar",""+e.getLocalizedMessage());
        }
    }

}
