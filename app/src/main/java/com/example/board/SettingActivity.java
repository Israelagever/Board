package com.example.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerSize, spinnerColor;
    List<String> sizeOption, colorOption;

    Intent intent;
    int choiceOfSize;
    String  choiceOfColor;


    ArrayAdapter<String> dataAdapter;//אובייקט שמכין את הarrayList לspinner
    SharedPreferences saveSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//הופף את הactivity למסך מלא

        setContentView(R.layout.activity_setting);

        init();

    }

    public void init() {//פעולה שמאתחלת את האובייקטים ומקבלת מהSharedPreferences את ההגדרות הנוכחיות

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);//מראה כפתור חזרה בלמעלה של המסך


        saveSetting = getSharedPreferences("data",0);

        choiceOfSize = saveSetting.getInt("size",4);
        choiceOfColor = saveSetting.getString("colorOfTile name",null);
        spinnerSize = findViewById(R.id.spinnerSize);
        spinnerColor = findViewById(R.id.spinnerColor);
        createSpinnerOfSize();
        createSpinnerOfColor();

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//מה קורה כשלוחצים על הכפתור חזרה
        if (item.getItemId() == android.R.id.home) {
            this.finish();//סוגר את הactivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void createSpinnerOfSize() {//יצירת הספינר של הגדרת הגודל של הלוח
        sizeOption = new ArrayList<String>();
        for (int i = 3; i <= 10; i++)
            sizeOption.add(String.valueOf(i));//הכנסת הגדלים לArrayList
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sizeOption);//יצירת המתאם עם הרשימה של הגדלים
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//שינוי העיצוב של התצוגה כשהספינר נפתח

        spinnerSize.setAdapter(dataAdapter);
        spinnerSize.setSelection(choiceOfSize - 3);//מכיוון שהגודל הכי קטן הוא 3 אז הבחירה צריכה להיות פחות 3


        spinnerSize.setOnItemSelectedListener(this);
    }

    private void createSpinnerOfColor() {//יצירת הספינר של הגדרת הצבע של האריחים


        colorOption = new ArrayList<String>();
        colorOption.add("magenta");
        colorOption.add("green");colorOption.add("yellow");
        colorOption.add("black");colorOption.add("gray");
        colorOption.add("purple");colorOption.add("red");//הכנסת סוגי הצבעים לרשימה


        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colorOption);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(dataAdapter);




        for (int i = 0;i<7;i++)
        {
            if (spinnerColor.getAdapter().getItem(i).equals(choiceOfColor)){
                spinnerColor.setSelection(i);//מציאת הצבע שמוגדר עכשיו
                break;
            }
        }
        spinnerColor.setOnItemSelectedListener(this);//
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {//מתי שנבחר משהו
        SharedPreferences.Editor editor = saveSetting.edit();

        if (view.getParent() == spinnerSize) {//בודק אם נלחץ בספינר של הגדלים

            String temp = adapterView.getItemAtPosition(i).toString();//מוצא את הבחירה
            editor.putInt("size", Integer.parseInt(temp));//מעדכן את ההגדרה
            editor.commit();

            setResult(RESULT_OK, intent);
        }
        else//נלחץ בספינר של הצבעים
        {
            String temp = adapterView.getItemAtPosition(i).toString();


            switch (temp) {//מעדכן איזה צבע להגדיר לפי הבחירה
                case "magenta":
                    editor.putInt("color",Color.MAGENTA);
                    editor.putString("colorOfTile name","magenta");
                    break;
                case "green":
                    editor.putInt("color",Color.parseColor("#4CAF50"));
                    editor.putString("colorOfTile name","green");
                    break;
                case "yellow":
                    editor.putInt("color",Color.parseColor("#FFC107"));
                    editor.putString("colorOfTile name","yellow");
                    break;
                case "black":
                    editor.putInt("color",Color.BLACK);
                    editor.putString("colorOfTile name","black");
                    break;
                case "gray":
                    editor.putInt("color",Color.parseColor("#8E8E8E"));
                    editor.putString("colorOfTile name","gray");
                    break;
                case "purple":
                    editor.putInt("color",Color.parseColor("#5F4195"));
                    editor.putString("colorOfTile name","purple");
                    break;
                case "red":
                    editor.putInt("color",Color.parseColor("#C61E1E"));
                    editor.putString("colorOfTile name","red");
                    break;
            }
            editor.commit();
            setResult(RESULT_OK, intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}