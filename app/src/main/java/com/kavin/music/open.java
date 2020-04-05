package com.kavin.music;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class open extends AppCompatActivity {
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);


        Thread thread=new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(1000);

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent=new Intent(open.this,Main2Activity.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
