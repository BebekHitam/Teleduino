package com.example.teleduino;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashNya extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_nya);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent cumaSplash = new Intent(SplashNya.this, MainActivity.class);
                startActivity(cumaSplash);
                finish();
            }
        }, 1000);





    }
}
