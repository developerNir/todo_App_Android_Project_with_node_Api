package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;


public class UserActivity extends AppCompatActivity {


//    String url = "https://impossible-lamb-outfit.cyclic.cloud/api/v1/list";
    String url = "https://nirabd.000webhostapp.com/apps/todoGet.php";

    TextView textView;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        // textview introduce
        textView = findViewById(R.id.textTv);

        // Create new Request Queue --------------------------------------

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        // JsonArrayRequest ----------------------------------------------


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                textView.setText(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText(error.toString());
            }
        });


//  add jsonArrayRequest with Request Queue --------------------------------
        requestQueue.add(jsonArrayRequest);

    }


}