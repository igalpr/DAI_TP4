package com.example.dai_tp4;

import android.app.Fragment;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentNombre extends Fragment implements View.OnClickListener{
    View VistaDevolver;
    Button BotonProcesar;
    public View onCreateView(LayoutInflater inflater, ViewGroup grupoView, Bundle datosRecibidos)
    {
        VistaDevolver=inflater.inflate(R.layout.buscra_por_nombre,grupoView,false);
        BotonProcesar=VistaDevolver.findViewById(R.id.BotonProxima);
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


    public void onClick(View v) {
        RecaudarInformacion();
    }
}
