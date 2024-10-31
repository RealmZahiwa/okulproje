package com.example.proje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.io.IOException;
import android.database.SQLException;
import android.widget.Toast;

import com.example.proje.DatabaseHelper;

public class test extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView questionTextView;
    private TextView skors;
    private Button testA;
    private Button testB;
    private Button testC;
    private Button testD;
    private Button next;
    private Question currentQuestion;

    public int soru_id = 0;
    public int soru_id_tut = 0;
    public int dogru=0;
    public int yanlis=0;
    public int i=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        questionTextView = findViewById(R.id.test_txt_soru);
        skors = findViewById(R.id.test_txt_skors);
        testA = findViewById(R.id.test_btn_a);
        testB = findViewById(R.id.test_btn_b);
        testC = findViewById(R.id.test_btn_c);
        testD = findViewById(R.id.test_btn_d);
        next = findViewById(R.id.test_btn_next);

        Intent intent = getIntent();
        if (intent != null) {
            soru_id = intent.getIntExtra("soru_id", 0);
        } else {
            Toast.makeText(this, "Intent null", Toast.LENGTH_SHORT).show();
            finish();
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

        testA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAnswer = testA.getText().toString();
                checkAnswer(selectedAnswer, testA);
                enableButtons(false); // Butonları devre dışı bırak
            }
        });

        testB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAnswer = testB.getText().toString();
                checkAnswer(selectedAnswer, testB);
                enableButtons(false); // Butonları devre dışı bırak
            }
        });

        testC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAnswer = testC.getText().toString();
                checkAnswer(selectedAnswer, testC);
                enableButtons(false); // Butonları devre dışı bırak
            }
        });

        testD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAnswer = testD.getText().toString();
                checkAnswer(selectedAnswer, testD);
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
    // ----UMUD-----
    private void yonlendir() {
        if (i > 5) {
            Intent intent = new Intent(test.this, doruyan.class);
            //doğru yanlış sayılarını aktarma
            intent.putExtra("dogru", dogru);
            intent.putExtra("yanlis", yanlis);
            intent.putExtra("soru_id", soru_id_tut);
            startActivity(intent);
            finish(); // Mevcut aktiviteyi kapat
        }
    }

    private void checkAnswer(String selectedAnswer, Button clickedButton) {
        if (currentQuestion == null) {
            // currentQuestion null ise, işlem yapma
            return;
        }

        String correctAnswer = currentQuestion.getSikA(); // Doğru cevabı herhangi bir şıkkı temsil ediyor olarak varsayıyoruz.

        // Seçilen cevap doğru ise yeşil, değilse kırmızı renk ayarı yapılıyor
        if (selectedAnswer.equals(correctAnswer)) {
            clickedButton.setBackgroundResource(R.drawable.button_background_green);
            dogru++;
            skors.setText("doğru:"+dogru+"\nyanlış:"+yanlis);
            yonlendir();
        } else {
            if(clickedButton != null) {
                clickedButton.setBackgroundResource(R.drawable.button_background_red);
            }
            yanlis++;
            skors.setText("doğru:"+dogru+"\nyanlış:"+yanlis);
            yonlendir();
        }
    }


    // Veritabanından soru çeken metod
    @SuppressLint("Range")
    private Question getQuestionFromDatabase() {
        SQLiteDatabase db = dbHelper.getDatabase();
        Cursor cursor = db.rawQuery("SELECT soru_icerik, dogru_cevap, yanlis_cevap1, yanlis_cevap2, yanlis_cevap3 FROM sorular WHERE soru_id=" + soru_id, null);
        Question question = null;
        if (cursor.moveToFirst()) {
            String soru = cursor.getString(cursor.getColumnIndex("soru_icerik"));
            String sikA = cursor.getString(cursor.getColumnIndex("dogru_cevap"));
            String sikB = cursor.getString(cursor.getColumnIndex("yanlis_cevap1"));
            String sikC = cursor.getString(cursor.getColumnIndex("yanlis_cevap2"));
            String sikD = cursor.getString(cursor.getColumnIndex("yanlis_cevap3"));
            question = new Question(soru, sikA, sikB, sikC, sikD);
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
            testA.setText(currentQuestion.getSikA());
            testB.setText(currentQuestion.getSikB());
            testC.setText(currentQuestion.getSikC());
            testD.setText(currentQuestion.getSikD());
            return true; // Soru yüklendi
        } else {
            // Eğer soru bulunamazsa hata mesajı göster veya varsayılan bir işlem yap
            enableButtons(false); // Butonları devre dışı bırak
            return false; // Soru yüklenemedi
        }
    }



    private void resetButtonColors() {
        testA.setBackgroundResource(R.drawable.round_btn);
        testB.setBackgroundResource(R.drawable.round_btn);
        testC.setBackgroundResource(R.drawable.round_btn);
        testD.setBackgroundResource(R.drawable.round_btn);
    }

    private void enableButtons(boolean enabled) {
        testA.setEnabled(enabled);
        testB.setEnabled(enabled);
        testC.setEnabled(enabled);
        testD.setEnabled(enabled);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    public class Question {
        private String soru;
        private String sikA;
        private String sikB;
        private String sikC;
        private String sikD;

        public Question(String soru, String sikA, String sikB, String sikC, String sikD) {
            this.soru = soru;
            this.sikA = sikA;
            this.sikB = sikB;
            this.sikC = sikC;
            this.sikD = sikD;
        }

        public String getSoru() {
            return soru;
        }

        public String getSikA() {
            return sikA;
        }

        public String getSikB() {
            return sikB;
        }

        public String getSikC() {
            return sikC;
        }

        public String getSikD() {
            return sikD;
        }
    }
}