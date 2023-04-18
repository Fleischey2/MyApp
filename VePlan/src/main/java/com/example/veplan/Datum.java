package com.example.veplan;

public class Datum {

    private String tag;
    private String monat;
    private String jahr;

    public Datum(String tag, String monat, String jahr) {
        this.tag = tag;
        this.monat = monat;
        this.jahr = jahr;
    }

    public String toString() {
        return tag + "." + monat + "." + jahr;
    }

    public String getJahr() {
        return jahr;
    }

    public String getMonat() {
        return monat;
    }

    public String getTag() {
        return tag;
    }
}
