package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.model.Komponenta;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/komponente")
public class KomponentaController {

    private final AutomatizacijaService service;

    public KomponentaController(AutomatizacijaService service) {
        this.service = service;
    }

    @GetMapping("")
    public String listaKomponenti(Model model) {
        model.addAttribute("komponente", service.getSveKomponente());
        return "komponente/lista";
    }

    @GetMapping("/novi")
    public String novaForma(Model model) {
        model.addAttribute("komponenta", new Komponenta());
        return "komponente/forma";
    }

    @PostMapping("/sacuvaj")
    public String sacuvaj(@ModelAttribute("komponenta") Komponenta komponenta) {
        service.sacuvajKomponentu(komponenta);
        return "redirect:/komponente";
    }

    @GetMapping("/izmeni/{id}")
    public String izmeniForma(@PathVariable("id") Long id, Model model) {
        Komponenta k = service.getKomponentaById(id);
        if (k == null) {
            return "redirect:/komponente";
        }
        model.addAttribute("komponenta", k);
        return "komponente/forma";
    }

    @GetMapping("/obrisi/{id}")
    public String obrisi(@PathVariable("id") Long id) {
        service.obrisiKomponentu(id);
        return "redirect:/komponente";
    }
}
