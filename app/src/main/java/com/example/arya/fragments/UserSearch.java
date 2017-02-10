package com.example.arya.fragments;

import android.content.Intent;
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
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ARYA on 2/10/2017.
 */
public class UserSearch extends FragmentActivity {

    public UserSearch() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_search);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.user_search_doctor, new UserSearch.UserSearchPage()).commit();
    }

    public static class UserSearchPage extends Fragment {

        public UserSearchPage(){

        }

        void showResults(JSONObject json){
            Intent intent = new Intent(getActivity().getApplicationContext(), UserSearchResults.class);
            intent.putExtra("searchResults", json.toString());
            startActivity(intent);
        }

        public void search(final JSONObject json){
            String URL = "http://10.0.2.2:9000/search/";
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            StringRequest editRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                showResults(new JSONObject(response));
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container
                , Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_user_search,
                    container, false);


            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Button search = (Button) getActivity().findViewById(R.id.searchButton);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity().getApplicationContext(),"در حال جستحو...", Toast.LENGTH_SHORT).show();
                    new AsyncTask<Void,Void,Void>() {
                        JSONObject jsonBody = new JSONObject();
                        public Void doInBackground(Void... a) {


                            TextView speciality = (TextView) getActivity().findViewById(R.id.specialityValue);
                            TextView name = (TextView) getActivity().findViewById(R.id.searchNameValue);
                            TextView areaName = (TextView) getActivity().findViewById(R.id.searchAreaName);



                            try {
                                if(name.getText() != null && name.getText().toString().length() != 0){
                                    jsonBody.put("firstName", name.getText().toString().split(" ")[0]);
                                    jsonBody.put("lastName", name.getText().toString().split(" ")[1]);
                                }
                                else {
                                    jsonBody.put("firstName", null);
                                    jsonBody.put("lastName", null);
                                }
                                if(speciality.getText().toString() != null && speciality.getText().toString().length() !=0)
                                    jsonBody.put("speciality", speciality.getText().toString());
                                else jsonBody.put("speciality", null);

                                jsonBody.put("areaName", areaName.getText().toString());

                                search(jsonBody);
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
