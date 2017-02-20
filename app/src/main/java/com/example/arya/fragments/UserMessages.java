package com.example.arya.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.arya.agileapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ARYA on 2/20/2017.
 */
public class UserMessages extends FragmentActivity {

    public UserMessages(){}

    ListView lv;
    String[] results;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_messages);


        try {
            lv = (ListView) findViewById(R.id.messages_list_view);
            JSONArray messages = new JSONArray(getIntent().getStringExtra("userMessages"));
            results = new String[messages.length()];

            for (int i = 0; i < messages.length(); i++) {
                JSONObject jsonObject = new JSONObject(messages.get(i).toString());
                results[i] = "Subject: "+jsonObject.getString("subject") + "\nDescription: " + jsonObject.getString("issueReport");
            }

            // Create a List from String Array elements
            final List<String> message_list = new ArrayList<>(Arrays.asList(results));

            // Create an ArrayAdapter from List
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                    (this, android.R.layout.simple_list_item_1, message_list);

            // DataBind ListView with items from ArrayAdapter
            lv.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
