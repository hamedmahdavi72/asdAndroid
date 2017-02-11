package com.example.arya.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.example.arya.agileapp.R;

/**
 * Created by ARYA on 2/11/2017.
 */
public class DoctorPageAsUserInfo extends FragmentActivity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctor_page_as_user_info);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("مشخصات پزشک");

        // set dialog message
        alertDialogBuilder
                .setMessage(getIntent().getStringExtra("docInfo"))
                .setCancelable(false)
                .setNegativeButton("بستن",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                        DoctorPageAsUserInfo.this.finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
