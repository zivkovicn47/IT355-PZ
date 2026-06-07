package com.metropolitan.it355pz.model;

public class Projekat {
    private Long id;
    private String naziv;
    private String klijent;
    private String status; 

    public Projekat() {
    }

    public Projekat(Long id, String naziv, String klijent, String status) {
        this.id = id;
        this.naziv = naziv;
        this.klijent = klijent;
        this.status = status;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getKlijent() {
        return klijent;
    }

    public void setKlijent(String klijent) {
        this.klijent = klijent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
