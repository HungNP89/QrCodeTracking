package com.example.qrcodetracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView qrGenerator, qrScanner, qrTracking, setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrGenerator = findViewById(R.id.generator);
        qrScanner = findViewById(R.id.scanner);
        qrTracking = findViewById(R.id.tracking);
        setting = findViewById(R.id.setting);


        qrGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToGenerate = new Intent(MainActivity.this,QrCodeGenerator.class);
                startActivity(moveToGenerate);
                finish();
            }
        });

        qrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToScanner = new Intent(MainActivity.this,QrCodeScanner.class);
                startActivity(moveToScanner);
                finish();
            }
        });

        qrTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToTracking = new Intent(MainActivity.this, QrCodeTracking.class);
                startActivity(moveToTracking);
                finish();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToSetting = new Intent(MainActivity.this,QrCodeSetting.class);
                startActivity(moveToSetting);
                finish();
            }
        });
    }
}