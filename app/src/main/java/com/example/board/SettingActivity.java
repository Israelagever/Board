package com.example.board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerSize, spinnerColor;
    List<String> sizeOption, colorOption;

    Intent intent;
    int choiceOfSize, choiceOfColor;

    ArrayAdapter<String> dataAdapter;
    SharedPreferences saveSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();

    }

    public void init() {

        intent = getIntent();
        choiceOfSize = intent.getExtras().getInt("size");
        choiceOfColor = intent.getExtras().getInt("color");
        spinnerSize = findViewById(R.id.spinnerSize);
        spinnerColor = findViewById(R.id.spinnerColor);
        createSpinnerOfSize();
        createSpinnerOfColor();

        saveSetting = getSharedPreferences("settings",0);


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
        colorOption = new ArrayList<String>();
        colorOption.add("magenta");
        colorOption.add("green");
        colorOption.add("yellow");
        colorOption.add("black");
        colorOption.add("blue");
        colorOption.add("cyan");
        colorOption.add("red");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colorOption);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerColor.setAdapter(dataAdapter);
        //spinnerColor.setSelection(choiceOfColor-3);

        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = adapterView.getItemAtPosition(i).toString();
                int tempColor = 0;
                SharedPreferences.Editor editor = saveSetting.edit();

                switch (temp) {
                    case "magenta":
                        editor.putInt("color",Color.MAGENTA);
                        break;
                    case "green":
                        editor.putInt("color",Color.GREEN);
                        break;
                    case "yellow":
                        editor.putInt("color",Color.YELLOW);
                        break;
                    case "black":
                        editor.putInt("color",Color.BLACK);
                        break;
                    case "blue":
                        editor.putInt("color",Color.LTGRAY);
                        break;
                    case "cyan":
                        editor.putInt("color",Color.CYAN);
                        break;
                    case "red":
                        editor.putInt("color",Color.RED);
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