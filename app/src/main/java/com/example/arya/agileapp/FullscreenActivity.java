package com.example.arya.agileapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.example.arya.fragments.UserProfile;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity {

        Button b1;
        EditText ed1,ed2;
        HashMap<String, String> extraData = new HashMap<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fullscreen);

            b1 = (Button)findViewById(R.id.button);
            ed1 = (EditText)findViewById(R.id.editText);
            ed2 = (EditText)findViewById(R.id.editText2);
            CookieHandler.setDefault(new CookieManager());
        }

    @Override
    protected void onStart() {
            super.onStart();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true) {
                    Toast.makeText(getApplicationContext(),
                            "درحال ارسال...",Toast.LENGTH_SHORT).show();


// Optionally, you can just use the default CookieManager

                    String URL = "http://10.0.2.2:9000/login/";
                    RequestQueue queue = Volley.newRequestQueue(getApplication());
                    final RequestQueue queue2 = Volley.newRequestQueue(getApplication());
                    String URL2 = "http://10.0.2.2:9000/getUser";
                    final StringRequest stringRequest2 = new StringRequest(Request.Method.GET, URL2,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // your response

                                    try {
                                        JSONObject res = new JSONObject(response.toString());
                                        loadUserProfile(res);

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

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // your response
//                                    Log.d("response", response);
                                    try {
                                        JSONObject resJson = new JSONObject(response.toString());
                                        if(resJson.has("loginmsg")){
                                            queue2.add(stringRequest2);
                                            queue2.start();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "مشخصات وارد شده نادرست است.",Toast.LENGTH_SHORT).show();
                                        }
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
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            JSONObject jsonBody = new JSONObject();
                            try {
                                jsonBody.put("username", ed1.getText());
                                jsonBody.put("password", ed2.getText());
                                String your_string_json = jsonBody.toString();
                                return your_string_json.getBytes();
                            }catch (Exception e){}
                            return null;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
       /* b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               loadUserProfile();
           }
       });*/

    }

    private void loadUserProfile(JSONObject res){
            Intent intent = new Intent(this, UserProfile.class);
            intent.putExtra("userInfo", res.toString());
            startActivity(intent);
        }
}
