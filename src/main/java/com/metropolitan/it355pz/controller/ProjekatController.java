package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.model.Projekat;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projekti")
public class ProjekatController {

    private final AutomatizacijaService service;

    public ProjekatController(AutomatizacijaService service) {
        this.service = service;
    }

    @GetMapping("")
    public String listaProjekata(Model model) {
        model.addAttribute("projekti", service.getSveProjekte());
        return "projekti/lista";
    }

    @GetMapping("/novi")
    public String novaForma(Model model) {
        model.addAttribute("projekat", new Projekat());
        return "projekti/forma";
    }

    @PostMapping("/sacuvaj")
    public String sacuvaj(@ModelAttribute("projekat") Projekat projekat) {
        service.sacuvajProjekat(projekat);
        return "redirect:/projekti";
    }

    @GetMapping("/izmeni/{id}")
    public String izmeniForma(@PathVariable("id") Long id, Model model) {
        Projekat p = service.getProjekatById(id);
        if (p == null) {
            return "redirect:/projekti";
        }
        model.addAttribute("projekat", p);
        return "projekti/forma";
    }

    @GetMapping("/obrisi/{id}")
    public String obrisi(@PathVariable("id") Long id) {
        service.obrisiProjekat(id);
        return "redirect:/projekti";
    }
}
