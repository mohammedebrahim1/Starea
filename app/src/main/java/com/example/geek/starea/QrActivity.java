package com.example.geek.starea;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QrActivity extends AppCompatActivity {

    @BindView(R.id.qr_toolbar)
    Toolbar qrToolbar;
    @BindView(R.id.qr)
    ImageView qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        ButterKnife.bind(this);

        qrToolbar.setTitle("Absence");
        setSupportActionBar(qrToolbar);
    }
}
