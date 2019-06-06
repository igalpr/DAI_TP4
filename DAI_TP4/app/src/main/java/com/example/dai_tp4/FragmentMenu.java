package com.example.dai_tp4;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentMenu extends Fragment implements View.OnClickListener {

    View VistaDevolver;
    Button Categorias;
    Button Buscar;
    Button Radio;
    public View onCreateView(LayoutInflater inflater, ViewGroup grupoView, Bundle datosRecibidos)
    {

        VistaDevolver=inflater.inflate(R.layout.menu_prinicpal,grupoView,false);
        Categorias=VistaDevolver.findViewById(R.id.BotonCategoria);
        Buscar=VistaDevolver.findViewById(R.id.BotonNombre);
        Radio=VistaDevolver.findViewById(R.id.BotonRadio);

        Categorias.setOnClickListener(this);
        Buscar.setOnClickListener(this);
        Radio.setOnClickListener(this);


        return VistaDevolver;
    }
    public void onClick(View v) {

        int id=v.getId();
        MainActivity actividadPrincipal=(MainActivity)getActivity();
        if(id==R.id.BotonNombre)
        {
            actividadPrincipal.LlamarABuscar(id);
        }
        if(id==R.id.BotonCategoria)
        {
            actividadPrincipal.LlamarCategorias(id);
        }
        if(id==R.id.BotonRadio)
        {
            actividadPrincipal.LLamarARadio(id);
        }
    }
    }

