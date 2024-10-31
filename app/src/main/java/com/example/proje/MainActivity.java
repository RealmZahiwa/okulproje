package com.example.proje;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button oyunaGir;
    Button profil;
    Button ayarlar;
    Button cikis;
    Button derslik; // Yeni buton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        oyunaGir = findViewById(R.id.main_btn_oyunagir);
        profil = findViewById(R.id.main_btn_profil);
        ayarlar = findViewById(R.id.main_btn_ayarlar);
        cikis = findViewById(R.id.main_btn_cikis);
        derslik = findViewById(R.id.main_btn_derslik);

        oyunaGir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, konular.class);
                startActivity(intent);
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, profil.class);
                startActivity(intent);
            }
        });

        ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ayarlar.class);
                startActivity(intent);
            }
        });

        cikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Çıkış işlemleri
            }
        });

        derslik.setOnClickListener(new View.OnClickListener() { // Yeni buton için onClickListener ekleyin
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DerslikActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
