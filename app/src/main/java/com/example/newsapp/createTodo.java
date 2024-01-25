package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class createTodo extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String>arrayAdapter;
    String[] item = {"rad","green","yellow"};

    // from variable --------------------------------------------
    EditText titleEd,descriptionEd;
    Button btnCreate;
    String TAG = "createTodo";
    String getToken;
    SharedPreferences sharedPreferences;
    String url = "https://sore-pear-fish-cuff.cyclic.app/api/v1/todo-create";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        titleEd = findViewById(R.id.titleEd);
        descriptionEd = findViewById(R.id.descriptionEd);
        btnCreate = findViewById(R.id.btnCreate);

        sharedPreferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);



        getToken = sharedPreferences.getString("token", "");
        // dropDown Function ----------------------------------------------
        arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_manu, item);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(createTodo.this, item, Toast.LENGTH_SHORT).show();
            }
        });
        // -----------------------------------------------------------------



        btnCreate.setOnClickListener(v->{
            RequestQueue requestQueue = Volley.newRequestQueue(createTodo.this);


            Log.d(TAG, "onCreate: "+item.toString());

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("title", titleEd.getText().toString());
                jsonObject.put("description", descriptionEd.getText().toString());
                jsonObject.put("priority", "green");
                jsonObject.put("setTime", new Date().toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if (success){
                        Toast.makeText(createTodo.this, "Todo Create Success", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: "+response);
                        startActivity(new Intent(createTodo.this,GetAllTodo.class));
                    }
                    if (!success){
                        String error = response.getString("error");
                        Log.d(TAG, "onResponse: "+error);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("Authorization",getToken);
                return headers;
            }
        };

        // add request Queue -------------------------
            requestQueue.add(jsonObjectRequest);

        });





    }
}