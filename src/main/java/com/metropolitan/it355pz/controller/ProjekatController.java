package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.model.Projekat;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projekti")
public class ProjekatController {

    private final AutomatizacijaService service;

    public ProjekatController(AutomatizacijaService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<Projekat>> listaProjekata() {
        return ResponseEntity.ok(service.getSveProjekte());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projekat> getById(@PathVariable("id") Long id) {
        Projekat p = service.getProjekatById(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @PostMapping("/sacuvaj")
    public ResponseEntity<Void> sacuvaj(@RequestBody Projekat projekat) {
        service.sacuvajProjekat(projekat);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/obrisi/{id}")
    public ResponseEntity<Void> obrisi(@PathVariable("id") Long id) {
        service.obrisiProjekat(id);
        return ResponseEntity.ok().build();
    }
}
