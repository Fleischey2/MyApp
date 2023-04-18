package com.example.veplan;

import com.google.gson.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.util.JsonReader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.veplan.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class recipesActivity extends AppCompatActivity {

    static int recipes = 0;
    LinearLayout l;
    boolean deleteMode;
    boolean dayAuswahl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes);
        if(getIntent().hasExtra("day")) {
            dayAuswahl = true;
        }
        ImageView trashcan = (ImageView) findViewById(R.id.im_trash);
        ToggleButton trashOffOn = (ToggleButton) findViewById(R.id.but_del_rec);
        Button addRecButton = (Button) findViewById(R.id.but_add_rec);

        if(dayAuswahl) {
            trashcan.setVisibility(View.INVISIBLE);
            trashOffOn.setVisibility(View.INVISIBLE);
            addRecButton.setVisibility(View.INVISIBLE);
        }

        recipesActivity temp = this;

        deleteMode = false;

        //Ding wo alle Rezepte rein kommen
        l = (LinearLayout) findViewById(R.id.rec_list);

        try {
            //Path zur Datei
            String filepath = getFilesDir().getAbsolutePath() + "/data.json";
            Log.i("filePath", filepath);
            BufferedReader reader = Files.newBufferedReader(Paths.get(filepath));


            //Initialisierung von Gson
            Gson gson = new GsonBuilder().create();
            //Array von allen Rezepten
            JsonArray allData = gson.fromJson(reader, JsonArray.class);
            recipes = allData.size();

            //Alle rezepte durchgehen und in das LinearLayout l machen (nur Name)
            for(int i = 0; i < allData.size(); i++) {

                JsonObject innerObject = (JsonObject) allData.get(i);
                Recipe recipe = gson.fromJson(innerObject, Recipe.class);
                Button b = new Button(this);
                b.setTextColor(Color.BLACK);
                Drawable buttonDrawable = b.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                //the color is a direct color int and not a color resource
                DrawableCompat.setTint(buttonDrawable, Color.WHITE);
                //b.setBackground(buttonDrawable);
                //b.setText(innerObject.get("name").getAsString());
                b.setText(recipe.getName());
                //b.setId(innerObject.get("id").getAsInt());
                b.setId(recipe.getId());
                Log.i("asInt","" + innerObject.get("id").getAsInt());
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(dayAuswahl == false) {
                            if (deleteMode) {
                                l.removeView(b);
                                int viewCounter = 0;
                                for (int i = 0; i < allData.size(); i++) {
                                    if (((JsonObject) allData.get(i)).get("id").getAsInt() == b.getId()) {
                                        allData.remove(i);
                                    }
                                }


                                try {
                                    FileWriter file = new FileWriter(filepath);
                                    file.write(gson.toJson(allData));
                                    file.flush();
                                    file.close();
                                } catch (Exception e) {

                                }

                            } else {
                                Intent intent = new Intent(temp, com.example.veplan.recipeInfoActivity.class);
                                intent.putExtra("id", b.getId());
                                startActivity(intent);

                                Log.i("testInsideOnclickName", b.getText().toString());
                                Log.i("testInsideOnclickID", "" + b.getId());
                            }
                        } else {

                            Intent returnIntent = getIntent();
                            returnIntent.putExtra("RecipeId", b.getId());
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();

                        }
                    }
                });

                l.addView(b);
                Log.i("testTheJsonObject", innerObject.toString());

                ToggleButton killSwitch = (ToggleButton) findViewById(R.id.but_del_rec);
                killSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            deleteMode = true;
                        } else {
                            deleteMode = false;
                        }
                    }
                });


            }

        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    //Öffne anderes Menü
    public void addBut(View view) {

        Intent addRecActivity = new Intent(this, com.example.veplan.addRecActivity.class);
        startActivity(addRecActivity);

    }

    //Updatet wenn man von ner anderen Activity zurückkommt
    @Override
    public void onRestart() {
        super.onRestart();

        Intent intent = this.getIntent();
        finish();
        startActivity(intent);
    }
}
