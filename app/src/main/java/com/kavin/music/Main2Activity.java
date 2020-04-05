package com.kavin.music;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    public String[] itemsall;

    public ListView msongs;

    private ArrayAdapter adapter;

    private MediaPlayer mplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        msongs = findViewById(R.id.songlist);

       // adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemsall);msongs.setAdapter(adapter);

        appexterstorage();

        mplayer = new MediaPlayer();
        if(mplayer != null)
        {
            mplayer.stop();
            mplayer.release();
        }
    }

    public void appexterstorage() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        displayaudio();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token)
                    {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    public ArrayList<File> readaudio(File file)
    {
        ArrayList<File> arrarlist =new ArrayList<>();

        File[] allfiles = file.listFiles();

        for(File individualfile : allfiles)
        {
            if(individualfile.isDirectory() && !individualfile.isHidden())
            {
                arrarlist.addAll(readaudio(individualfile));
            }
            else
            {
                if(individualfile.getName().endsWith(".mp3") || individualfile.getName().endsWith(".aac") || individualfile.getName().endsWith(".wav") || individualfile.getName().endsWith(".wma") || individualfile.getName().endsWith("m4a")) {
                    arrarlist.add(individualfile);



                }
            }
        }

        return arrarlist;

    }

    public void displayaudio()
    {
        final ArrayList<File> audiosongs=readaudio(Environment.getExternalStorageDirectory());

        itemsall =new String[audiosongs.size()];

        for(int sc=0;sc<audiosongs.size();sc++)
        {
            itemsall[sc] =audiosongs.get(sc).getName();
        }

        final ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(Main2Activity.this,android.R.layout.simple_list_item_1,itemsall);
        msongs.setAdapter(arrayAdapter);



        msongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mplayer = new MediaPlayer();
                if(mplayer != null)
                {
                    mplayer.stop();
                    mplayer.release();
                }
                    String songname = msongs.getItemAtPosition(position).toString();
                    Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                    intent.putExtra("song", audiosongs);
                    intent.putExtra("name", songname);
                    intent.putExtra("position", position);
                    startActivity(intent);

                }
        });
    }


}
