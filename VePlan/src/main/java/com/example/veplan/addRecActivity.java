package com.example.veplan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class addRecActivity extends AppCompatActivity {
    LinearLayout l;
    int buttonPressed = 1;  //Wie viele Zutaten rein kommen
    String[] metrics = {"g", "kg", "ml", "l", "TL", "EL", "Prise"};
    ArrayAdapter<String> adapter;
    Spinner mySpinner;
    TextView newestAmount;
    TextView newestName;

    //Name und Beschreibung von neuem Rezept
    TextView nameOfRec;
    TextView howTo;
    TextView portions;

    ConstraintLayout newestIngr;
    ConstraintLayout addDelButtons;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rec);

        l = (LinearLayout) findViewById(R.id.rec_menu);

        mySpinner = (Spinner) findViewById(R.id.ingr_metric);
        adapter = new ArrayAdapter<String>(addRecActivity.this,
                R.layout.spinner_list,metrics);
        mySpinner.setAdapter(adapter);
        mySpinner.setSelection(0);

        nameOfRec = findViewById(R.id.rec_name);
        howTo = findViewById(R.id.rec_prep);
        portions = findViewById(R.id.ingr_for_portion);

        newestIngr = (ConstraintLayout) findViewById(R.id.first_rec);
        newestIngr.setId(31200);
        newestAmount = (TextView) newestIngr.getViewById(R.id.ingr_amount);
        newestName = (TextView) newestIngr.getViewById(R.id.ingr_name);

        addDelButtons = (ConstraintLayout) findViewById(R.id.but_adddel);


    }


    //Zutaten hinzufügen
    public void addIngr(View view) {





        boolean portionEmpty = (portions.getText().toString().equals(""));
        boolean amountEmpty = (newestAmount.getText().toString().equals(""));
        boolean nameEmpty = (newestName.getText().toString().equals(""));


        if(!amountEmpty && !nameEmpty && !portionEmpty) {
            //Der Container mit Name, Zutaten und Beschreibung


            ConstraintLayout newIngr = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.ingr_list, null);
            newIngr.setId(31200 + buttonPressed++);


            Log.i("neues Layout", "" + newIngr.getId());

            Spinner newSpinner = (Spinner) newIngr.getViewById(R.id.ingr_metric);
            newSpinner.setAdapter(adapter);
            newSpinner.setSelection(0);

            l.removeView(addDelButtons);

            newestIngr = newIngr;
            l.addView(newIngr);
            newestAmount.setEnabled(false);
            newestName.setEnabled(false);
            newSpinner.setEnabled(false);
            l.addView(addDelButtons);

        } else {
            if(amountEmpty) {
                newestAmount.setHint("*");
                newestAmount.setHintTextColor(getColor(R.color.warningred));
            }
            if(nameEmpty) {
                newestName.setHint("Zutat-Name*");
                newestName.setHintTextColor(getColor(R.color.warningred));
            }
            if(portionEmpty) {
                portions.setHint("*");
                portions.setHintTextColor(getColor(R.color.warningred));
            }

        }
        newestAmount = (TextView) newestIngr.getViewById(R.id.ingr_amount);
        Log.i("Amount test", newestAmount.getText().toString());
        newestName = (TextView) newestIngr.getViewById(R.id.ingr_name);

    }

    public void delIngr(View view) {

        if(buttonPressed > 1) {

            Button addButton = (Button) findViewById(R.id.add_button);
            l.removeView(newestIngr);
            newestIngr = (ConstraintLayout) findViewById(31199 + --buttonPressed);

            newestAmount = (TextView) newestIngr.getViewById(R.id.ingr_amount);
            newestAmount.setEnabled(true);
            newestName = (TextView) newestIngr.getViewById(R.id.ingr_name);
            newestName.setEnabled(true);



        }

    }

    //Abschließen und in die Datei schreiben
    @SuppressLint("ResourceType")
    public void submit(View view) {

        if (!nameOfRec.getText().toString().equals("") && !howTo.getText().toString().equals("")) {
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



                Zutat[] zutaten = new Zutat[buttonPressed];

                for (int i = 0; i < buttonPressed; i++) {

                    ConstraintLayout ingrLayout = (ConstraintLayout) findViewById(31200 + i);


                    //JsonObject temp = new JsonObject();

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

                        zutaten[i] = new Zutat(ingrAmount.getText().toString(), ingrName.getText().toString(), ingrMetric);
//                        temp.addProperty("zutatname", ingrName.getText().toString());
//                        temp.addProperty("value", ingrAmount.getText().toString());
//                        temp.addProperty("metric", ingrMetric);
//                        ingredients.add(temp);
                    }

                }
                //recipe.add("Zutaten", ingredients);
                String recipe = gson.toJson(new Recipe(nameOfRec.getText().toString(),"153" + recipesActivity.recipes ,portions.getText().toString() ,howTo.getText().toString(), zutaten));
                //Das Rezept zu der Rezeptliste hinzufügen
                //Bleibt so
                JsonObject recipeObject = gson.fromJson(recipe, JsonObject.class);
                Log.i("Logging", "" + recipeObject.get("id").toString());
                allData.add(recipeObject);
                Log.i("Logging", "" + allData.size());

                boolean zutatenClean = true;
                for(Zutat e : zutaten) {
                    if(e == null) {
                        zutatenClean = false;
                    }
                }

                if (!(howTo.getText().toString().equals("")) && !(nameOfRec.getText().toString().equals("")) && zutatenClean) {
                    FileWriter file = new FileWriter(filepath);
                    file.write(gson.toJson(allData));
                    file.flush();
                    file.close();
                    finish();
                } else {
                    newestName.setHint("!*!");
                    newestName.setHintTextColor(getColor(R.color.warningred));

                    newestAmount.setHint("!*!");
                    newestAmount.setHintTextColor(getColor(R.color.warningred));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            howTo.setHint("!*!");
            howTo.setHintTextColor(getColor(R.color.warningred));

            nameOfRec.setHint("Name des Rezeptes*");
            nameOfRec.setHintTextColor(getColor(R.color.warningred));

        }
    }



}


//EXTRA METHODE RETURNT BOOLEAN WENN NEUSTE ZUTAT NICHT VOLLSTÄNDIG IST
/**
 * public boolean name() {
 *     Textviews global machen
 *     constrainlayout neu mit buttonpressed is ja global
 *     dann die booleans mit .equals("");
 *     dann return first boolean & second boolean dann dass ! dann methodenname iwas mit vollständig
 *
 * }
 *
 * in if von addIngr dann einfach die Methode aufrufen wenn true dann neuen buton wenn nicht dann
 * das mit den Warnungen machen
 *
 * in submit so umstrukturieren, dass wenn das true liefert und portions != "" ist,
 * dann alles normal ausführen, wenn nicht dann nichts in den containern speichern und
 * überall wo nichts drin steht muss man ausfüllen
 *
 * man kann ja nur eine neue zutat machen wenn die letzte vollständig ist, dh man muss nur die
 * neuste anschauen und dafür ist ja die neue methode, HIER DRIN WICHTIG WENN BEI NEUER ZUTAT
 * NUR 1 AUSGEFÜLLT DANN IST FALSCH DANN DAS ANDERE WARNUNG, WENN KEINS DANN IST OKAY UND DAS LEERE
 * WIRD IGNORIERT
 * DAMIT MUSS MAN KEINEN LÖSCH BUTTON HABEN LOL
 * Cool danke ez <3 Ich geh weg tschüss glaub ich
 *
 */