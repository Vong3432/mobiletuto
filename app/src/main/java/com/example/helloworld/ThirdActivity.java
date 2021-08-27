package com.example.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {

    TextView txt_result;
    Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_page);

        txt_result = findViewById(R.id.txt_result);
        btn_ok = findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String seconds = intent.getStringExtra(SecondActivity.SECOND_MESSAGE);
        String counter = intent.getStringExtra(SecondActivity.COUNTER_MESSAGE);
        txt_result.setText("You have prayed " + counter + " times over the span second of " + seconds + ". Click ok to go back main page.");
    }
}
