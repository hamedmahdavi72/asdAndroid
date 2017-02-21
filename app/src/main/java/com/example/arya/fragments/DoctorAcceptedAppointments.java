package com.example.arya.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import com.example.arya.agileapp.R;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ARYA on 2/21/2017.
 */
public class DoctorAcceptedAppointments extends FragmentActivity {


    ListView lv;
    String[] results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctor_accepted_appointments);

        try {
            lv = (ListView) findViewById(R.id.doctor_app_listView);
            JSONArray acceptedAppointments = new JSONArray(getIntent().getStringExtra("acceptedAppointments"));
            results = new String[acceptedAppointments.length()];

            for (int i = 0; i < acceptedAppointments.length(); i++) {
                JSONObject jsonObject = new JSONObject(acceptedAppointments.get(i).toString());
                if(jsonObject.has("customerUsername")){
                    results[i] = "\n\nCustomer Username: "+jsonObject.getString("customerUsername") + "\n\nAppointment Date: " + new Date(new Long(jsonObject.getString("appointmentDate")))+"\n\n";
                }
                else {
                    if(jsonObject.has("doctorUsername")){
                        results[i] = "\n\nDoctor Username: "+jsonObject.getString("doctorUsername") + "\n\nAppointment Date: " + new Date(new Long(jsonObject.getString("appointmentDate")))+"\n\n";
                    }
                }
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
