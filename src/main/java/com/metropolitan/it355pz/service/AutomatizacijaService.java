package com.metropolitan.it355pz.service;

import com.metropolitan.it355pz.model.*;
import com.metropolitan.it355pz.repository.InMemoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutomatizacijaService {

    private final InMemoryRepository repository;

    public AutomatizacijaService(InMemoryRepository repository) {
        this.repository = repository;
    }

    // --- Projekat CRUD ---
    public List<Projekat> getSveProjekte() {
        return repository.getProjekti();
    }

    public Projekat getProjekatById(Long id) {
        return repository.getProjekatById(id);
    }

    public void sacuvajProjekat(Projekat p) {
        if (p.getId() == null) {
            repository.dodajProjekat(p);
        } else {
            repository.azurirajProjekat(p);
        }
    }

    public void obrisiProjekat(Long id) {
        repository.obrisiProjekat(id);
    }

    // --- Komponenta CRUD ---
    public List<Komponenta> getSveKomponente() {
        return repository.getKomponente();
    }

    public Komponenta getKomponentaById(Long id) {
        return repository.getKomponentaById(id);
    }

    public void sacuvajKomponentu(Komponenta k) {
        if (k.getId() == null) {
            repository.dodajKomponentu(k);
        } else {
            repository.azurirajKomponentu(k);
        }
    }

    public void obrisiKomponentu(Long id) {
        repository.obrisiKomponentu(id);
    }

    // --- Inzenjer CRUD ---
    public List<Inzenjer> getSveInzenjere() {
        return repository.getInzenjeri();
    }

    public Inzenjer getInzenjerById(Long id) {
        return repository.getInzenjerById(id);
    }

    public void sacuvajInzenjer(Inzenjer i) {
        if (i.getId() == null) {
            repository.dodajInzenjer(i);
        } else {
            repository.azurirajInzenjer(i);
        }
    }

    public void obrisiInzenjer(Long id) {
        repository.obrisiInzenjer(id);
    }

    // --- Licenca CRUD ---
    public List<Licenca> getSveLicence() {
        return repository.getLicence();
    }

    public Licenca getLicencaById(Long id) {
        return repository.getLicencaById(id);
    }

    public void sacuvajLicencu(Licenca l) {
        if (l.getId() == null) {
            repository.dodajLicencu(l);
        } else {
            repository.azurirajLicencu(l);
        }
    }

    public void obrisiLicencu(Long id) {
        repository.obrisiLicencu(id);
    }

    // --- Zadatak CRUD ---
    public List<Zadatak> getSveZadatke() {
        return repository.getZadaci();
    }

    public Zadatak getZadatakById(Long id) {
        return repository.getZadatakById(id);
    }

    public void sacuvajZadatak(Zadatak z) {
        if (z.getId() == null) {
            repository.dodajZadatak(z);
        } else {
            repository.azurirajZadatak(z);
        }
    }

    public void obrisiZadatak(Long id) {
        repository.obrisiZadatak(id);
    }
}
