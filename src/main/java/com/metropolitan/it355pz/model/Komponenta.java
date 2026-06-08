package com.metropolitan.it355pz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Komponenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String naziv;
    private String serijskiBroj;
    private String proizvodjac;
    private String status; // npr. "Na stanju", "Ugrađeno"

    // Prazan konstruktor
    public Komponenta() {
    }

    // Konstruktor sa svim poljima
    public Komponenta(Long id, String naziv, String serijskiBroj, String proizvodjac, String status) {
        this.id = id;
        this.naziv = naziv;
        this.serijskiBroj = serijskiBroj;
        this.proizvodjac = proizvodjac;
        this.status = status;
    }

    // Getter i Setter metode
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

    public String getSerijskiBroj() {
        return serijskiBroj;
    }

    public void setSerijskiBroj(String serijskiBroj) {
        this.serijskiBroj = serijskiBroj;
    }

    public String getProizvodjac() {
        return proizvodjac;
    }

    public void setProizvodjac(String proizvodjac) {
        this.proizvodjac = proizvodjac;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
