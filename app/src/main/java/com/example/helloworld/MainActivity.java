package com.example.helloworld;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.note.MESSAGE";

    Button btnStart;
    EditText txtPrayerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPrayerName = findViewById(R.id.text_prayer_name);

        btnStart = findViewById(R.id.button_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SecondActivity.class);
                intent.putExtra(EXTRA_MESSAGE, txtPrayerName.getText().toString());
                startActivity(intent);
            }
        });
    }
}