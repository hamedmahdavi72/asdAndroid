package com.example.arya.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arya.agileapp.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARYA on 2/11/2017.
 */
public class DoctorPageAsUser extends FragmentActivity {


    String docId;

    public DoctorPageAsUser() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctor_page_as_user);

        docId = getIntent().getStringExtra("doctorId");

        //first
        List<Integer> firstDaySpinner =  new ArrayList<>();
        List<Integer> firstMonthSpinner =  new ArrayList<>();
        List<Integer> firstYearSpinner =  new ArrayList<>();

/*        //second
        List<Integer> secondDaySpinner =  new ArrayList<>();
        List<Integer> secondMonthSpinner =  new ArrayList<>();
        List<Integer> secondYearSpinner =  new ArrayList<>();

        //third
        List<Integer> thirdDaySpinner =  new ArrayList<>();
        List<Integer> thirdMonthSpinner =  new ArrayList<>();
        List<Integer> thirdYearSpinner =  new ArrayList<>();

        //Second Spinner


        //Third Spinner*/

        //First Spinner
        firstYearSpinner.add(1395);
        firstYearSpinner.add(1396);

        for(int i = 1 ; i <= 31 ; i++)
        {
            firstDaySpinner.add(i);
            if( i <= 12)
                firstMonthSpinner.add(i);
        }

        //Day
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, firstDaySpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.firstDaySpinner);
        sItems.setAdapter(adapter);

        //Month
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, firstMonthSpinner);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems1 = (Spinner) findViewById(R.id.firstMonthSpinner);
        sItems1.setAdapter(adapter2);

        //Year
        ArrayAdapter<Integer> adapter3 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, firstYearSpinner);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems2 = (Spinner) findViewById(R.id.firstYearSpinner);
        sItems2.setAdapter(adapter3);


        //Day
        ArrayAdapter<Integer> adapter11 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, firstDaySpinner);

        adapter11.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems11 = (Spinner) findViewById(R.id.secondDaySpinner);
        sItems11.setAdapter(adapter11);

        //Month
        ArrayAdapter<Integer> adapter12 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, firstMonthSpinner);

        adapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems12 = (Spinner) findViewById(R.id.secondMonthSpinner);
        sItems12.setAdapter(adapter12);

        //Year
        ArrayAdapter<Integer> adapter13 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, firstYearSpinner);

        adapter13.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems13 = (Spinner) findViewById(R.id.secondYearSpinner);
        sItems13.setAdapter(adapter13);


        //Day
        ArrayAdapter<Integer> adapter21 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, firstDaySpinner);

        adapter21.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems21 = (Spinner) findViewById(R.id.thirdDaySpinner);
        sItems21.setAdapter(adapter21);

        //Month
        ArrayAdapter<Integer> adapter22 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, firstMonthSpinner);

        adapter22.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems22 = (Spinner) findViewById(R.id.thirdMonthSpinner);
        sItems22.setAdapter(adapter22);

        //Year
        ArrayAdapter<Integer> adapter23 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, firstYearSpinner);

        adapter23.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems23 = (Spinner) findViewById(R.id.thirdYearSpinner);
        sItems23.setAdapter(adapter23);



    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.doctor_page_as_user_actionbar:
                loadDoctorInfo(docId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctorinfomenu, menu);
        return true;
    }

    public void loadDoctorInfo(String docId){

        final String[] jsonResponse = new String[1];
        String URL = "http://10.0.2.2:9000/doctors/info/"+docId;
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            jsonResponse[0] = "Name: \t"+jsonObject.getString("firstName")+" "+jsonObject.getString("lastName")
                            +"\nSpeciality: \t"+jsonObject.getString("speciality")
                            +"\nClinic Address: \t"+jsonObject.getString("clinicAddress")
                            +"\nClinic Phone Number: \t"+jsonObject.getString("clinicPhoneNumber");
                            showInfoDialog(jsonResponse[0]);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
            }
        })

        {
        };

        queue.add(stringRequest);

    }

    public void showInfoDialog(String dialog){
        Intent intent = new Intent(getApplicationContext(), DoctorPageAsUserInfo.class);
        intent.putExtra("docInfo", dialog);
        startActivity(intent);
    }

    public void requestAppointments(){

    }
}
