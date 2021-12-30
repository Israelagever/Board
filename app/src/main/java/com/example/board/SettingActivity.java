package com.example.board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                /*
                switch (temp) {
                    case "magenta":
                        tempColor = Color.MAGENTA;

                    case "green":
                        tempColor =Color.GREEN;

                    case "yellow":
                        tempColor =  Color.YELLOW;

                    case "black":
                        tempColor = Color.BLACK;

                    case "blue":
                        tempColor = Color.BLUE;

                    case "cyan":
                        tempColor = Color.CYAN;

                    case "red":
                        tempColor = Color.RED;
                 }

                 */
                if (temp == "magenta")
                    tempColor = Color.MAGENTA;
                if (temp == "green")
                    tempColor = Color.GREEN;
                if (temp == "yellow")
                    tempColor = Color.YELLOW;
                if (temp == "black")
                    tempColor = Color.BLACK;
                if (temp == "blue")
                    tempColor = Color.BLUE;
                if (temp == "cyan")
                    tempColor = Color.CYAN;
                if (temp == "red")
                    tempColor = Color.RED;


                intent.putExtra("color", tempColor);
                setResult(RESULT_OK, intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String temp = adapterView.getItemAtPosition(i).toString();
        intent.putExtra("size", Integer.parseInt(temp));


        setResult(RESULT_OK, intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}