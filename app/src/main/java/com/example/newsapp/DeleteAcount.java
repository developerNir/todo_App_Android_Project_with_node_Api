package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class DeleteAcount extends AppCompatActivity {

    String url = "https://sore-pear-fish-cuff.cyclic.app/api/v1/user-delete";
    private static final String TAG = "deleteResponse";

    TextView textView;
    SharedPreferences sharedPreferences;
    String shareToken;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_acount);

        textView = findViewById(R.id.textViewToken);


        sharedPreferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);

        shareToken = sharedPreferences.getString("token", "");



        if (shareToken.length() >0) {
            textView.append("mytoken is: \n" + shareToken);
        }else {
            textView.setText("no Token");
        }


        // user delete account -----------------------------
        RequestQueue requestQueue = Volley.newRequestQueue(DeleteAcount.this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    boolean success = jsonObject.getBoolean("success");
                    if (!success){
                        String error = jsonObject.getString("error");
                        Log.d(TAG, "onResponse: Server"+error);
                    }
                    if (success){
                        String data = jsonObject.getString("data");
                        String massage = jsonObject.getString("massage");
                        Log.d(TAG, "onResponse: Success: "+data+" "+massage);
                        Toast.makeText(DeleteAcount.this, "Delete success"+massage, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Log.d(TAG, "onResponse: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Authorization",shareToken);
                return hashMap;
            }
        };

        requestQueue.add(jsonObjectRequest);


    }
}