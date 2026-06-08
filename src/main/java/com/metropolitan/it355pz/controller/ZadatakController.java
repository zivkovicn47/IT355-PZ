package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.model.Zadatak;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zadaci")
public class ZadatakController {

    private final AutomatizacijaService service;

    public ZadatakController(AutomatizacijaService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<Zadatak>> listaZadataka() {
        return ResponseEntity.ok(service.getSveZadatke());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zadatak> getById(@PathVariable("id") Long id) {
        Zadatak z = service.getZadatakById(id);
        if (z == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(z);
    }

    @PostMapping("/sacuvaj")
    public ResponseEntity<Void> sacuvaj(@RequestBody Zadatak zadatak) {
        service.sacuvajZadatak(zadatak);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/obrisi/{id}")
    public ResponseEntity<Void> obrisi(@PathVariable("id") Long id) {
        service.obrisiZadatak(id);
        return ResponseEntity.ok().build();
    }
}
