package com.example.veplan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

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
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class recipeInfoActivity extends AppCompatActivity {

    //Metrische Optionen für die Zutaten
    String[] metrics = {"g", "kg", "ml", "l", "TL", "EL", "Prise"};

    //Adapter für Spinner (Zutaten)
    ArrayAdapter<String> adapter;

    //Portionen Textfeld
    TextView portions;

    //Liste von Zutaten
    Zutat[] ingrList;

    //Variablen für Refresh Methode
    int id;
    int oldFactor;
    int objectCounter = 0;

    EditText t;
    EditText prep_info;
    Drawable editTextEditablePrep;
    Drawable editTextEditableHeader;
    Drawable editTextNotEditable;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_info);



        editTextNotEditable = findViewById(R.id.prep_header).getBackground();

        //Überschrift für Rezeptinfo (Name des Rezeptes)
        t = findViewById(R.id.info_rec_name);
        editTextEditableHeader = t.getBackground();
        t.setBackground(editTextNotEditable);
                //Textfeld für die Zubereitung
        prep_info = (EditText) findViewById(R.id.prep_info);
        editTextEditablePrep = prep_info.getBackground();
        prep_info.setBackground(editTextNotEditable);
        //Textfeld für die Portionen
        portions = (EditText) findViewById(R.id.changable_ingr_amount);
        //Layout mit allen Infos
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_info);

        //Adapter für Spinner initialisieren mit den Metrics
        adapter = new ArrayAdapter<String>(recipeInfoActivity.this,
                R.layout.spinner_list,metrics);

        //Id von angeklicktem Rezept bekommen
        id = getIntent().getExtras().getInt("id");
        //Log.i("testInsideInfoID",""+ id);

        Recipe recipe;

        try {
            //Die Datei wo alles gespeichert wird
            String filepath = getFilesDir().getAbsolutePath() + "/data.json";
            BufferedReader reader = Files.newBufferedReader(Paths.get(filepath));

            //Gson wird initialisiert
            Gson gson = new GsonBuilder().create();
            //JsonArray von der Datei bekommen
            JsonArray allData = gson.fromJson(reader, JsonArray.class);

            //Object innerhalb des Arrays finden, mit der gleichen ID des angeklickten
            JsonObject objectWithId = null;

            for (int i = 0; i < allData.size(); i++) {
                objectWithId = (JsonObject) allData.get(i);
                if (objectWithId.get("id").getAsInt() == id) {
                    objectCounter = i;
                    break;
                }
            }

            recipe = gson.fromJson(objectWithId, Recipe.class);

            //Alter Faktor für wie viele Portionen
            if(!recipe.getPortions().isEmpty()) {
                oldFactor = Integer.parseInt(recipe.getPortions());
                portions.setText(recipe.getPortions());
            }
            //Überschrift mit Rezeptname
            t.setText(recipe.getName());
            //Zubereitung
            prep_info.setText(recipe.getZubereitung());
            //"Zubereitung" und das unterstreichen
            TextView headprep = (TextView) findViewById(R.id.prep_header);
            headprep.setPaintFlags(headprep.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            //Portionen


            //Liste mit den Zutaten
            ingrList = recipe.getZutaten();

            Log.i("Length of Zutaten ARRAY", ""+ ingrList.length);


            //Für jedes Rezept ein Layout inflaten und ausfüllen
            for(int i = 0; i < ingrList.length; i++) {


                Zutat ingr = ingrList[i];
                if(ingr != null) {
                    ConstraintLayout newIngr = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.ingr_list, null);
                    newIngr.setId(id + i);
                    TextView name = (TextView) newIngr.getViewById(R.id.ingr_name);
                    TextView value = (TextView) newIngr.getViewById(R.id.ingr_amount);
                    Spinner metric = (Spinner) newIngr.getViewById(R.id.ingr_metric);
                    metric.setAdapter(adapter);

                    //Richtiges Metric selecten
                    for (int n = 0; n < metrics.length; n++) {

                        if (ingr.getMetric().equals(metrics[n])) {
                            metric.setSelection(n);
                        }

                    }
                    //Ausschalten damit des nicht gewechselt werden kann

                    metric.setEnabled(false);

                    name.setText(ingr.getNameZutat());
                    name.setTextColor(Color.BLACK);
                    name.setEnabled(false);
                    value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4) {
                    }});
                    value.setText(ingr.getMenge());
                    value.setTextColor(Color.BLACK);
                    value.setEnabled(false);


                    l.addView(newIngr);
                }
            }

            ToggleButton editSwitch = (ToggleButton) findViewById(R.id.but_edit_rec);
            editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //Wenn editieren aktiv
                        for(int i = 0; i < ingrList.length; i++) {
                            Zutat ingr = ingrList[i];
                            if(ingr != null) {
                                ConstraintLayout newIngr = (ConstraintLayout) findViewById(id + i);
                                TextView name = (TextView) newIngr.getViewById(R.id.ingr_name);
                                TextView value = (TextView) newIngr.getViewById(R.id.ingr_amount);
                                Spinner metric = (Spinner) newIngr.getViewById(R.id.ingr_metric);

                                //Einschalten weil Editiermodus an

                                metric.setEnabled(true);

                                name.setEnabled(true);
                                value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3) {
                                }});
                                value.setEnabled(true);
                            }
                        }


                        t.setEnabled(true);
                        t.setBackground(editTextEditableHeader);
                        prep_info.setEnabled(true);
                        prep_info.setBackground(editTextEditablePrep);

                        l.findViewById(R.id.lay_portionsize).findViewById(R.id.but_refactor).setEnabled(false);

                    } else {
                        //Wenn editieren nicht aktiv
                        Log.i("Info", "BinichHier");

                        if (!t.getText().toString().equals("") && !prep_info.getText().toString().equals("")) {
                            try {

                                //Path zur Datei
                                String filepath = getFilesDir().getAbsolutePath() + "/data.json";
                                Log.i("FilePath", filepath);
                                BufferedReader reader = Files.newBufferedReader(Paths.get(filepath));


                                //Initialisierung von Gson
                                Gson gson = new GsonBuilder().create();
                                //Array von allen Rezepten
                                JsonArray allData = gson.fromJson(reader, JsonArray.class);

                                Log.i("Wie viele Rezepte", "" + allData.size());
                                allData.remove(objectCounter);


                                //Array von den Zutaten eines jeweiligen Rezepts
                                Zutat[] ingredients = new Zutat[ingrList.length];

                                //Name aus Namenfeld
                                recipe.setName(t.getText().toString());
                                recipe.setZubereitung(prep_info.getText().toString());
                                //Hier noch für wie viele Portionen
                                recipe.setPortions(portions.getText().toString());


                                //Zutaten hinzufügen !! Hier noch typ angeben vom Spinner
                                //Anders als zuvor mit Constrainlayout irgendwie machen
                                for (int i = 0; i < ingrList.length; i++) {

                                    Zutat temp = ingrList[i];
                                    if(temp != null) {
                                        ConstraintLayout ingrLayout = (ConstraintLayout) findViewById(id + i);
                                        Spinner mySpinner = (Spinner) ingrLayout.getViewById(R.id.ingr_metric);
                                        TextView ingrAmount = (TextView) ingrLayout.getViewById(R.id.ingr_amount);
                                        TextView ingrName = (TextView) ingrLayout.getViewById(R.id.ingr_name);
                                        String ingrMetric = mySpinner.getSelectedItem().toString();

                                        boolean amountEmpty = (ingrAmount.getText().toString().equals(""));
                                        boolean nameEmpty = (ingrName.getText().toString().equals(""));
                                        boolean portionEmpty = (portions.getText().toString().equals(""));

                                        Log.i("Empty test", "amount" + amountEmpty + " name" + nameEmpty);

                                        //Beide Felder sind beschrieben & portion is angegeben -> dann wird es in json getan
                                        if (!amountEmpty && !nameEmpty && !portionEmpty) {
                                            temp.setNameZutat(ingrName.getText().toString());
                                            temp.setMenge(ingrAmount.getText().toString());
                                            temp.setMetric(ingrMetric);
                                            ingredients[i] = temp;
                                        }
                                    }

                                }
                                recipe.setZutaten(ingredients);
                                String json = gson.toJson(recipe);
                                //Bleibt so
                                JsonObject recipeObject = gson.fromJson(json, JsonObject.class);
                                allData.add(recipeObject);

                                if (!(prep_info.getText().toString().equals("")) && !(t.getText().toString().equals(""))) {
                                    FileWriter file = new FileWriter(filepath);
                                    file.write(gson.toJson(allData));
                                    file.flush();
                                    file.close();
                                    //finish(); Ich weiß noch nicht ob ich das so will
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        for(int i = 0; i < ingrList.length; i++) {


                            ConstraintLayout newIngr = (ConstraintLayout) findViewById(id + i);
                            if(newIngr != null) {
                                TextView name = (TextView) newIngr.getViewById(R.id.ingr_name);
                                TextView value = (TextView) newIngr.getViewById(R.id.ingr_amount);
                                Spinner metric = (Spinner) newIngr.getViewById(R.id.ingr_metric);

                                //Ausschalten weil Editiermodus an

                                metric.setEnabled(false);

                                name.setEnabled(false);
                                value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4) {
                                }});
                                value.setEnabled(false);
                            }
                        }

                        t.setEnabled(false);
                        t.setBackground(editTextNotEditable);
                        prep_info.setEnabled(false);
                        prep_info.setBackground(editTextNotEditable);

                        l.findViewById(R.id.lay_portionsize).findViewById(R.id.but_refactor).setEnabled(true);
                    }
                }
            });

            //Falls keine Zutaten vorhanden, Portionierung löschen
            if(ingrList.length == 0) {

                l.removeView(l.findViewById(R.id.lay_portionsize));

            }

        } catch(Exception e) {

        }
    }

    public void refreshPortions(View view) {

        int newFactor = Integer.parseInt(portions.getText().toString());
        if(newFactor == 0) {
            portions.setText(String.valueOf(oldFactor));
            return;
        }
        Log.i("factor of portions", "" + Integer.parseInt(portions.getText().toString()));


            for (int i = 0; i < ingrList.length; i++) {



                ConstraintLayout ingrComplex = (ConstraintLayout) findViewById(id + i);
                TextView value = (TextView) ingrComplex.getViewById(R.id.ingr_amount);
                int valueInt = Integer.parseInt(value.getText().toString());
                int newValue = (valueInt * newFactor) / oldFactor;

                value.setText(String.valueOf(newValue));

            }

        oldFactor = newFactor;

    }



}

