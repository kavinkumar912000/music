package com.kavin.music;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout pr;
    private SpeechRecognizer sr;
    private Intent srintent;
    private String keeper ="";

    private ImageView pauseplaybutton,nextbutton,previousbutton,forwardbutton,backwardbutton;
    private TextView songNameText;

    private ImageView imageview;
    private RelativeLayout lowerrelativelayout;
    private Button voiceenabledbutton;

    private String mode="ON";

    private MediaPlayer mymediaplayer;
    private int position;
    private ArrayList<File> mysongs;
    private String msongname;
    public Button speak;
    private SeekBar seekbar;
    private Runnable runnable;
    private Handler handler;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkVoice();

        pauseplaybutton = findViewById(R.id.pause_btn);
        nextbutton = findViewById(R.id.next_btn);
        previousbutton = findViewById(R.id.previous_btn);
        imageview = findViewById(R.id.logo);

        lowerrelativelayout = findViewById(R.id.lower);
        voiceenabledbutton = findViewById(R.id.voice_enabled);
        songNameText = findViewById(R.id.songname);
        speak = findViewById(R.id.spk);
        forwardbutton =findViewById(R.id.forward_btn);
        backwardbutton=findViewById(R.id.backword_btn);
        seekbar =findViewById(R.id.mseekbar);
        handler =new Handler();

        mymediaplayer =new MediaPlayer();

        pr = findViewById(R.id.parentlayout);

        sr=SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        srintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        srintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        srintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


            validateReceive();
        imageview.setBackgroundResource(R.drawable.logo);


        sr.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results)
            {
                ArrayList<String> metchesfound =results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(metchesfound != null)
                {
                   if(mode.equals("ON"))
                   {
                       keeper = metchesfound.get(0);

                       if(keeper.equals("stop"))
                       {
                           playpausesong();
                           Toast.makeText(MainActivity.this,"command="+keeper,Toast.LENGTH_SHORT).show();
                       }
                       else if(keeper.equals("play"))
                       {
                           playpausesong();
                           Toast.makeText(MainActivity.this,"command="+keeper,Toast.LENGTH_SHORT).show();
                       }
                       else if(keeper.equals("next song"))
                       {
                           playnext();
                           Toast.makeText(MainActivity.this,"command="+keeper,Toast.LENGTH_SHORT).show();
                       }
                       else if(keeper.equals("previous song"))
                       {
                           playprevious();
                           Toast.makeText(MainActivity.this,"command="+keeper,Toast.LENGTH_SHORT).show();
                       }
                       else if(keeper.equals("forward"))
                       {
                           mymediaplayer.seekTo(mymediaplayer.getCurrentPosition()+5000);
                           Toast.makeText(MainActivity.this,"command="+keeper,Toast.LENGTH_SHORT).show();
                       }
                       else if(keeper.equals("back"))
                       {
                           mymediaplayer.seekTo(mymediaplayer.getCurrentPosition()-5000);
                           Toast.makeText(MainActivity.this,"command="+keeper,Toast.LENGTH_SHORT).show();
                       }

                   }

                }

            }

            @Override
            public void onPartialResults(Bundle partialResults)
            {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        speak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sr.startListening(srintent);
                        keeper ="";
                        break;

                    case MotionEvent.ACTION_UP:
                        sr.stopListening();
                        break;

                }
                return false;
            }
        });


        pr.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sr.startListening(srintent);
                        keeper ="";
                        break;

                    case MotionEvent.ACTION_UP:
                        sr.stopListening();
                        break;

                }
                return false;
            }
        });

        voiceenabledbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("ON"))
                {
                    mode="OFF";
                    voiceenabledbutton.setText("Voice Enabled Mode - OFF");
                    lowerrelativelayout.setVisibility(View.VISIBLE);
                    speak.setVisibility(View.GONE);
                    }
                else
                {
                    mode="ON";
                    voiceenabledbutton.setText("Voice Enabled Mode - ON");
                    lowerrelativelayout.setVisibility(View.GONE);
                    speak.setVisibility(View.VISIBLE);
                }

            }
        });

        pauseplaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               playpausesong();
            }
        });

        previousbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(mymediaplayer.getCurrentPosition()>0)
               {
                   playprevious();
               }
            }
        });

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mymediaplayer.getCurrentPosition()>0)
                {
                    playnext();
                }
            }
        });
        forwardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymediaplayer.seekTo(mymediaplayer.getCurrentPosition()+5000);
            }
        });
        backwardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymediaplayer.seekTo(mymediaplayer.getCurrentPosition()-5000);
            }
        });




        mymediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mymediaplayer) {
                seekbar.setMax(mymediaplayer.getDuration());
                mymediaplayer.start();
                cs();

            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    mymediaplayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }




    private void validateReceive(){
        if(mymediaplayer != null)
        {
            mymediaplayer.stop();
            mymediaplayer.release();
        }


        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();


        mysongs=(ArrayList)bundle.getParcelableArrayList("song");
        msongname =mysongs.get(position).getName();
        String songname=intent.getStringExtra("name");
        songNameText.setText(songname);
        songNameText.setSelected(true);


        position=bundle.getInt("position",0);
        Uri uri=Uri.parse(mysongs.get(position).toString());


        mymediaplayer=MediaPlayer.create(MainActivity .this,uri);
        mymediaplayer.start();
        cs();



    }

    private void checkVoice()
    {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            if(!(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED))
            {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.parse("package:"+getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
    private void cs() {
        seekbar.setProgress(mymediaplayer.getCurrentPosition());

        if(mymediaplayer.isPlaying())
        {
            runnable =new Runnable() {
                @Override
                public void run() {
                    cs();
                }
            };
            handler.postDelayed(runnable,1000);
        }
    }



    private void playpausesong()
    {
        imageview.setBackgroundResource(R.drawable.four);

        if(mymediaplayer.isPlaying())
        {
            pauseplaybutton.setImageResource(R.drawable.play);
            mymediaplayer.pause();
        }
        else
        {
            pauseplaybutton.setImageResource(R.drawable.pause);
            mymediaplayer.start();
            cs();

            imageview.setBackgroundResource(R.drawable.five);
        }
    }

    private void playnext()
    {
        mymediaplayer.pause();
        mymediaplayer.stop();
        mymediaplayer.release();
        seekbar.setProgress(10);

        position =((position+1)%mysongs.size());

        Uri uri=Uri.parse(mysongs.get(position).toString());

        mymediaplayer =MediaPlayer.create(MainActivity.this,uri);

        msongname = mysongs.get(position).toString();
        songNameText.setText(msongname);
        mymediaplayer.start();

        imageview.setBackgroundResource(R.drawable.three);

        if(mymediaplayer.isPlaying())
        {
            pauseplaybutton.setImageResource(R.drawable.pause);
        }
        else
        {
            pauseplaybutton.setImageResource(R.drawable.play);

            imageview.setBackgroundResource(R.drawable.five);
        }
    }

    private void playprevious()
    {
        mymediaplayer.pause();
        mymediaplayer.stop();
        mymediaplayer.release();
        seekbar.setProgress(10);

        position = ((position-1)<0 ? (mysongs.size()-1) :(position-1));

        Uri uri=Uri.parse(mysongs.get(position).toString());

        mymediaplayer =MediaPlayer.create(MainActivity.this,uri);

        msongname = mysongs.get(position).toString();
        songNameText.setText(msongname);
        mymediaplayer.start();

        imageview.setBackgroundResource(R.drawable.two);

        if(mymediaplayer.isPlaying())
        {
            pauseplaybutton.setImageResource(R.drawable.pause);
        }
        else
        {
            pauseplaybutton.setImageResource(R.drawable.play);

            imageview.setBackgroundResource(R.drawable.five);
        }


    }
}
