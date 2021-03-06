package com.example.arya.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arya.agileapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UserProfile extends FragmentActivity {


    private TextView nameValue;
    private TextView mobileValue;
    private TextView nationalIdValue;
    private ImageView cris;
    private boolean isCustomer = true;
    private boolean seenOnce = false;



    public UserProfile() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_profile);
        nameValue = (TextView) findViewById(R.id.nameValue);
        mobileValue = (TextView)  findViewById(R.id.mobileValue);
        nationalIdValue = (TextView)  findViewById(R.id.nationalIdValue);

        try {
            JSONObject info = new JSONObject(getIntent().getStringExtra("userInfo"));
            nameValue.setText(info.getString("firstName")+" "+ info.getString("lastName"));
            mobileValue.setText(info.getString("mobileNumber"));
            nationalIdValue.setText(info.getString("nationalId"));
            if(info.has("supportedInsuranceCompanies")){
                ImageView profileImage = (ImageView) findViewById(R.id.profileIcon);
                profileImage.setImageResource(0);
                profileImage.setImageResource(R.drawable.profpic);
                profileImage.setBackground(getResources().getDrawable(R.drawable.profpic));
                isCustomer = false;
            }
            else {
                isCustomer = true;
                ImageView profileImage = (ImageView) findViewById(R.id.profileIcon);
                profileImage.setImageResource(0);
                profileImage.setImageResource(R.drawable.users1);
                profileImage.setBackground(getResources().getDrawable(R.drawable.users1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.clear();
        if(isCustomer && !seenOnce){
            menu.add(Menu.NONE, 91110258, menu.NONE,  "جستجوی پزشکان");
            menu.add(Menu.NONE, 91110261, menu.NONE,  "ملاقاتهای کاربر");
            seenOnce = true;
            return true;
        }
        else {
            if(!isCustomer && !seenOnce){
                menu.add(Menu.NONE, 91110259, menu.NONE,  "قرار ملاقات های تایید شده");
                menu.add(Menu.NONE, 91110260, menu.NONE,  "قرارهای ملاقات");
                seenOnce = true;
                return true;
            }
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.userprofilemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.errorReporting:
                loadIssueReportPage();
                return true;
            case 91110258:
                loadSearchPage();
                return true;
            case 91110259:
                getAcceptedAppointments();
                //showHelp();
                return true;
            case 91110260:
                loadListOfRequests();
                return true;
            case R.id.userMessages:
                getUserMessages();
                return true;
            case 91110261:
                getAcceptedAppointments();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void loadListOfRequests(){
        String URL = "http://10.0.2.2:9000/doctor/appointmentRequests";
        getDoctorAppointments(URL);
    }

    private void loadListOfRequestsPage(JSONArray jsonArray){
        Intent intent = new Intent(getApplicationContext(), DoctorAcceptsAppointments.class);
        intent.putExtra("appointments", jsonArray.toString());
        startActivity(intent);
    }

    private void getDoctorAppointments(final String URL){
        new AsyncTask<Void,Void,Void>() {
            public Void doInBackground(Void... a) {
                String URL2 = URL;
                RequestQueue queue = Volley.newRequestQueue(getApplication());
                final StringRequest stringRequest2 = new StringRequest(Request.Method.GET, URL2,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // your response
                                try {
                                    JSONArray res = new JSONArray(response.toString());
                                    loadListOfRequestsPage(res);

                                } catch (JSONException e) {}

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    }
                })

                {
                };
                queue.add(stringRequest2);
                return null;
            }
        }.execute();
    }


    public void loadAcceptedAppointmentsPage(JSONArray jsonArray){
        Intent intent = new Intent(getApplicationContext(), DoctorAcceptedAppointments.class);
        intent.putExtra("acceptedAppointments", jsonArray.toString());
        startActivity(intent);
    }


    private void requestForAcceptedAppointments(final String URL){
        new AsyncTask<Void,Void,Void>() {
            public Void doInBackground(Void... a) {
                String URL2 = URL;
                RequestQueue queue = Volley.newRequestQueue(getApplication());
                final StringRequest stringRequest2 = new StringRequest(Request.Method.GET, URL2,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // your response
                                try {
                                    JSONArray res = new JSONArray(response.toString());
                                    loadAcceptedAppointmentsPage(res);

                                } catch (JSONException e) {
                                    loadUserMessagePage(response.toString());
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
                queue.add(stringRequest2);
                return null;
            }
        }.execute();
    }

    public void getAcceptedAppointments(){
        if(!isCustomer){
            requestForAcceptedAppointments("http://10.0.2.2:9000/doctor/getDoctorAppointments");
        }
        else {
            requestForAcceptedAppointments("http://10.0.2.2:9000/customer/appointments");
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.profile, new PlaceholderFragment()).commit();
    }


    private void getUserMessages(){
        new AsyncTask<Void,Void,Void>() {
            public Void doInBackground(Void... a) {
                String URL2 = "http://10.0.2.2:9000/getMessages";
                RequestQueue queue = Volley.newRequestQueue(getApplication());
                final StringRequest stringRequest2 = new StringRequest(Request.Method.GET, URL2,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // your response
                                try {
                                    JSONArray res = new JSONArray(response.toString());
                                    loadUserMessagePage(res);

                                } catch (JSONException e) {
                                    loadUserMessagePage(response.toString());
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
                queue.add(stringRequest2);
                return null;
            }
        }.execute();
    }


    private void loadIssueReportPage(){
        Intent intent = new Intent(getApplicationContext(), UserIssueReport.class);
        startActivity(intent);
    }

    private void loadUserMessagePage(JSONArray userMessages){
        Intent intent = new Intent(getApplicationContext(), UserMessages.class);
        intent.putExtra("userMessages", userMessages.toString());
        startActivity(intent);
    }

    private void loadUserMessagePage(String userMessages){
        Intent intent = new Intent(getApplicationContext(), UserMessages.class);
        intent.putExtra("userMessages", userMessages.toString());
        startActivity(intent);
    }

    private void loadSearchPage() {
        Intent intent = new Intent(getApplicationContext(), UserSearch.class);
        startActivity(intent);
    }

    public static class PlaceholderFragment extends Fragment{

        public PlaceholderFragment(){

        }

        public void saveEdits(final JSONObject json){
            String URL = "http://10.0.2.2:9000/edit/";
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            StringRequest editRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // your response
//                                    Log.d("response", response);
                            try {
                                JSONObject resJson = new JSONObject(response.toString());
                                if(resJson.has("cusedit"))
                                    Toast.makeText(getActivity().getApplicationContext(), resJson.getString("cusedit").toString(), Toast.LENGTH_SHORT).show();
                                else{
                                    if(resJson.has("docedit"))
                                        Toast.makeText(getActivity().getApplicationContext(), resJson.getString("docedit").toString(), Toast.LENGTH_SHORT).show();
                                    else {
                                        Toast.makeText(getActivity().getApplicationContext(), "خطایی رخ داده است...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
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
                        String your_string_json = json.toString();
                        return your_string_json.getBytes();
                    }catch (Exception e){}
                    return null;
                }
            };
            queue.add(editRequest);
        }

        @Override
        public  View  onCreateView(LayoutInflater inflater, ViewGroup container
                , Bundle savedInstanceState){
             View rootView = inflater.inflate(R.layout.fragment_user_profile,
                    container, false);


            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Button saveEdits = (Button) getActivity().findViewById(R.id.saveEdits);
            saveEdits.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity().getApplicationContext(),"ارسال تغییرات...", Toast.LENGTH_SHORT).show();
                    new AsyncTask<Void,Void,Void>() {
                        JSONObject jsonBody = new JSONObject();
                        public Void doInBackground(Void... a) {
                            TextView newPassword = (TextView) getView().findViewById(R.id.newPassword);

                            TextView confirmPassword = (TextView) getActivity().findViewById(R.id.confirmPassword);

                            if (confirmPassword.getText().toString().length() == 0)
                                newPassword = (TextView) getView().findViewById(R.id.confirmPassword);

                            TextView mobile = (TextView) getActivity().findViewById(R.id.mobileValue);
                            TextView name = (TextView) getActivity().findViewById(R.id.nameValue);
                            String p = newPassword.getText().toString();
                            String c = confirmPassword.getText().toString();



                            try {
                                jsonBody.put("mobileNumber", mobile.getText().toString());
                                jsonBody.put("nationalId", "0016464923");
                                jsonBody.put("firstName", name.getText().toString().split(" ")[0]);
                                jsonBody.put("lastName", name.getText().toString().split(" ")[1]);
                                jsonBody.put("password", p);
                                jsonBody.put("confirmPassword", c);

                                saveEdits(jsonBody);
                            } catch (JSONException e) {
                                return null;
                            }
                            return null;
                        }
                    }.execute();
                }
            });
        }
    }
}

