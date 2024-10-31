package com.example.proje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import android.database.SQLException;
import android.widget.Toast;

import com.example.proje.DatabaseHelper;

public class doruyan extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView questionTextView;
    private TextView skors;
    private Button dogruButton;
    private Button yanlisButton;
    private Button next;
    private Question currentQuestion;
    public int dogru=0;
    public int yanlis=0;
    public int soru_id = 0;
    public int soru_id_tut=0;
    public int i=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doruyan_layout);

        skors = findViewById(R.id.doruyan_txt_skors);
        questionTextView = findViewById(R.id.doruyan_txt_soru);
        dogruButton = findViewById(R.id.doruyan_btn_true);
        yanlisButton = findViewById(R.id.doruyan_btn_false);
        next = findViewById(R.id.doruyan_btn_next);


        Intent intent = getIntent();
        if (intent != null) {
            // Intent'ten verileri al
            dogru = intent.getIntExtra("dogru", 0);
            yanlis = intent.getIntExtra("yanlis", 0);
            soru_id = intent.getIntExtra("soru_id", 0);
            // Diğer işlemler...
        } else {
            // Uygun bir şekilde işlem yapın veya hata mesajı gösterin
            Toast.makeText(this, "Intent null", Toast.LENGTH_SHORT).show();
            finish(); // Aktiviteyi sonlandır
        }

        soru_id_tut=soru_id;

        skors.setText("doğru:"+dogru+"\nyanlış:"+yanlis);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soru_id++;
                i=i+1;
                loadQuestion();
                yonlendir();
                resetButtonColors(); // Buton renklerini sıfırla
                enableButtons(true); // Butonları etkinleştir
            }
        });

        dogruButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true, dogruButton);
                enableButtons(false); // Butonları devre dışı bırak
            }
        });

        yanlisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false, yanlisButton);
                enableButtons(false); // Butonları devre dışı bırak
            }
        });

        // Veritabanı yardımcısını başlat
        dbHelper = new DatabaseHelper(this);

        try {
            // Veritabanını oluştur ve aç
            dbHelper.createDatabase();
            dbHelper.openDatabase();
        } catch (IOException e) {
            throw new Error("Unable to create database");
        } catch (SQLException e) {
            throw e;
        }

        // İlk soruyu yükle
        loadQuestion();
    }

    private void yonlendir() {
        if (i > 5) {
            Intent intent = new Intent(doruyan.this, boslukdoldurma.class);
            intent.putExtra("dogru", dogru);
            intent.putExtra("yanlis", yanlis);
            intent.putExtra("soru_id", soru_id_tut);
            startActivity(intent);
            finish(); // Mevcut aktiviteyi kapat
        }
    }

    private void checkAnswer(boolean selectedAnswer, Button clickedButton) {
        if (currentQuestion == null) {
            // currentQuestion null ise, işlem yapma
            return;
        }

        boolean correctAnswer = currentQuestion.isDogruCevap(); // Doğru cevabı kontrol et

        // Seçilen cevap doğru ise yeşil, değilse kırmızı renk ayarı yapılıyor
        if (selectedAnswer == correctAnswer) {
            clickedButton.setBackgroundResource(R.drawable.button_background_green);
            dogru++;
            skors.setText("doğru:" + dogru + "\nyanlış:" + yanlis);
            yonlendir();
        } else {
            clickedButton.setBackgroundResource(R.drawable.button_background_red);
            yanlis++;
            skors.setText("doğru:" + dogru + "\nyanlış:" + yanlis);
            yonlendir();
        }
    }

    // Veritabanından soru çeken metod
    @SuppressLint("Range")
    private Question getQuestionFromDatabase() {
        SQLiteDatabase db = dbHelper.getDatabase();
        Cursor cursor = db.rawQuery("SELECT soru_icerik, dogru_cevap FROM tfsorular WHERE soru_id=" + soru_id, null);
        Question question = null;
        if (cursor.moveToFirst()) {
            String soru = cursor.getString(cursor.getColumnIndex("soru_icerik"));
            boolean dogruCevap = cursor.getInt(cursor.getColumnIndex("dogru_cevap")) > 0;
            question = new Question(soru, dogruCevap);
        }
        cursor.close();
        return question;
    }

    private boolean loadQuestion() {
        // Veritabanından soru çek
        currentQuestion = getQuestionFromDatabase();

        // TextView'e soru yaz
        if (currentQuestion != null) {
            questionTextView.setText(currentQuestion.getSoru());
            return true; // Soru yüklendi
        } else {
            enableButtons(false); // Butonları devre dışı bırak
            return false; // Soru yüklenemedi
        }
    }

    private void resetButtonColors() {
        dogruButton.setBackgroundResource(R.drawable.round_btn);
        yanlisButton.setBackgroundResource(R.drawable.round_btn);
    }

    private void enableButtons(boolean enabled) {
        dogruButton.setEnabled(enabled);
        yanlisButton.setEnabled(enabled);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    public class Question {
        private String soru;
        private boolean dogruCevap;

        public Question(String soru, boolean dogruCevap) {
            this.soru = soru;
            this.dogruCevap = dogruCevap;
        }

        public String getSoru() {
            return soru;
        }

        public boolean isDogruCevap() {
            return dogruCevap;
        }
    }
}
