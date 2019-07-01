package com.example.dai_tp4;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    static MediaPlayer mediaPlayer;
    static boolean isPlayingAudio;
    LatLng posicionseleccionada=null;
    String Filtro="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentMenu fragmentMenu=new FragmentMenu();
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragamentAAsignar,fragmentMenu);
        fragmentTransaction.commit();
        isPlayingAudio=false;
        PlayAudio(this.getApplicationContext(),R.raw.musica);

    }
    /*public void SeguirProcesosPunto3()
    {  ParteDosPuntoTres punto2=new ParteDosPuntoTres();
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragamentAAsignar,punto2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void IrAlMapa(int radio,String Categoria)
    {

         Intent irAPuntoTres=new Intent(this,ActividadMapas.class);
         Bundle paqueteDeDatos=new Bundle();
         paqueteDeDatos.putDouble("latitud",posicionseleccionada.latitude);
         paqueteDeDatos.putDouble("longitud",posicionseleccionada.longitude);
         paqueteDeDatos.putInt("radio",radio);
         paqueteDeDatos.putString("categoria",Categoria);
         irAPuntoTres.putExtras(paqueteDeDatos);
         startActivity(irAPuntoTres);
    }*/
    public static void PlayAudio(Context c, int id){
        mediaPlayer = MediaPlayer.create(c, id);
        SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC,50);
        if (!mediaPlayer.isPlaying())
        {
            isPlayingAudio= true;
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }
    public void LlamarCategorias()
    {
        fragmentPorCategoria fragmentPorCategoria=new fragmentPorCategoria();
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragamentAAsignar,fragmentPorCategoria);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void LlamarABuscar()
    {
        FragmentNombre fragmentNombre=new FragmentNombre();
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        Log.d("transaction","begin");
        fragmentTransaction.replace(R.id.FragamentAAsignar,fragmentNombre);
        Log.d("transaction","replace");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.d("transaction","commit");
    }
    public void LLamarARadio()
    {

        Intent IrAlMapa=new Intent(this,ActividadMapas.class);
        startActivity(IrAlMapa);
    }
    public void PasarAResultado(String Valor)
    {
        Filtro=Valor;
        FragmentMostrarResultado fragmentMostrarResultado=new FragmentMostrarResultado();
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragamentAAsignar,fragmentMostrarResultado);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public String getFiltro()
    {
        return Filtro;
    }

}
