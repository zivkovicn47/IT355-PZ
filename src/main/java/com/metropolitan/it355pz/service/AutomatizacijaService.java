package com.metropolitan.it355pz.service;

import com.metropolitan.it355pz.model.*;
import com.metropolitan.it355pz.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutomatizacijaService {

    private final ProjekatRepository projekatRepository;
    private final KomponentaRepository komponentaRepository;
    private final InzenjerRepository inzenjerRepository;
    private final LicencaRepository licencaRepository;
    private final ZadatakRepository zadatakRepository;

    public AutomatizacijaService(ProjekatRepository projekatRepository,
                                 KomponentaRepository komponentaRepository,
                                 InzenjerRepository inzenjerRepository,
                                 LicencaRepository licencaRepository,
                                 ZadatakRepository zadatakRepository) {
        this.projekatRepository = projekatRepository;
        this.komponentaRepository = komponentaRepository;
        this.inzenjerRepository = inzenjerRepository;
        this.licencaRepository = licencaRepository;
        this.zadatakRepository = zadatakRepository;
    }

    // --- Projekat CRUD ---
    public List<Projekat> getSveProjekte() {
        return projekatRepository.findAll();
    }

    public Projekat getProjekatById(Long id) {
        return projekatRepository.findById(id).orElse(null);
    }

    public void sacuvajProjekat(Projekat p) {
        projekatRepository.save(p);
    }

    public void obrisiProjekat(Long id) {
        projekatRepository.deleteById(id);
    }

    // --- Komponenta CRUD ---
    public List<Komponenta> getSveKomponente() {
        return komponentaRepository.findAll();
    }

    public Komponenta getKomponentaById(Long id) {
        return komponentaRepository.findById(id).orElse(null);
    }

    public void sacuvajKomponentu(Komponenta k) {
        komponentaRepository.save(k);
    }

    public void obrisiKomponentu(Long id) {
        komponentaRepository.deleteById(id);
    }

    // --- Inzenjer CRUD ---
    public List<Inzenjer> getSveInzenjere() {
        return inzenjerRepository.findAll();
    }

    public Inzenjer getInzenjerById(Long id) {
        return inzenjerRepository.findById(id).orElse(null);
    }

    public void sacuvajInzenjer(Inzenjer i) {
        inzenjerRepository.save(i);
    }

    public void obrisiInzenjer(Long id) {
        inzenjerRepository.deleteById(id);
    }

    // --- Licenca CRUD ---
    public List<Licenca> getSveLicence() {
        return licencaRepository.findAll();
    }

    public Licenca getLicencaById(Long id) {
        return licencaRepository.findById(id).orElse(null);
    }

    public void sacuvajLicencu(Licenca l) {
        licencaRepository.save(l);
    }

    public void obrisiLicencu(Long id) {
        licencaRepository.deleteById(id);
    }

    // --- Zadatak CRUD ---
    public List<Zadatak> getSveZadatke() {
        return zadatakRepository.findAll();
    }

    public Zadatak getZadatakById(Long id) {
        return zadatakRepository.findById(id).orElse(null);
    }

    public void sacuvajZadatak(Zadatak z) {
        zadatakRepository.save(z);
    }

    public void obrisiZadatak(Long id) {
        zadatakRepository.deleteById(id);
    }
}
