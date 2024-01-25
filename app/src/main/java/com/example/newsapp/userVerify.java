package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class userVerify extends AppCompatActivity {

    EditText EdEmail, EdOtp;
    Button btnVerify;
    TextView textView;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verify);

        EdEmail = findViewById(R.id.EditTextEmail);
        EdOtp = findViewById(R.id.EditTextOtp);
        btnVerify = findViewById(R.id.buttonVerifyClick);
        textView = findViewById(R.id.textTv);
        progressBar = findViewById(R.id.progress_circular);


        sharedPreferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);



        RequestQueue requestQueue = Volley.newRequestQueue(userVerify.this);

        // button Click to User Verify listen -------------------

        btnVerify.setOnClickListener(v->{
            progressBar.setVisibility(View.VISIBLE);
            String url = "https://sore-pear-fish-cuff.cyclic.app/api/v1/user-verify/"+EdEmail.getText().toString()+"/"+EdOtp.getText().toString();

            // JsonObject Api Request for get request-----------------
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        boolean success = jsonObject.getBoolean("success");
                        if (!success){
                            String error = jsonObject.getString("error");
                            textView.append("\nError is: "+error);
                        }
                        if (success){
                            String token = jsonObject.getString("token");
                            String user = jsonObject.getString("user");

                            textView.append("\n token"+token);
                            textView.append("\n userData"+user);
                            editor.putString("token", token);
                            editor.apply();


                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    textView.append("\n Error Response : "+error);

                }
            });

            requestQueue.add(jsonObjectRequest);

        });


    }
}