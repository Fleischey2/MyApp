package com.example.veplan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;

public class calenderActivity extends AppCompatActivity {

    private ArrayList<Button> monat = new ArrayList<>();
    private String[] months = {"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August"
    , "September", "Oktober", "November", "Dezember"};
    int changeMonthIndex;
    int changeYearIndex;

    //Heutiger Tag und Jahr mit Monat
    private MonthDay m;
    private YearMonth ym;
    TextView monthName;
    TextView yearName;
    LinearLayout weeks;
    HorizontalScrollView weeksScrollView;

    //Schaltjahr irgendwann mal
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender);
        ym = YearMonth.now();
        //Alle wichtigen Felder getten
        monthName = findViewById(R.id.name_month);
        yearName = findViewById(R.id.name_year);
        weeks = (LinearLayout) findViewById(R.id.weeksOfMonth);
        weeksScrollView = (HorizontalScrollView) findViewById(R.id.parent_width_scroll);

        changeMonthIndex = ym.getMonthValue() - 1;
        monthName.setText(months[ym.getMonthValue() - 1]);

        changeYearIndex = ym.getYear();
        yearName.setText("" + changeYearIndex);

        addWeek();

    }

    /**
     * Pfeil nach rechts und Pfeil nach links bei der Monatauswahl
     * @param view Button leftmonth oder rightmonth
     */
    public void changeMonth(View view) {
        weeksScrollView.smoothScrollTo(0,0);
        removeWeeks();
        if(view == findViewById(R.id.right_month)) {


            if((changeMonthIndex + 1) == 12) {

                changeMonthIndex = 0;
                changeYearIndex += 1;
                yearName.setText("" + changeYearIndex);

            } else {

                changeMonthIndex += 1;

            }
            monthName.setText(months[changeMonthIndex]);

        } else if(view == findViewById(R.id.left_month)) {


            if((changeMonthIndex - 1) < 0) {
                changeMonthIndex = 11;
                changeYearIndex -= 1;
                yearName.setText("" + changeYearIndex);
            } else {
                changeMonthIndex -= 1;
            }
            monthName.setText(months[changeMonthIndex]);

        }
        addWeek();


    }

    public void addWeek() {
        LocalDate nowDate = LocalDate.now();
        int weekOfYear = nowDate.get(WeekFields.of(new Locale("en")).weekOfYear());
        Log.i("Week of year","" + weekOfYear);

        int amountWeeks = 0;

        LocalDate dateSelected = LocalDate.of(changeYearIndex, changeMonthIndex + 1, 1);

        int amountDays = dateSelected.getMonth().length(dateSelected.isLeapYear());
        int amountDaysCounter = amountDays;
        Log.i("Debug Tagescounter", amountDays + "");

        //Log.i("Debug","" + dateSelected.getDayOfWeek().getValue());
        boolean longMonth = dateSelected.getMonth().length(dateSelected.isLeapYear()) == 31 && dateSelected.getDayOfWeek().getValue() >= 6;
        boolean middlelongMonth = dateSelected.getMonth().length(dateSelected.isLeapYear()) == 30 && dateSelected.getDayOfWeek().getValue() == 7;
        boolean februaryShort = dateSelected.getMonth().length(dateSelected.isLeapYear()) == 28 && dateSelected.getDayOfWeek().getValue() == 1;

        if(longMonth || middlelongMonth) {
            amountWeeks = 6;
        } else if(februaryShort) {
            amountWeeks = 4;
        } else {
            amountWeeks = 5;
        }

        for(int i = 0; i < amountWeeks; i++) {
            ConstraintLayout week = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.calender_week, null);
            week.setId(ym.getMonthValue() + weekOfYear + i);


            Button[] weekDays = {(Button) week.findViewById(R.id.but_mon), (Button) week.findViewById(R.id.but_tue),
                    (Button) week.findViewById(R.id.but_wedn), (Button) week.findViewById(R.id.but_thurs),
                    (Button) week.findViewById(R.id.but_fri), (Button) week.findViewById(R.id.but_satu),
                    (Button) week.findViewById(R.id.but_sund)};



            if(i == 0) {

                int daysOfLastMonth = 0;
                if((changeMonthIndex - 1) < 0) {
                    daysOfLastMonth = 31;
                } else {
                    LocalDate lastMonthDate = LocalDate.of(changeYearIndex, changeMonthIndex, 1);
                    daysOfLastMonth = lastMonthDate.getMonth().length(lastMonthDate.isLeapYear());

                }

                int firstDayOfMonth = dateSelected.getDayOfWeek().getValue();
                Log.i("Debug first day: ", "" + firstDayOfMonth);
                Log.i("Debug daysOfLastMonth", "" + daysOfLastMonth);



                for(int last = firstDayOfMonth - 1; last >= 1; last--) {
                    weekDays[last - 1].setText("" + daysOfLastMonth--);
                    weekDays[last - 1].setClickable(false);
                    weekDays[last - 1].setBackgroundColor(Color.argb(50, 110, 110, 110));
                }
                for(int first = firstDayOfMonth - 1; first < 7; first++) {
                    weekDays[first].setText("" + (amountDays - --amountDaysCounter));
                    weekDays[first].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dayInfo(view);
                        }
                    });
                }

            } else if(i == amountWeeks - 1) {
                LocalDate thisMonthLastDay = LocalDate.of(changeYearIndex, changeMonthIndex + 1, amountDays);
                int lastDayOfMonth = thisMonthLastDay.getDayOfWeek().getValue();
                Log.i("Debug lastDayOfMonth", "" + lastDayOfMonth);

                for(int first = 0; first < lastDayOfMonth; first++) {
                    weekDays[first].setText("" + (amountDays - --amountDaysCounter));
                    weekDays[first].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dayInfo(view);
                        }
                    });
                }
                int counter = 1;

                for(int last = lastDayOfMonth; last < 7; last++) {
                    Log.i("Debug inside lastweek", "" + last);
                    weekDays[last].setText("" + counter++);
                    weekDays[last].setClickable(false);
                    weekDays[last].setBackgroundColor(Color.argb(50, 110, 110, 110));
                }

            } else {
                for(int c = 0; c < 7; c++) {
                    weekDays[c].setText("" + (amountDays - --amountDaysCounter));
                    weekDays[c].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dayInfo(view);
                        }
                    });
                }
            }

            //weekDays[0].setText(31 + "");

            weeks.addView(week);

        }

    }

    public void removeWeeks() {

        LocalDate nowDate = LocalDate.now();
        int weekOfYear = nowDate.get(WeekFields.of(new Locale("en")).weekOfYear());
        Log.i("Week of year","" + weekOfYear);

        int amountWeeks = 0;

        LocalDate dateSelected = LocalDate.of(changeYearIndex, changeMonthIndex + 1, 1);
        //Log.i("Debug","" + dateSelected.getDayOfWeek().getValue());
        boolean longMonth = dateSelected.getMonth().length(dateSelected.isLeapYear()) == 31 && dateSelected.getDayOfWeek().getValue() >= 6;
        boolean middlelongMonth = dateSelected.getMonth().length(dateSelected.isLeapYear()) == 30 && dateSelected.getDayOfWeek().getValue() == 7;
        boolean februaryShort = dateSelected.getMonth().length(dateSelected.isLeapYear()) == 28 && dateSelected.getDayOfWeek().getValue() == 1;

        if(longMonth || middlelongMonth) {
            amountWeeks = 6;
        } else if(februaryShort) {
            amountWeeks = 4;
        } else {
            amountWeeks = 5;
        }

        for(int i = 0; i < amountWeeks; i++) {

            ConstraintLayout week = findViewById(ym.getMonthValue() + weekOfYear + i);
            weeks.removeView(week);

        }

    }

    public void dayInfo(View view) {
        Log.i("MonthIndex", "" + changeMonthIndex);
        LocalDate date = LocalDate.of(changeYearIndex, changeMonthIndex + 1, Integer.parseInt(((Button) view).getText().toString()));
        Intent dayInfoActivity = new Intent(this, com.example.veplan.dayActivity.class);
        dayInfoActivity.putExtra("date", date);
        startActivity(dayInfoActivity);

    }

}
