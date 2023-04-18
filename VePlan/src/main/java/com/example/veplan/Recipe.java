package com.example.veplan;

import java.util.List;

public class Recipe {

    String name;
    String id;
    String zubereitung;
    String portions;
    Zutat[] zutaten;

    public Recipe(String name, String id, String portions,String zubereitung, Zutat[] zutaten) {
        this.name = name;
        this.id = id;
        this.portions = portions;
        this.zubereitung = zubereitung;
        this.zutaten = zutaten;
    }

    public int getId() {
        return Integer.parseInt(id);
    }

    public String getName() {
        return name;
    }

    public String getPortions() {
        return portions;
    }

    public String getZubereitung() {
        return zubereitung;
    }

    public Zutat[] getZutaten() {
        return zutaten;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPortions(String portions) {
        this.portions = portions;
    }

    public void setZubereitung(String zubereitung) {
        this.zubereitung = zubereitung;
    }

    public void setZutaten(Zutat[] zutaten) {
        this.zutaten = zutaten;
    }
}
