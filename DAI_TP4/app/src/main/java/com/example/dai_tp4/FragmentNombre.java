package com.example.dai_tp4;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.VideoView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import static com.example.dai_tp4.R.id.ListaBuscarPorCategorias;

public class FragmentNombre extends Fragment implements View.OnClickListener{
    View VistaDevolver;
    Button BotonProcesar;
    public View onCreateView(LayoutInflater inflater, ViewGroup grupoView, Bundle datosRecibidos)
    {
        VistaDevolver=inflater.inflate(R.layout.buscra_por_nombre,grupoView,false);
        BotonProcesar=VistaDevolver.findViewById(R.id.BotonNombre);
        BotonProcesar.setOnClickListener(this);
        return VistaDevolver;
    }
    public void RecaudarInformacion()
    {
        EditText editText;
        editText=VistaDevolver.findViewById(R.id.NombreIngresado);

        String nombre=editText.getText().toString();
        MainActivity mainActivity=(MainActivity) getActivity();
        mainActivity.PasarAResultado(nombre);
    }

    @Override
    public void onClick(View v) {
        RecaudarInformacion();
    }
}
