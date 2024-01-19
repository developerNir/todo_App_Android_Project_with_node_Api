package com.example.newsapp;



import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class SplashActivity extends AppCompatActivity {

    TextView textView;
    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = findViewById(R.id.text2);
        button= findViewById(R.id.buttonNT);
//        buttonVerify = findViewById(R.id.buttonVarfiy);

        textView.setOnClickListener(v->{
            start();
        });

        button.setOnClickListener(v->{
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        });
//        buttonVerify.setOnClickListener(v->{
////            startActivity(new Intent(SplashActivity.this, varifiy_fragment.class));
//        });

    }
    private void start(){
        startActivity(new Intent(SplashActivity.this, UserActivity.class));
    }
}