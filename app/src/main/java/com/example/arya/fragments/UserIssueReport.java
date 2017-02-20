package com.example.arya.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arya.agileapp.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ARYA on 2/20/2017.
 */
public class UserIssueReport extends FragmentActivity {


    Spinner issueSubject;
    EditText issueReport;
    List<String> issueSubjects =  new ArrayList<>();
    Button sendIssue;

    public UserIssueReport(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_issue_report);

        issueReport = (EditText) findViewById(R.id.issue_report_value);
        issueSubject = (Spinner) findViewById(R.id.issue_subject);
        sendIssue = (Button) findViewById(R.id.sendIssue);


        issueSubjects.add("ورود و خروج به حساب کاربری");
        issueSubjects.add("رزرو وقت ملاقات");
        issueSubjects.add("ملاقاتهای تائید شده");
        issueSubjects.add("دیگر");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, issueSubjects);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        issueSubject.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sendIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("subject", issueSubject.getSelectedItem().toString());
                    jsonObject.put("issueReport", issueReport.getText().toString());
                    jsonObject.put("issueDate", new Date().toString());

                    sendUserIssue(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendUserIssue(final JSONObject jsonObject){
        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String URL = "http://10.0.2.2:9000/issueReport/";
                RequestQueue queue = Volley.newRequestQueue(getApplication());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response != null){
                                    Toast.makeText(getApplicationContext(),"مشکل با موفقیت ارسال شد.", Toast.LENGTH_LONG).show();
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
                        return jsonObject.toString().getBytes();
                    }
                };
                queue.add(stringRequest);
                return null;
            }
        }.execute();
    }
}
