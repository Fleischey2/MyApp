package com.example.veplan;

public class Zutat {

    String menge;
    String nameZutat;
    String metric;

    public Zutat(String menge, String nameZutat, String metric) {
        this.menge = menge;
        this.nameZutat = nameZutat;
        this.metric = metric;
    }

    public String getMenge() {
        return menge;
    }
    public String getNameZutat() {
        return nameZutat;
    }
    public String getMetric() {
        return metric;
    }

    public void setMenge(String menge) {
        this.menge = menge;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }
    public void setNameZutat(String nameZutat) {
        this.nameZutat = nameZutat;
    }


}
