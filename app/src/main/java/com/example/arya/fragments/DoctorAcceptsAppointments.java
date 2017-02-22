package com.example.arya.fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arya.agileapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by ARYA on 2/22/2017.
 */
public class DoctorAcceptsAppointments extends FragmentActivity {

    ListView lv;
    String[] results;
    String[] appointmentIds;
    Calendar myCalendar = Calendar.getInstance();
    String selectedCustomerUsername;
    JSONArray jsonArray;
    String[] mongoId;
    int mongoIdPosition;

    public DoctorAcceptsAppointments(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_search_results);

        // Initializing a new String Array
        try {
            lv = (ListView) findViewById(R.id.javabha);
            jsonArray = new JSONArray(getIntent().getStringExtra("appointments"));
            results = new String[jsonArray.length()];
            appointmentIds = new String[results.length];
            mongoId = new String[results.length];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
//                appointmentIds[i] = jsonObject.getString("email");
                mongoId[i] = jsonObject.getString("id").toString();
                results[i] = "\n\nCustomer Username: "+jsonObject.getString("customerUsername");
                JSONArray intervals = jsonObject.getJSONArray("appointmentInterval");
                for(int j = 0 ; j < intervals.length() ; j ++){
                    results[i] += "\n\nAppointmentRequest "+(j+1)+":\n\n"+"Date: "+new Date(Long.parseLong(intervals.getJSONObject(j).getString("date")))+"\n\nTime Interval: "+intervals.getJSONObject(j).getString("fromHour")
                    +" - "+intervals.getJSONObject(j).getString("toHour")+"\n";
                }
            }

            if(results.length == 0){
                results = new String[1];
                results[0]="درخواست ملاقاتی وجود ندارد.";
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



    public void showTimeDialogBox(final Long date){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("زمان مورد نظر را انتخاب کنید");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mtext = input.getText().toString();
                acceptAppointmentForCustomer(mtext, date);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public void acceptAppointmentForCustomer(final String time, final Long date){
        String URL = "http://10.0.2.2:9000/doctor/acceptAppointmentRequest/";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest editRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
            }
        })

        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject appointmentData = new JSONObject();
                    Date newDate = new Date(date);
                    newDate.setHours(Integer.parseInt(time));
                    appointmentData.put("date", newDate.getTime());
                    appointmentData.put("id", new JSONObject(mongoId[mongoIdPosition]));
                    appointmentData.put("customerUsername", selectedCustomerUsername);
                    return appointmentData.toString().getBytes();
                }catch (Exception e){}
                return null;
            }
        };
        queue.add(editRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Toast toast= Toast.makeText(getApplicationContext(), myCalendar.getTime().toString(), Toast.LENGTH_SHORT);
                toast.show();
                showTimeDialogBox(myCalendar.getTimeInMillis());
            }

        };

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    selectedCustomerUsername = jsonArray.getJSONObject(position).getString("customerUsername");
                    mongoIdPosition = position;
                } catch (JSONException e) {
                    Toast toast= Toast.makeText(getApplicationContext(), "خطایی رخ داده است.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                new DatePickerDialog(DoctorAcceptsAppointments.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                // selected item
                /*Toast toast = Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT);
                toast.show();*/
               /* new AsyncTask<Void, Void, Void>() {
                    public Void doInBackground(Void... a) {
                        goToDoctor(selected);
                        return null;
                    }
                }.execute();
            }
        });*/
            }
        });
    }
}
