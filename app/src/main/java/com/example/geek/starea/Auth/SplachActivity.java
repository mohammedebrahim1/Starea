package com.example.geek.starea.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.geek.starea.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplachActivity extends AppCompatActivity {
    private static int SPLACH_SCREEN = 2500;
    Animation topAnim, bottomAnim;
    ImageView logo, logoName, solgan;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.splach_activity);
        //Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
        //Hooks
        logo = findViewById(R.id.logo);
        logoName = findViewById(R.id.logo_name);
        solgan = findViewById(R.id.solgan);

        logo.setAnimation(topAnim);
        logoName.setAnimation(bottomAnim);
        solgan.setAnimation(bottomAnim);
        // open login activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplachActivity.this , LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } , SPLACH_SCREEN);
    }
    }

