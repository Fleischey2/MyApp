package com.example.veplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class foodActivity extends AppCompatActivity {

int width;
int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food);
        HorizontalScrollView widthValue = (HorizontalScrollView) findViewById(R.id.parent_width_scroll);
        width = widthValue.getWidth();
        height = widthValue.getHeight();



        LinearLayout l = (LinearLayout) findViewById(R.id.weeksOfMonth);
        for(int i = 0; i < 2; i++) {

            ConstraintLayout week = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.calender_week, null);
            week.setId(33100 + i);
            l.addView(week);


        }




    }




}
