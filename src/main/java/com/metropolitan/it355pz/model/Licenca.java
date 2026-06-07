package com.metropolitan.it355pz.model;

public class Licenca {
    private Long id;
    private String nazivSoftvera;
    private String kljucLicence;
    private String tipLicence;
    private boolean aktivna;

    // Prazan konstruktor
    public Licenca() {
    }

    // Konstruktor sa svim poljima
    public Licenca(Long id, String nazivSoftvera, String kljucLicence, String tipLicence, boolean aktivna) {
        this.id = id;
        this.nazivSoftvera = nazivSoftvera;
        this.kljucLicence = kljucLicence;
        this.tipLicence = tipLicence;
        this.aktivna = aktivna;
    }

    // Getter i Setter metode
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazivSoftvera() {
        return nazivSoftvera;
    }

    public void setNazivSoftvera(String nazivSoftvera) {
        this.nazivSoftvera = nazivSoftvera;
    }

    public String getKljucLicence() {
        return kljucLicence;
    }

    public void setKljucLicence(String kljucLicence) {
        this.kljucLicence = kljucLicence;
    }

    public String getTipLicence() {
        return tipLicence;
    }

    public void setTipLicence(String tipLicence) {
        this.tipLicence = tipLicence;
    }

    public boolean isAktivna() {
        return aktivna;
    }

    public void setAktivna(boolean aktivna) {
        this.aktivna = aktivna;
    }
}
