package com.example.proje;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class finish extends AppCompatActivity {

    public int dogru = 0;
    public int yanlis = 0;
    public int soru_id = 0;
    private Button main_menu;
    private TextView skors;
    private DatabaseHelper dbHelper;

    public String konu = "konu";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_layout);

        skors = findViewById(R.id.finish_txt_skors);
        main_menu = findViewById(R.id.finish_btn_main);

        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDatabase();
            dbHelper.openDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        if (intent != null) {
            dogru = intent.getIntExtra("dogru", 0);
            yanlis = intent.getIntExtra("yanlis", 0);
            soru_id = intent.getIntExtra("soru_id", 0);
        } else {
            Toast.makeText(this, "Intent null", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(soru_id==1){
            konu="SELECT";
        } else if (soru_id==6) {
            konu="INSERT";
        }
        else{
            konu="UPDATE";
        }

        skors.setText("doğru:" + dogru + "\nyanlış:" + yanlis);

        if (dogru < 15) {
            if (!checkKonuExists(konu)) {
                insertData(konu);
            }
        } else {
            if (checkKonuExists(konu)) {
                deleteData(konu);
            }
        }

        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finish.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean checkKonuExists(String konu) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT 1 FROM gecilemeyen_konular WHERE konu = ?";
        Cursor cursor = db.rawQuery(query, new String[]{konu});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    private void insertData(String konu) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("konu", konu);
        db.insert("gecilemeyen_konular", null, values);
        db.close();
    }

    private void deleteData(String konu) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "konu = ?";
        db.delete("gecilemeyen_konular", whereClause, new String[]{konu});
        db.close();
    }
}