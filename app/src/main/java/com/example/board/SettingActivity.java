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

    ArrayAdapter<String> dataAdapter;
    SharedPreferences saveSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_setting);

        init();

    }

    public void init() {

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);


        saveSetting = getSharedPreferences("settings",0);

        choiceOfSize = saveSetting.getInt("size",4);
        choiceOfColor = saveSetting.getString("color name",null);
        spinnerSize = findViewById(R.id.spinnerSize);
        spinnerColor = findViewById(R.id.spinnerColor);
        createSpinnerOfSize();
        createSpinnerOfColor();

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void createSpinnerOfSize() {
        sizeOption = new ArrayList<String>();
        for (int i = 3; i <= 10; i++)
            sizeOption.add(String.valueOf(i));
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sizeOption);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSize.setAdapter(dataAdapter);
        spinnerSize.setSelection(choiceOfSize - 3);


        spinnerSize.setOnItemSelectedListener(this);
    }

    private void createSpinnerOfColor() {
        SharedPreferences.Editor editor = saveSetting.edit();

        colorOption = new ArrayList<String>();
        colorOption.add("magenta");
        colorOption.add("green");colorOption.add("yellow");
        colorOption.add("black");colorOption.add("gray");
        colorOption.add("purple");colorOption.add("red");


        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colorOption);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(dataAdapter);




        for (int i = 0;i<7;i++)
        {
            if (spinnerColor.getAdapter().getItem(i).equals(choiceOfColor)){
                spinnerColor.setSelection(i);
                break;
            }
        }


        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = adapterView.getItemAtPosition(i).toString();


                switch (temp) {
                    case "magenta":
                        editor.putInt("color",Color.MAGENTA);
                        editor.putString("color name","magenta");
                        break;
                    case "green":
                        editor.putInt("color",Color.parseColor("#4CAF50"));
                        editor.putString("color name","green");
                        break;
                    case "yellow":
                        editor.putInt("color",Color.parseColor("#FFC107"));
                        editor.putString("color name","yellow");
                        break;
                    case "black":
                        editor.putInt("color",Color.BLACK);
                        editor.putString("color name","black");
                        break;
                    case "gray":
                        editor.putInt("color",Color.parseColor("#8E8E8E"));
                        editor.putString("color name","gray");
                        break;
                    case "purple":
                        editor.putInt("color",Color.parseColor("#5F4195"));
                        editor.putString("color name","purple");
                        break;
                    case "red":
                        editor.putInt("color",Color.parseColor("#C61E1E"));
                        editor.putString("color name","red");
                        break;
                }
                editor.commit();
                setResult(RESULT_OK, intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        SharedPreferences.Editor editor = saveSetting.edit();
        String temp = adapterView.getItemAtPosition(i).toString();
        editor.putInt("size", Integer.parseInt(temp));
        editor.commit();

        setResult(RESULT_OK, intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}