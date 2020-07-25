package com.example.geek.starea.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geek.starea.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomePageActivity extends AppCompatActivity {
    @BindView(R.id.loginAc)
    Button loginAc;
    @BindView(R.id.signupTv)
    TextView signupTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        ButterKnife.bind(this);
        loginAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        signupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(WelcomePageActivity.this , SignupActivity.class);
                startActivity(intent2);
            }
        });

    }


}
