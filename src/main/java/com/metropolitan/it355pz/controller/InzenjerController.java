package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.model.Inzenjer;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inzenjeri")
public class InzenjerController {

    private final AutomatizacijaService service;

    public InzenjerController(AutomatizacijaService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<Inzenjer>> listaInzenjera() {
        return ResponseEntity.ok(service.getSveInzenjere());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inzenjer> getById(@PathVariable("id") Long id) {
        Inzenjer i = service.getInzenjerById(id);
        if (i == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(i);
    }

    @PostMapping("/sacuvaj")
    public ResponseEntity<Void> sacuvaj(@RequestBody Inzenjer inzenjer) {
        service.sacuvajInzenjer(inzenjer);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/obrisi/{id}")
    public ResponseEntity<Void> obrisi(@PathVariable("id") Long id) {
        service.obrisiInzenjer(id);
        return ResponseEntity.ok().build();
    }
}
