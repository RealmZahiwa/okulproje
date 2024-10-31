package com.example.proje;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proje.R;

public class DerslerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dersler);

        TextView textView = findViewById(R.id.dersler_textview);

        String topic = getIntent().getStringExtra("topic");
        if (topic != null) {
            switch (topic) {
                case "SELECT":
                    textView.setText("SELECT sorguları, veritabanından veri almak için kullanılır. Örneğin:\n\n" +
                            "SELECT * FROM Students WHERE age > 18;");
                    break;
                case "INSERT":
                    textView.setText("INSERT sorguları, veritabanına yeni veri eklemek için kullanılır. Örneğin:\n\n" +
                            "INSERT INTO Students (name, age) VALUES ('John Doe', 20);");
                    break;
                case "UPDATE":
                    textView.setText("UPDATE sorguları, veritabanındaki mevcut veriyi güncellemek için kullanılır. Örneğin:\n\n" +
                            "UPDATE Students SET age = 21 WHERE name = 'John Doe';");
                    break;
            }
        }
    }
}
