package com.example.proje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DerslikActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.derslik);

        Button selectButton = findViewById(R.id.derslik_btn_select);
        Button insertButton = findViewById(R.id.derslik_btn_insert);
        Button updateButton = findViewById(R.id.derslik_btn_update);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDerslerActivity("SELECT");
            }
        });

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDerslerActivity("INSERT");
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDerslerActivity("UPDATE");
            }
        });
    }

    private void goToDerslerActivity(String topic) {
        Intent intent = new Intent(DerslikActivity.this, DerslerActivity.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }
}
