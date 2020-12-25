package com.aldiariq.avalancheeffect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnEnkripsi, btnDekripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initView();

        this.btnEnkripsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent halamanEnkripsi = new Intent(MainActivity.this, Enkripsi.class);
                startActivity(halamanEnkripsi);
            }
        });

        this.btnDekripsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent halamanDekripsi = new Intent(MainActivity.this, Dekripsi.class);
                startActivity(halamanDekripsi);
            }
        });
    }

    private void initView(){
        this.btnEnkripsi = (Button) findViewById(R.id.btnEnkripsiutama);
        this.btnDekripsi = (Button) findViewById(R.id.btnDekripsiutama);
    }
}