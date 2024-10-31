package com.example.proje;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class profil extends AppCompatActivity {

    ListView listView;
    EditText editUsername;
    private Button kaydet;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_layout);

        listView = findViewById(R.id.profil_list_konular);
        editUsername = findViewById(R.id.profil_edit_username);
        kaydet = findViewById(R.id.profil_btn_kaydet);

        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDatabase();
            dbHelper.openDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Veritabanından veri çek
        ArrayList<String> veriDizisi = getAllKonu();

        // ArrayAdapter oluşturun ve veri dizisini ListView'e bağlayın
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, veriDizisi);

        // ListView'e adaptörü ayarlayın
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Tıklanan öğenin metnini al
                String topic = (String) parent.getItemAtPosition(position);

                // Yeni intent oluştur ve DerslerActivity'e yönlendir
                Intent intent = new Intent(profil.this, DerslerActivity.class);
                // İhtiyaç varsa, DerslerActivity'e veri aktarabilirsiniz:
                intent.putExtra("topic", topic);
                startActivity(intent);
            }
        });

        // Profil tablosundan username'i çek ve EditText'e ayarla
        String username = getUsername();
        if (username != null) {
            editUsername.setText(username);
        }

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = editUsername.getText().toString();
                if (!newUsername.isEmpty()) {
                    updateUsername(newUsername);
                }
            }
        });
    }

    private ArrayList<String> getAllKonu() {
        ArrayList<String> konuList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT konu FROM gecilemeyen_konular";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String konu = cursor.getString(cursor.getColumnIndex("konu"));
                konuList.add(konu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return konuList;
    }

    private String getUsername() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT username FROM profil WHERE profil_id = 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
            cursor.close();
            db.close();
            return username;
        }
        cursor.close();
        db.close();
        return null;
    }

    private void updateUsername(String newUsername) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", newUsername);
        db.update("profil", values, "profil_id = ?", new String[]{"1"});
        db.close();
    }
}