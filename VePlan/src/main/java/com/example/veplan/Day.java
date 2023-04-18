package com.example.veplan;

import java.time.LocalDate;

public class Day {

    Datum date = null;
    Recipe recipe = null;
    Einkaufsliste einkaufsliste = null;

    public Day(Datum date) {
        this.date = date;
    }

    public Day(Datum date, Recipe recipe) {
        this.date = date;
        this.recipe = recipe;
    }

    public Day(Datum date, Einkaufsliste einkaufsliste) {
        this.date = date;
        this.einkaufsliste = einkaufsliste;
    }

    public Day(Datum date, Recipe recipe, Einkaufsliste einkaufsliste) {
        this.date = date;
        this.einkaufsliste = einkaufsliste;
        this.recipe = recipe;
    }

    public Datum getDate() {
        return date;
    }

    public Einkaufsliste getEinkaufsliste() {
        return einkaufsliste;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
