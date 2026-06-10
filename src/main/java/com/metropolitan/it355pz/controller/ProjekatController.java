package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.model.Projekat;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import com.metropolitan.it355pz.service.ExcelExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/projekti")
public class ProjekatController {

    private final AutomatizacijaService service;
    private final ExcelExportService excelExportService;

    public ProjekatController(AutomatizacijaService service, ExcelExportService excelExportService) {
        this.service = service;
        this.excelExportService = excelExportService;
    }

    @GetMapping("")
    public ResponseEntity<List<Projekat>> listaProjekata() {
        return ResponseEntity.ok(service.getSveProjekte());
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel() {
        try {
            List<Projekat> projekti = service.getSveProjekte();
            ByteArrayInputStream in = excelExportService.exportProjekti(projekti);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=projekti.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(in.readAllBytes());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
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
