package com.example.piacpalota.domain;

public class Buy {
    private String termek;
    private String kategoria;
    private String hely;

    public Buy(String termek, String kategoria, String hely) {
        this.termek = termek;
        this.kategoria = kategoria;
        this.hely = hely;
    }

    public String getTermek() {
        return termek;
    }

    public String getHely() {
        return hely;
    }

    public String getKategoria() {
        return kategoria;
    }
}
