package com.example.geek.starea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class QrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Absence");
        setSupportActionBar(toolbar);
    }
}
