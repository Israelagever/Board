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

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//הופף את הactivity למסך מלא
        init();

    }

    public void init(){//פעולה שמאתחלת את האובייקטים
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);//מראה כפתור חזרה בלמעלה של המסך


        sp = getSharedPreferences("data",0);
        editor = sp.edit();

        message=(EditText)findViewById(R.id.etSms);
        sendSms =(Button)findViewById(R.id.btnSms);

        sendSms.setOnClickListener(this);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//מה קורה כשלוחצים על הכפתור חזרה
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (!message.getText().toString().equals("")) {
            permission();//בקשת הרשאה
            if (isGranted) {
                sendSmsFunction();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "massage cannot br null", Toast.LENGTH_LONG).show();

    }
    public void dialog()//בונה את הדיאלוג אם המשתמש לא נתן הרשאה ומנסה עוד פעם לשלוח הודעה
    {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(this);
        builder.setMessage("to contact with developer, the app need to access sms");//הסבר למשתמש
        builder.setTitle("Sms permission needed");
        builder.setCancelable(true);


        builder.setPositiveButton(
                        "Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)//אם המשתמש לוחץ allow
                            {
                                ActivityCompat.requestPermissions(SmsActivity.this, new String[]{Manifest.permission.SEND_SMS}, 200);//מבקש הרשאה
                            }
                        });
        builder.setNegativeButton(
                        "No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which)//אם המשתמש לוחץ no
                            {
                                dialog.cancel();//סוגר את הדיאלוג
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();//מציד את הדיאלוג
    }


    public void sendSmsFunction(){//פעולה ששלוחת sms למפתח
        String msg=message.getText().toString();//מוציא את ההודעה מהeditText


        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);


        SmsManager sms=SmsManager.getDefault();//מתחבר לsms של המערכת
        sms.sendTextMessage("0585115606", null, msg, pi,null);//שולח את ההודעה וחוזר למסך הראשי

        Toast.makeText(getApplicationContext(), "Message Sent successfully!", Toast.LENGTH_LONG).show();
    }

    public void permission() {//פעולה שמטפלת בהרשאה
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {//אם אין הרשאה
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {//אם זה פעם שניה שמבקשים הרשאה
                dialog();


            }
            else if (!sp.getBoolean("firstCheckPermission",false)){//אחרת אם זה פעם ראשונה שמבקשים הרשאה
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 100);//מבקרש הרשאה
                editor.putBoolean("firstCheckPermission",true);
                editor.commit();

            }
            else{//אם זה כבר פעם שלישית מפנים את המשתמש להגדרות של האפליקציה
                Toast.makeText(this, "Please allow sms permission setting", Toast.LENGTH_LONG);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);//הגדרת הintent להגדרות של האפליקציה
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);//הפניה להגדרות של האפליקציה הנוכחית
                this.startActivity(intent);
            }

        }
        else//יש הרשאה
            isGranted = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {//פעולה שמופעלת אחרי בקשת הרשאה
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//אם המשתמש הרשה
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