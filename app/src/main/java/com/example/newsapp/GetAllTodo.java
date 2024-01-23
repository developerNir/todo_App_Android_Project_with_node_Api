package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.Objects;

public class GetAllTodo extends AppCompatActivity {

    String TAG = "todoGet";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView textView;
    ArrayList<HashMap<String,String>>arrayList = new ArrayList<>();
    HashMap<String,String>hashMap;
    String url = "https://sore-pear-fish-cuff.cyclic.app/api/v1/all-todo";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_todo);


        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progress_circularShow);
        textView = findViewById(R.id.errorHand);

        String token = MainActivity.sharedPreferences.getString("token", "");





        RequestQueue requestQueue = Volley.newRequestQueue(GetAllTodo.this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray allTodo = data.getJSONArray("allTodo");



                        for (int x=0; x<allTodo.length(); x++){
                            JSONObject todo = allTodo.getJSONObject(x);
                            String title = todo.getString("title");
                            String description = todo.getString("description");
                            String priority = todo.getString("priority");
                            String createDate = todo.getString("createdAt");

                            // add array list data ------------

                            hashMap = new HashMap<>();
                            hashMap.put("title",title);
                            hashMap.put("des",description);
                            hashMap.put("priority", priority);
                            hashMap.put("createDate",createDate);
                            arrayList.add(hashMap);
//                            Log.d(TAG, "onResponse: title: \n"+title);
//                            Log.d(TAG, "onResponse: description: \n"+description);
//                            Log.d(TAG, "onResponse: priority: \n"+priority);


                        }

                        // create Adapter and set Adapter with recycler view ---


                        myAdapter adapter = new myAdapter();
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(GetAllTodo.this));



                    }
                    if (!success){
                        String error = jsonObject.getString("error");
                        Log.d(TAG, "onResponse: Error"+error);
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(error);
                    }



                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: error Listener: "+error);
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", token);
                return headers;
            }
        };

        // add Request Queue this JsonObjectRequest ----------------------------

        requestQueue.add(jsonObjectRequest);



    }


    // create RecyclerView Adapter -------------------------------------------

    private class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder>{


        @NonNull
        @Override
        public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.splash_view, parent, false);
            return new myViewHolder(view);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

            hashMap = arrayList.get(position);
            String title = hashMap.get("title");
            String des = hashMap.get("des");
            String piro = hashMap.get("priority");
            String myDate = hashMap.get("createDate");

            holder.titleTv.setText(title);
            holder.DesTv.setText(des);
            holder.DateTv.setText(myDate);
            holder.priorityTv.setText(piro);
            if (Objects.equals(piro, "rad")) {
                holder.priorityTv.setBackground(getDrawable(R.drawable.red_shape));
            }
            if (Objects.equals(piro, "green")){
                holder.priorityTv.setBackground(getDrawable(R.drawable.green_shape));
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private class myViewHolder extends RecyclerView.ViewHolder{

            TextView titleTv,DesTv,DateTv,priorityTv;
            public myViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTv = itemView.findViewById(R.id.titleTv);
                DesTv = itemView.findViewById(R.id.DescriptionTv);
                DateTv = itemView.findViewById(R.id.dateTv);
                priorityTv = itemView.findViewById(R.id.pirorityTv);
                

            }
        }

    }


}