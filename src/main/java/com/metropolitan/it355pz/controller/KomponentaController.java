package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.model.Komponenta;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/komponente")
public class KomponentaController {

    private final AutomatizacijaService service;

    public KomponentaController(AutomatizacijaService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<Komponenta>> listaKomponenti() {
        return ResponseEntity.ok(service.getSveKomponente());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Komponenta> getById(@PathVariable("id") Long id) {
        Komponenta k = service.getKomponentaById(id);
        if (k == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(k);
    }

    @PostMapping("/sacuvaj")
    public ResponseEntity<Void> sacuvaj(@RequestBody Komponenta komponenta) {
        service.sacuvajKomponentu(komponenta);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/obrisi/{id}")
    public ResponseEntity<Void> obrisi(@PathVariable("id") Long id) {
        service.obrisiKomponentu(id);
        return ResponseEntity.ok().build();
    }
}
