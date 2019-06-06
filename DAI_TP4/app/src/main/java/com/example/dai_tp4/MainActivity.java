package com.example.dai_tp4;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Bundle;

public class MainActivity extends Activity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentMenu fragmentMenu=new FragmentMenu();
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragamentAAsignar,fragmentMenu);
        fragmentTransaction.commit();
        ponercancion();

    }
    private void ponercancion()
    {

        if(mediaPlayer!=null)
        {
            mediaPlayer.release();
        }
        mediaPlayer=MediaPlayer.create(this,R.raw.musica);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }
    public void LlamarCategorias(int id)
    {

    }
    public void LlamarABuscar(int id)
    {

    }
    public void LLamarARadio(int id)
    {

    }

}
