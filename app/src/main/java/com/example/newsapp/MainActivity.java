package com.example.newsapp;



import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {




    private static final String TAG = "response";
    TextView textView;
    public RequestQueue requestQueue;
    ProgressBar progressBar;
    Button button;
    EditText nameEd, passwordEd, emailEd;
    String url = "https://sore-pear-fish-cuff.cyclic.app/api/v1/register";

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.ResponseText);
        progressBar = findViewById(R.id.progress_circular);
        button = findViewById(R.id.buttonClick);
        nameEd = findViewById(R.id.nameEd);
        passwordEd = findViewById(R.id.passwordEd);
        emailEd = findViewById(R.id.emailEd);

        sharedPreferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);


        requestQueue = Volley.newRequestQueue(MainActivity.this);




        // button click -----------------------------------

        button.setOnClickListener(v->{
            progressBar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", nameEd.getText().toString());
            jsonObject.put("password", passwordEd.getText().toString());
            jsonObject.put("email", emailEd.getText().toString());
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
                        button.setVisibility(View.GONE);
                        try{
                            JSONObject jsonObject1 = new JSONObject(response.toString());

                            boolean success = jsonObject1.getBoolean("success");


                            if (!success){
                                String error = jsonObject1.getString("error");

                                textView.append("\n"+error);
                            }

                            if (success){
                                String token = jsonObject1.getString("token");
                                String user = jsonObject1.getString("user");
                                // user Expend ----------name ---- email ------otp ----
                                JSONObject jsonObject2 = new JSONObject(user);
                                String name = jsonObject2.getString("name");
                                String email = jsonObject2.getString("email");
                                String otp = jsonObject2.getString("otp");

                                editor = sharedPreferences.edit();
                                editor.putString("token",token);
                                editor.putString("email", email);
                                editor.apply();

                                textView.append("\n" + token);
                                textView.append("\n" + name);
                                textView.append("\n" + sharedPreferences.getString("email","notUser"));
                                textView.append("\n" + otp);
                                textView.append("\n\n" + user);
                            }


                        }catch (JSONException err){
                            textView.append("\n JSON =========="+err);
                        }

                        Log.d(TAG, "JsonObjectRequest onResponse: " + response);
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
                        textView.append("\n"+error.toString());
                        textView.append("\n"+error.networkResponse);
                        textView.append("\n"+error.networkResponse.statusCode);
                        textView.append("\n"+error.networkResponse.allHeaders);
                        Log.e(TAG, "JsonObjectRequest onErrorResponse: " + error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json");
                return hashMap;
            }
        };



           requestQueue.add(jsonObjectRequest);
       });







    }



}