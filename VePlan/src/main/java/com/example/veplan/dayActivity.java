package com.example.veplan;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class dayActivity extends AppCompatActivity {

    int idOfSelectedRecipe = 0;
    JsonArray allData;
    JsonArray allDays;
    Gson gson;
    Button butRecipe;
    Day dayInfo;
    String filepath;
    String calenderFilePath;
    TextView dateText;
    TextView dayNameText;
    DateTimeFormatter myFormat;
    Datum date;
    String[] daysName = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};

    private ActivityResultLauncher<Intent> getRecipeOrShoppinglist =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == Activity.RESULT_OK) {

                    idOfSelectedRecipe = (int) result.getData().getExtras().get("RecipeId");
                    //Log.i("Logging", "IdVomRezept = " + idOfSelectedRecipe);
                    for(int i = 0; i < allData.size(); i++) {

                        JsonObject innerObject = (JsonObject) allData.get(i);
                        Recipe recipe = gson.fromJson(innerObject, Recipe.class);
                        if(recipe.getId() == idOfSelectedRecipe) {

                            try {
                                butRecipe.setText(recipe.getName());
                                dayInfo = new Day(date, recipe);
                                Log.i("TestDate", gson.toJson(dayInfo));
                                allDays.add(gson.fromJson(gson.toJson(dayInfo), JsonObject.class));

                                FileWriter file = new FileWriter(calenderFilePath);
                                file.write(gson.toJson(allDays));
                                file.flush();
                                file.close();
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_day);


        dayNameText = (TextView) findViewById(R.id.day_name);
        dateText = (TextView) findViewById(R.id.day_date);
        LocalDate dateTemp = ((LocalDate) getIntent().getExtras().get("date"));
        date = new Datum("" + dateTemp.getDayOfMonth(), "" + dateTemp.getMonthValue(), "" + dateTemp.getYear());
        dayNameText.setText(daysName[dateTemp.getDayOfWeek().getValue() - 1]);
        dateText.setText(date.toString());


        try {
            //Path zur Datei
            filepath = getFilesDir().getAbsolutePath() + "/data.json";
            calenderFilePath = getFilesDir().getAbsolutePath() + "/calenderdata.json";
            BufferedReader reader = Files.newBufferedReader(Paths.get(filepath));
            BufferedReader readerCalender = Files.newBufferedReader(Paths.get(calenderFilePath));


            //Initialisierung von Gson
            gson = new GsonBuilder().create();

            //Array von allen Rezepten
            allData = gson.fromJson(reader, JsonArray.class);
            allDays = gson.fromJson(readerCalender, JsonArray.class);
            Log.i("wie Viele Tage gespeichert", "" + allDays.size());

            butRecipe = (Button) findViewById(R.id.but_addRecipe);

            Log.i("Bin ich Hier?",""+ allDays.size());
            for(int i = 0; i < allDays.size(); i++) {
                Log.i("Bin ich Hier drin?", "Ja in der For Schleife");
                JsonObject object = (JsonObject) allDays.get(i);
                Log.i("Test", object.toString());
                Day searchedDay = gson.fromJson(object, Day.class);
                LocalDate searchedDate = LocalDate.of(Integer.parseInt(searchedDay.getDate().getJahr()),
                        Integer.parseInt(searchedDay.getDate().getMonat()),
                        Integer.parseInt(searchedDay.getDate().getTag()));

                //Log.i("searchedDay", searchedDate.getDayOfYear() + "");
                Log.i("currentDay", dateTemp.getDayOfYear() + "");
                if(searchedDate.getDayOfYear() == dateTemp.getDayOfYear()) {
                    butRecipe.setText(searchedDay.getRecipe().getName());
                }

            }




        } catch(Exception e) {
            Log.i("Was passiert hier", e.toString());
        }


    }


    public void addRecipe(View view) {

        if(butRecipe.getText().toString().equals("Rezept Hinzufügen")) {
            Intent recipeListActivity = new Intent(this, com.example.veplan.recipesActivity.class);
            recipeListActivity.putExtra("day", true);
            getRecipeOrShoppinglist.launch(recipeListActivity);
        } else {
            Intent intent = new Intent(this, com.example.veplan.recipeInfoActivity.class);
            intent.putExtra("id", idOfSelectedRecipe);
            startActivity(intent);
        }

    }


}
