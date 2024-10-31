package com.example.proje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import android.database.SQLException;

public class sorguyaz extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView questionTextView;
    private EditText answerEditText;
    private TextView skors;
    private Button next;
    private Question currentQuestion;
    public int dogru = 0;
    public int yanlis = 0;
    public int soru_id = 0;
    public int soru_id_tut = 0;
    public int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sorguyaz_layout);

        questionTextView = findViewById(R.id.sorgu_txt_soru);
        answerEditText = findViewById(R.id.sorgu_edit_cevap);
        next = findViewById(R.id.sorgu_btn_next);
        skors = findViewById(R.id.sorgu_txt_skors);

        Intent intent = getIntent();
        if (intent != null) {
            dogru = intent.getIntExtra("dogru", 0);
            yanlis = intent.getIntExtra("yanlis", 0);
            soru_id = intent.getIntExtra("soru_id", 0);
        } else {
            Toast.makeText(this, "Intent null", Toast.LENGTH_SHORT).show();
            finish();
        }

        soru_id_tut = soru_id;

        skors.setText("doğru:" + dogru + "\nyanlış:" + yanlis);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAnswer = answerEditText.getText().toString().trim();
                checkAnswer(userAnswer);
                soru_id++;
                i = i + 1;
                loadQuestion();
                yonlendir();
                answerEditText.setText("");
            }
        });

        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDatabase();
            dbHelper.openDatabase();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        loadQuestion();
    }

    private void yonlendir() {
        if (i > 5) {
            Intent intent = new Intent(sorguyaz.this, finish.class);
            intent.putExtra("dogru", dogru);
            intent.putExtra("yanlis", yanlis);
            intent.putExtra("soru_id", soru_id_tut);
            startActivity(intent);
            finish();
        }
    }

    private void checkAnswer(String userAnswer) {
        if (currentQuestion == null) {
            return;
        }

        String correctAnswer = currentQuestion.getDogruCevap().trim();
        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            dogru++;
            skors.setText("doğru:" + dogru + "\nyanlış:" + yanlis);
        }
        else if(userAnswer.equalsIgnoreCase("")){

        }
        else {
            yanlis++;
            skors.setText("doğru:" + dogru + "\nyanlış:" + yanlis);
        }
    }

    private Question getQuestionFromDatabase() {
        SQLiteDatabase db = dbHelper.getDatabase();
        Cursor cursor = db.rawQuery("SELECT soru, cevap FROM sorguyaz WHERE soru_id=" + soru_id, null);
        Question question = null;
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String soru = cursor.getString(cursor.getColumnIndex("soru"));
            @SuppressLint("Range") String cevap = cursor.getString(cursor.getColumnIndex("cevap"));
            Log.d("sorguyaz", "Question loaded: " + soru + ", Answer: " + cevap);
            question = new Question(soru, cevap);
        } else {
            Log.d("sorguyaz", "No question found for soru_id=" + soru_id);
        }
        if (cursor != null) {
            cursor.close();
        }
        return question;
    }

    private void loadQuestion() {
        currentQuestion = getQuestionFromDatabase();
        if (currentQuestion != null) {
            questionTextView.setText(currentQuestion.getSoru());
        } else {
            //Toast.makeText(this, "Soru yüklenemedi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    public class Question {
        private String soru;
        private String dogruCevap;

        public Question(String soru, String cevap) {
            this.soru = soru;
            this.dogruCevap = cevap;
        }

        public String getSoru() {
            return soru;
        }

        public String getDogruCevap() {
            return dogruCevap;
        }
    }
}
