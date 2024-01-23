package com.example.newsapp;



import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SplashActivity extends AppCompatActivity {

    TextView textView,tokenTv;
    Button button, buttonVerify,BtnVerify,btnGetAllTodo, btnCreateTodo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = findViewById(R.id.text2);
        button= findViewById(R.id.buttonNT);
        buttonVerify = findViewById(R.id.deleteButtonAC);
        BtnVerify = findViewById(R.id.BtnVerify);
        btnGetAllTodo = findViewById(R.id.btnGetAllTodo);
        btnCreateTodo = findViewById(R.id.btnCreateTodo);
        tokenTv = findViewById(R.id.tokenTv);



        tokenTv.setOnClickListener(v->{
            tokenTv.setText(MainActivity.sharedPreferences.getString("token","notToken"));
        });




        if (!internet()){
            btnCreateTodo.setVisibility(View.GONE);
            btnGetAllTodo.setVisibility(View.GONE);
            BtnVerify.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            buttonVerify.setVisibility(View.GONE);
            textView.setText("Please Your internet Connection Currently Off");

        }else{
            btnCreateTodo.setVisibility(View.VISIBLE);
            btnGetAllTodo.setVisibility(View.VISIBLE);
            BtnVerify.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            buttonVerify.setVisibility(View.VISIBLE);
        }
        



        btnCreateTodo.setOnClickListener(v->{
            startActivity(new Intent(SplashActivity.this, createTodo.class));
        });

        btnGetAllTodo.setOnClickListener(v->{
            startActivity(new Intent(SplashActivity.this, GetAllTodo.class));
        });

        textView.setOnClickListener(v->{
            start();
        });

        button.setOnClickListener(v->{
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        });
        buttonVerify.setOnClickListener(v->{
            startActivity(new Intent(SplashActivity.this, DeleteAcount.class));
        });
        BtnVerify.setOnClickListener(v->{
            startActivity(new Intent(SplashActivity.this, userVerify.class));
        });

    }
    private void start(){
        startActivity(new Intent(SplashActivity.this, UserActivity.class));
    }


    // intent Connection Checking ------------------------------
    public boolean internet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network networkInfo = connectivityManager.getActiveNetwork();
        if(networkInfo !=null){
            return true;
        }else {
            return false;
        }

    }


}