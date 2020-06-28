package com.example.geek.starea.Auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.geek.starea.DefaultSignupFragment;
import com.example.geek.starea.R;

public class SignupActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // default on start
        DefaultSignupFragment fragment1 = new DefaultSignupFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.signup_content, fragment1, "");
        ft1.commit();
    }
}



