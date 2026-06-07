package com.metropolitan.it355pz.repository;

import com.metropolitan.it355pz.model.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryRepository {

    private final List<Komponenta> komponente = new ArrayList<>();
    private final List<Licenca> licence = new ArrayList<>();
    private final List<Inzenjer> inzenjeri = new ArrayList<>();
    private final List<Projekat> projekti = new ArrayList<>();
    private final List<Zadatak> zadaci = new ArrayList<>();

    private long nextKomponentaId = 1L;
    private long nextLicencaId = 1L;
    private long nextInzenjerId = 1L;
    private long nextProjekatId = 1L;
    private long nextZadatakId = 1L;

    public InMemoryRepository() {
        inicijalizujPodatke();
    }

    private void inicijalizujPodatke() {
        // Inicijalizacija Inženjera
        dodajInzenjer(new Inzenjer(null, "Nikola", "Živković", "nikola@example.com", "PLC Programer"));
        dodajInzenjer(new Inzenjer(null, "Ilija", "Petrović", "ilija@example.com", "SCADA Inženjer"));
        dodajInzenjer(new Inzenjer(null, "Jovana", "Marković", "jovana@example.com", "Projektant"));

        // Inicijalizacija Projekata
        dodajProjekat(new Projekat(null, "Pumpna stanica Miljkovac", "Vodovod Niš", "U radu"));
        dodajProjekat(new Projekat(null, "Automatizacija proizvodne linije", "Tetra Pak", "Završeno"));

        // Inicijalizacija Komponenti
        dodajKomponentu(new Komponenta(null, "Siemens S7-1200 CPU", "SN-539201", "Siemens", "Na stanju"));
        dodajKomponentu(new Komponenta(null, "HMI Panel KTP700", "SN-982104", "Siemens", "Ugrađeno"));
        dodajKomponentu(new Komponenta(null, "Frekventni regulator G120", "SN-104928", "Siemens", "Na stanju"));

        // Inicijalizacija Licenci
        dodajLicencu(new Licenca(null, "TIA Portal V19", "AAAA-BBBB-CCCC-DDDD", "Floating", true));
        dodajLicencu(new Licenca(null, "WinCC Runtime", "EEEE-FFFF-GGGG-HHHH", "Single", true));

        // Inicijalizacija Zadataka (koristi dodeljene ID-jeve inženjera i projekata: Nikola 1L, Ilija 2L; Miljkovac 1L, Tetra Pak 2L)
        dodajZadatak(new Zadatak(null, "Programiranje PLC logike za pumpe", 1L, 1L, false));
        dodajZadatak(new Zadatak(null, "Dizajniranje SCADA ekrana", 1L, 2L, false));
        dodajZadatak(new Zadatak(null, "Konfiguracija frekventnih regulatora", 2L, 1L, true));
    }

    // --- Komponenta CRUD ---
    public List<Komponenta> getKomponente() {
        return komponente;
    }

    public Komponenta getKomponentaById(Long id) {
        return komponente.stream()
                .filter(k -> k.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void dodajKomponentu(Komponenta k) {
        if (k.getId() == null) {
            k.setId(nextKomponentaId++);
        } else if (k.getId() >= nextKomponentaId) {
            nextKomponentaId = k.getId() + 1;
        }
        komponente.add(k);
    }

    public void azurirajKomponentu(Komponenta k) {
        for (int i = 0; i < komponente.size(); i++) {
            if (komponente.get(i).getId().equals(k.getId())) {
                komponente.set(i, k);
                return;
            }
        }
    }

    public void obrisiKomponentu(Long id) {
        komponente.removeIf(k -> k.getId().equals(id));
    }

    // --- Licenca CRUD ---
    public List<Licenca> getLicence() {
        return licence;
    }

    public Licenca getLicencaById(Long id) {
        return licence.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void dodajLicencu(Licenca l) {
        if (l.getId() == null) {
            l.setId(nextLicencaId++);
        } else if (l.getId() >= nextLicencaId) {
            nextLicencaId = l.getId() + 1;
        }
        licence.add(l);
    }

    public void azurirajLicencu(Licenca l) {
        for (int i = 0; i < licence.size(); i++) {
            if (licence.get(i).getId().equals(l.getId())) {
                licence.set(i, l);
                return;
            }
        }
    }

    public void obrisiLicencu(Long id) {
        licence.removeIf(l -> l.getId().equals(id));
    }

    // --- Inzenjer CRUD ---
    public List<Inzenjer> getInzenjeri() {
        return inzenjeri;
    }

    public Inzenjer getInzenjerById(Long id) {
        return inzenjeri.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void dodajInzenjer(Inzenjer i) {
        if (i.getId() == null) {
            i.setId(nextInzenjerId++);
        } else if (i.getId() >= nextInzenjerId) {
            nextInzenjerId = i.getId() + 1;
        }
        inzenjeri.add(i);
    }

    public void azurirajInzenjer(Inzenjer inz) {
        for (int i = 0; i < inzenjeri.size(); i++) {
            if (inzenjeri.get(i).getId().equals(inz.getId())) {
                inzenjeri.set(i, inz);
                return;
            }
        }
    }

    public void obrisiInzenjer(Long id) {
        inzenjeri.removeIf(i -> i.getId().equals(id));
    }

    // --- Projekat CRUD ---
    public List<Projekat> getProjekti() {
        return projekti;
    }

    public Projekat getProjekatById(Long id) {
        return projekti.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void dodajProjekat(Projekat p) {
        if (p.getId() == null) {
            p.setId(nextProjekatId++);
        } else if (p.getId() >= nextProjekatId) {
            nextProjekatId = p.getId() + 1;
        }
        projekti.add(p);
    }

    public void azurirajProjekat(Projekat p) {
        for (int i = 0; i < projekti.size(); i++) {
            if (projekti.get(i).getId().equals(p.getId())) {
                projekti.set(i, p);
                return;
            }
        }
    }

    public void obrisiProjekat(Long id) {
        projekti.removeIf(p -> p.getId().equals(id));
    }

    // --- Zadatak CRUD ---
    public List<Zadatak> getZadaci() {
        return zadaci;
    }

    public Zadatak getZadatakById(Long id) {
        return zadaci.stream()
                .filter(z -> z.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void dodajZadatak(Zadatak z) {
        if (z.getId() == null) {
            z.setId(nextZadatakId++);
        } else if (z.getId() >= nextZadatakId) {
            nextZadatakId = z.getId() + 1;
        }
        zadaci.add(z);
    }

    public void azurirajZadatak(Zadatak z) {
        for (int i = 0; i < zadaci.size(); i++) {
            if (zadaci.get(i).getId().equals(z.getId())) {
                zadaci.set(i, z);
                return;
            }
        }
    }

    public void obrisiZadatak(Long id) {
        zadaci.removeIf(z -> z.getId().equals(id));
    }
}
