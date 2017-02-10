package com.example.arya.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arya.agileapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ARYA on 2/10/2017.
 */
public class UserSearchResults extends FragmentActivity {

    public UserSearchResults() {
        // Required empty public constructor
    }


    ListView lv;
    String[] results;
    String[] doctorIds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_search_results);

        // Initializing a new String Array
        try {
            lv = (ListView) findViewById(R.id.javabha);
            JSONObject searchResults = new JSONObject(getIntent().getStringExtra("searchResults"));
            JSONArray jsonArray = searchResults.getJSONArray("results");
            results = new String[jsonArray.length()];
            doctorIds = new String[results.length];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                doctorIds[i] = jsonObject.getString("email");
                results[i] = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
            }

            // Create a List from String Array elements
            final List<String> doctor_list = new ArrayList<>(Arrays.asList(results));

            // Create an ArrayAdapter from List
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                    (this, android.R.layout.simple_list_item_1, doctor_list);

            // DataBind ListView with items from ArrayAdapter
            lv.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                final String selected = doctorIds[position];
                Toast toast = Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT);
                toast.show();
                new AsyncTask<Void, Void, Void>() {
                    JSONObject jsonBody = new JSONObject();

                    public Void doInBackground(Void... a) {
                        goToDoctor(selected);
                        return null;
                    }
                }.execute();
            }
        });
    }

    public void goToDoctor(String docId){
        String URL = "http://10.0.2.2:9000/doctors/page/"+docId;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest editRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
            }
        });
        queue.add(editRequest);
    }
}



