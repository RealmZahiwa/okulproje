package com.example.proje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class konular extends AppCompatActivity {
    Button select;
    Button insert;
    Button update;
    public int soru_id = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konular_layout);

        select = findViewById(R.id.konular_btn_select);
        insert = findViewById(R.id.konular_btn_insert);
        update = findViewById(R.id.konular_btn_update);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(konular.this,test.class);
                soru_id=1;
                intent.putExtra("soru_id", soru_id);
                startActivity(intent);
                finish();
            }
        });
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(konular.this,test.class);
                soru_id=6;
                intent.putExtra("soru_id", soru_id);
                startActivity(intent);
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(konular.this,test.class);
                soru_id=11;
                intent.putExtra("soru_id", soru_id);
                startActivity(intent);
                finish();
            }
        });

    }
}
