package com.example.newsapp;



import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {




    private static final String TAG = "response";


    TextView textView;
    public RequestQueue requestQueue;
    ProgressBar progressBar;
    String url = "https://sore-pear-fish-cuff.cyclic.app/api/v1/register";



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.ResponseText);
        progressBar = findViewById(R.id.progress_circular);


        requestQueue = Volley.newRequestQueue(MainActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "Volley");
            jsonObject.put("password", "93485sl89");
            jsonObject.put("email", "loveyoupal16@gmail.com");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the JSON response
                        progressBar.setVisibility(View.GONE);
                        try{
                            JSONObject jsonObject1 = new JSONObject(response.toString());
                            String token = jsonObject1.getString("token");
                            String user = jsonObject1.getString("user");
                            textView.append("\n"+token);
                            textView.append("\n\n"+user);
                        }catch (JSONException err){
                            textView.append("\n"+err.toString());
                        }

                        Log.d(TAG, "JsonObjectRequest onResponse: " + response.toString());
                        // Now, you can parse the JSON response and extract data
                        // If the response contains a reference to a JsonArray, you can proceed to fetch it
//
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        progressBar.setVisibility(View.GONE);
                        textView.setText(error.toString());
                        Log.e(TAG, "JsonObjectRequest onErrorResponse: " + error.toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("Content-Type", "application/json");
                return hashMap;
            }
        };

        requestQueue.add(jsonObjectRequest);







    }

}