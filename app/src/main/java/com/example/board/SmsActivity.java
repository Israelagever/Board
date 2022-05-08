package com.example.board;

import android.Manifest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.app.PendingIntent;
import android.content.Intent;
import android.provider.Settings;
import android.telephony.SmsManager;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SmsActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    EditText message;
    Button sendSms;
    boolean isGranted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        sp = getSharedPreferences("data",0);
        editor = sp.edit();

        message=(EditText)findViewById(R.id.etSms);
        sendSms =(Button)findViewById(R.id.btnSms);

        //Performing action on button click
        sendSms.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        permission();
        if (isGranted) {
            sendSmsFun();
        }

    }
    public void dialog()
    {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(this);
        builder.setMessage("to contact with developer, the app need to access sms");
        builder.setTitle("Sms permission needed");
        builder.setCancelable(false);

        builder.setPositiveButton(
                        "Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                ActivityCompat.requestPermissions(SmsActivity.this, new String[]{Manifest.permission.SEND_SMS}, 200);
                            }
                        });
        builder.setNegativeButton(
                        "No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void sendSmsFun(){
        String msg=message.getText().toString();

        //Getting intent and PendingIntent instance
        Intent intent=new Intent(getApplicationContext(),SmsActivity.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage("0585115606", null, msg, pi,null);


        Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                Toast.LENGTH_LONG).show();
    }

    public void permission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                dialog();


            }
            else if (!sp.getBoolean("firstCheckPermission",false)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 100);
                editor.putBoolean("firstCheckPermission",true);
                editor.commit();

            }
            else{//go to setting applications
                Toast.makeText(this, "Please allow sms permission setting", Toast.LENGTH_LONG);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                this.startActivity(intent);
            }

        }
        else
            isGranted = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isGranted = true;
                System.out.println("PLAYGROUND Permission has been granted");

            } else {
                System.out.println("PLAYGROUND Permission has been denied or request cancelled");
                isGranted = false;
            }
        }
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isGranted = true;
                System.out.println("PLAYGROUND Permission has been granted");

            } else {

                isGranted = false;
            }
        }
    }

}