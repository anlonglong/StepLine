package com.example.jacky.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        StepLine stepLine = (StepLine) findViewById(R.id.step_line);
        stepLine.setOnClickListener((v) -> {
            stepLine.setCenterLineColor(Color.RED);
        });
    }
}
