package com.example.arya.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arya.agileapp.R;
import com.example.arya.utils.date.JalaliCalendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARYA on 2/11/2017.
 */
public class DoctorPageAsUser extends FragmentActivity {


    String docId;

    private CheckBox first, second, third;
    private Button send;
    private TextView firstFrom, firstTo, secondFrom, secondTo, thirdFrom, thirdTo;

    public DoctorPageAsUser() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctor_page_as_user);

        first = (CheckBox) findViewById(R.id.firstConfirm);
        second = (CheckBox) findViewById(R.id.secondConfirm);
        third = (CheckBox) findViewById(R.id.thirdConfirm);

        send = (Button) findViewById(R.id.sendRequest);

        docId = getIntent().getStringExtra("doctorId");



        firstFrom = (TextView) findViewById(R.id.firstFromH);
        firstTo = (TextView) findViewById(R.id.firstToH);

        secondFrom = (TextView) findViewById(R.id.secondFromH);
        secondTo = (TextView) findViewById(R.id.secondToH);

        thirdFrom = (TextView) findViewById(R.id.thirdFromH);
        thirdTo = (TextView) findViewById(R.id.thirdToH);

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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAppointments();
            }
        });
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


    public void sendAppointMentRequests(final JSONObject payload){

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String URL = "http://10.0.2.2:9000/saveAppointments/";
                RequestQueue queue = Volley.newRequestQueue(getApplication());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equalsIgnoreCase("ok")){
                                    Toast.makeText(getApplicationContext(),"درخواست با موفقیت ثبت شد.", Toast.LENGTH_LONG).show();
                                    DoctorPageAsUser.this.finish();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"خطای داخلی!", Toast.LENGTH_LONG).show();
                    }
                })

                {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return payload.toString().getBytes();
                    }
                };
                queue.add(stringRequest);
                return null;
            }
        }.execute();
    }

    public void requestAppointments(){

        JalaliCalendar jalaliCalendar;
        JSONArray jsonArray = new JSONArray();
        JSONObject appointmentRequests = new JSONObject();

        Spinner sp11, sp12, sp13, sp21, sp22, sp23, sp31, sp32, sp33;

        sp11 = (Spinner) findViewById(R.id.firstDaySpinner);
        sp12 = (Spinner) findViewById(R.id.firstMonthSpinner);
        sp13 = (Spinner) findViewById(R.id.firstYearSpinner);

        sp21 = (Spinner) findViewById(R.id.secondDaySpinner);
        sp22 = (Spinner) findViewById(R.id.secondMonthSpinner);
        sp23 = (Spinner) findViewById(R.id.secondYearSpinner);

        sp31 = (Spinner) findViewById(R.id.thirdDaySpinner);
        sp32 = (Spinner) findViewById(R.id.thirdMonthSpinner);
        sp33 = (Spinner) findViewById(R.id.thirdYearSpinner);

        try {
            appointmentRequests.put("doctorUsername", docId);
            if (first.isChecked()) {
                JSONObject first = new JSONObject();

                first.put("fromHour", firstFrom.getText());
                first.put("toHour", firstTo.getText().toString());
                jalaliCalendar = new JalaliCalendar();
                jalaliCalendar.set(16,
                        Integer.parseInt(sp12.getSelectedItem().toString()),
                        Integer.parseInt(sp11.getSelectedItem().toString()));// year, month, day
                first.put("date", jalaliCalendar.getTime());
                jsonArray.put(first);
            }
            if (second.isChecked()) {
                JSONObject second = new JSONObject();
                second.put("fromHour", secondFrom.getText().toString());
                second.put("toHour", secondTo.getText().toString());
                jalaliCalendar = new JalaliCalendar();
                jalaliCalendar.set(Integer.parseInt(sp23.getSelectedItem().toString())
                        , Integer.parseInt(sp22.getSelectedItem().toString()),
                        Integer.parseInt(sp21.getSelectedItem().toString()));// year, month, day
                second.put("date", jalaliCalendar.getTime());
                jsonArray.put(second);
            }
            if (third.isChecked()) {
                JSONObject third = new JSONObject();
                third.put("fromHour", thirdFrom.getText().toString());
                third.put("toHour", thirdTo.getText().toString());
                jalaliCalendar = new JalaliCalendar();
                jalaliCalendar.set(Integer.parseInt(sp33.getSelectedItem().toString())
                        , Integer.parseInt(sp32.getSelectedItem().toString()),
                        Integer.parseInt(sp31.getSelectedItem().toString()));// year, month, day
                third.put("date", jalaliCalendar.getTime());
                jsonArray.put(third);
            }
            appointmentRequests.put("intervals", jsonArray);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),"خطای داخلی!", Toast.LENGTH_LONG).show();
        }
        sendAppointMentRequests(appointmentRequests);
    }
}
