package com.example.veplan;

import androidx.appcompat.app.AppCompatActivity;


import org.json.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Json File erstellen
        createData();

    }

    public void createData() {

        try {
            //Path zum internen Speicher mit Dateinamen
            String pathCalender = getFilesDir().getAbsolutePath() + "/calenderdata.json";
            String path = getFilesDir().getAbsolutePath() + "/data.json";
            //Initialisierung Gson
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            //JsonArray (Wo alle Rezepte im endeffekt rein kommen)
            JsonArray allData = new JsonArray();
            JsonArray allDays = new JsonArray();

            //Wenn File schon existiert dann mach gar nichts existiert ja schon
            if(new File(path).exists() && new File(pathCalender).exists()) {
                Log.i("INFO", "Files existiert schon");
            //Wenn noch nicht existiert erstell die Datei und schreib das leere
            //Array rein
            } else {
                FileWriter file = new FileWriter(path);
                file.write(gson.toJson(allData));
                file.flush();
                file.close();

                FileWriter fileCalender = new FileWriter(pathCalender);
                fileCalender.write(gson.toJson(allDays));
                fileCalender.flush();
                fileCalender.close();
                Log.i("INFO",gson.toJson(allData));
            }

        } catch(Exception e) {

        }


    }

    //Onclick von den 3 Buttons
    public void onClick(View view) {
        //Button mit Kalender
        if(view.equals(findViewById(R.id.but_cal))) {
            Intent calenderActivity = new Intent(this, com.example.veplan.calenderActivity.class);
            startActivity(calenderActivity);
        //Button mit Rezepten
        } else if(view.equals(findViewById(R.id.but_rec))) {
            Intent recipesActivity = new Intent(this, com.example.veplan.recipesActivity.class);
            startActivity(recipesActivity);
        //Button mit Food (Noch keine Funktion)
        } else if(view.equals(findViewById(R.id.but_food))) {
            Intent foodActivity = new Intent(this, com.example.veplan.foodActivity.class);
            startActivity(foodActivity);
        }
    }


}