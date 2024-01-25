package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    public static String todoId;
    TextView textView;
    Button button;
    ProgressBar progressBar;
    EditText editText;
    RecyclerView recyclerView;
    String url = "https://sore-pear-fish-cuff.cyclic.app/api/v1/note-create";
    String getUrl = "https://sore-pear-fish-cuff.cyclic.app/api/v1/note-all/";
    SharedPreferences sharedPreferences;
    String token;
    ArrayList<HashMap<String,String>>arrayList = new ArrayList<>();
    HashMap<String,String>hashMap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        textView = findViewById(R.id.textTv);
        button = findViewById(R.id.createNoteButton);
        progressBar = findViewById(R.id.progress_circular);
        editText = findViewById(R.id.edText);
        recyclerView = findViewById(R.id.recyclerview);

        sharedPreferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);

        token = sharedPreferences.getString("token", "noToken");


        // create note ----------------- on click ---------------
        button.setOnClickListener(v->{
            progressBar.setVisibility(View.VISIBLE);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id",todoId);
                jsonObject.put("note",editText.getText().toString());


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            request(url,jsonObject);

            // refresh activity ----------------------
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);


        });



        // all note get -----------------------------------------

        RequestQueue requestQueue = Volley.newRequestQueue(CreateNote.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl+todoId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if (success){
                        JSONArray data = response.getJSONArray("data");
                        for (int x=0; x<data.length(); x++){
                            JSONObject note = data.getJSONObject(x);
                            String noteText = note.getString("note");
                            String noteId = note.getString("_id");
                            String createDate = note.getString("createdAt");

                            hashMap = new HashMap<>();
                            hashMap.put("noteText", noteText);
                            hashMap.put("noteId", noteId);
                            hashMap.put("CreateDate", createDate);
                            arrayList.add(hashMap);
                        }
                        // Recycler View Add adapter and -----------------------

                        MyAdapter adapter = new MyAdapter();
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CreateNote.this));

                    }
                    if (!success){
                        String error = response.getString("error");
                        Toast.makeText(CreateNote.this, "Api Error : "+error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String>hashMap1 = new HashMap<>();
                hashMap1.put("Authorization",token);
                return hashMap1;
            }
        };
        requestQueue.add(jsonObjectRequest);


    }
    // -----------------------------------------------------------

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder>{

        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.news_item, parent, false);
            return new myViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

            hashMap = arrayList.get(position);
            String id = hashMap.get("noteId");
            String text = hashMap.get("noteText");
            String date = hashMap.get("CreateDate");

            holder.textView.setText(text);
            holder.text_subtitle.setText(date);

            holder.text_subtitle.setOnClickListener(v->{
                // note delete ---------------------------------------
                RequestQueue requestQueue = Volley.newRequestQueue(CreateNote.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,"https://sore-pear-fish-cuff.cyclic.app/api/v1/note-delete/"+id
                        , null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean Success = response.getBoolean("success");
                            if (Success){
                                String massage = response.getString("massage");
                                Toast.makeText(CreateNote.this, "++"+massage, Toast.LENGTH_SHORT).show();

                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);

                            }
                            if (!Success){
                                String error = response.getString("error");
                                Toast.makeText(CreateNote.this, "++"+error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateNote.this, " =="+error, Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String,String>hashMap = new HashMap<>();
                        hashMap.put("Authorization",token);
                        return hashMap;
                    }
                };

                requestQueue.add(jsonObjectRequest);
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private class myViewHolder extends RecyclerView.ViewHolder{

            TextView textView,text_subtitle;
            public myViewHolder(@NonNull View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.text_title);
                text_subtitle = itemView.findViewById(R.id.text_subtitle);


            }
        }

    }



    // create note request ---------------------------------------
    private void request(String url,JSONObject jsonObject){
        RequestQueue requestQueue = Volley.newRequestQueue(CreateNote.this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressBar.setVisibility(View.GONE);

                try {
                    boolean success = response.getBoolean("success");
                    if (success){
                        Toast.makeText(CreateNote.this, "Note Create Success", Toast.LENGTH_SHORT).show();
                    }
                    if (!success){
                        String error = response.getString("error");
                        Toast.makeText(CreateNote.this, "Error : "+error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateNote.this, "onError: "+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String>hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json");
                hashMap.put("Authorization",token);
                return hashMap;
            }
        };

        requestQueue.add(jsonObjectRequest);


    }

}