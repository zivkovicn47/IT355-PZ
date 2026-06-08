package com.metropolitan.it355pz.config;

import com.metropolitan.it355pz.model.*;
import com.metropolitan.it355pz.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final InzenjerRepository inzenjerRepository;
    private final ProjekatRepository projekatRepository;
    private final KomponentaRepository komponentaRepository;
    private final LicencaRepository licencaRepository;
    private final ZadatakRepository zadatakRepository;

    public DataInitializer(InzenjerRepository inzenjerRepository,
                           ProjekatRepository projekatRepository,
                           KomponentaRepository komponentaRepository,
                           LicencaRepository licencaRepository,
                           ZadatakRepository zadatakRepository) {
        this.inzenjerRepository = inzenjerRepository;
        this.projekatRepository = projekatRepository;
        this.komponentaRepository = komponentaRepository;
        this.licencaRepository = licencaRepository;
        this.zadatakRepository = zadatakRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (inzenjerRepository.count() == 0 && projekatRepository.count() == 0) {
            // Seed Inzenjeri
            inzenjerRepository.save(new Inzenjer(null, "Nikola", "Živković", "nikola@example.com", "PLC Programer"));
            inzenjerRepository.save(new Inzenjer(null, "Ilija", "Petrović", "ilija@example.com", "SCADA Inženjer"));
            inzenjerRepository.save(new Inzenjer(null, "Jovana", "Marković", "jovana@example.com", "Projektant"));

            // Seed Projekti
            projekatRepository.save(new Projekat(null, "Pumpna stanica Miljkovac", "Vodovod Niš", "U radu"));
            projekatRepository.save(new Projekat(null, "Automatizacija proizvodne linije", "Tetra Pak", "Završeno"));

            // Seed Komponente
            komponentaRepository.save(new Komponenta(null, "Siemens S7-1200 CPU", "SN-539201", "Siemens", "Na stanju"));
            komponentaRepository.save(new Komponenta(null, "HMI Panel KTP700", "SN-982104", "Siemens", "Ugrađeno"));
            komponentaRepository.save(new Komponenta(null, "Frekventni regulator G120", "SN-104928", "Siemens", "Na stanju"));

            // Seed Licence
            licencaRepository.save(new Licenca(null, "TIA Portal V19", "AAAA-BBBB-CCCC-DDDD", "Floating", true));
            licencaRepository.save(new Licenca(null, "WinCC Runtime", "EEEE-FFFF-GGGG-HHHH", "Single", true));

            // Seed Zadaci
            // Nikola (ID 1), Ilija (ID 2); Miljkovac (ID 1), Tetra Pak (ID 2)
            zadatakRepository.save(new Zadatak(null, "Programiranje PLC logike za pumpe", 1L, 1L, false));
            zadatakRepository.save(new Zadatak(null, "Dizajniranje SCADA ekrana", 1L, 2L, false));
            zadatakRepository.save(new Zadatak(null, "Konfiguracija frekventnih regulatora", 2L, 1L, true));
        }
    }
}
