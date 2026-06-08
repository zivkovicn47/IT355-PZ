package com.metropolitan.it355pz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Inzenjer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ime;
    private String prezime;
    private String email;
    private String uloga; // npr. "PLC Programer"

    // Prazan konstruktor
    public Inzenjer() {
    }

    // Konstruktor sa svim poljima
    public Inzenjer(Long id, String ime, String prezime, String email, String uloga) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.uloga = uloga;
    }

    // Getter i Setter metode
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }
}
