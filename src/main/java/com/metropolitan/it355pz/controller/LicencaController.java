package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.model.Licenca;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/licence")
public class LicencaController {

    private final AutomatizacijaService service;

    public LicencaController(AutomatizacijaService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<Licenca>> listaLicenci() {
        return ResponseEntity.ok(service.getSveLicence());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Licenca> getById(@PathVariable("id") Long id) {
        Licenca l = service.getLicencaById(id);
        if (l == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(l);
    }

    @PostMapping("/sacuvaj")
    public ResponseEntity<Void> sacuvaj(@RequestBody Licenca licenca) {
        service.sacuvajLicencu(licenca);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/obrisi/{id}")
    public ResponseEntity<Void> obrisi(@PathVariable("id") Long id) {
        service.obrisiLicencu(id);
        return ResponseEntity.ok().build();
    }
}
