package com.example.arya.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arya.agileapp.R;
import org.json.JSONException;
import org.json.JSONObject;


public class UserProfile extends FragmentActivity {


    private TextView nameValue;
    private TextView mobileValue;
    private TextView nationalIdValue;
    private ImageView cris;



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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.profile, new PlaceholderFragment()).commit();
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

