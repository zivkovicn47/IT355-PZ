package com.metropolitan.it355pz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Zadatak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String opis;
    private Long projekatId;
    private Long inzenjerId;
    private boolean zavrsen;

    // Prazan konstruktor
    public Zadatak() {
    }

    // Konstruktor sa svim poljima
    public Zadatak(Long id, String opis, Long projekatId, Long inzenjerId, boolean zavrsen) {
        this.id = id;
        this.opis = opis;
        this.projekatId = projekatId;
        this.inzenjerId = inzenjerId;
        this.zavrsen = zavrsen;
    }

    // Getter i Setter metode
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Long getProjekatId() {
        return projekatId;
    }

    public void setProjekatId(Long projekatId) {
        this.projekatId = projekatId;
    }

    public Long getInzenjerId() {
        return inzenjerId;
    }

    public void setInzenjerId(Long inzenjerId) {
        this.inzenjerId = inzenjerId;
    }

    public boolean isZavrsen() {
        return zavrsen;
    }

    public void setZavrsen(boolean zavrsen) {
        this.zavrsen = zavrsen;
    }
}
