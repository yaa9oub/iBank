package com.example.ibank;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    RelativeLayout icongrp ;
    ImageView banklogo ;
    TextView banktxt ;
    Animation leftoright,righttoleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

      icongrp = findViewById(R.id.icon_group);
      banklogo = findViewById(R.id.logo);
      banktxt = findViewById(R.id.banktxt);

        leftoright  = AnimationUtils.loadAnimation(this,R.anim.lefttoright);
        righttoleft = AnimationUtils.loadAnimation(this,R.anim.righttoleft);

        banklogo.setAnimation(righttoleft);
        banktxt.setAnimation(leftoright);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                icongrp.startAnimation(AnimationUtils.loadAnimation(SplashScreen.this,R.anim.splash_out));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent a = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(a);
                        finish();
                    }
                },500);
            }
        },2000);

    }
}
