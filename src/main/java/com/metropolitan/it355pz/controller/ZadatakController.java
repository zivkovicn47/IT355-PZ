package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.model.Zadatak;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/zadaci")
public class ZadatakController {

    private final AutomatizacijaService service;

    public ZadatakController(AutomatizacijaService service) {
        this.service = service;
    }

    @GetMapping("")
    public String listaZadataka(Model model) {
        model.addAttribute("zadaci", service.getSveZadatke());
        model.addAttribute("service", service); // Prosleđen servis za pretragu naziva projekta i inženjera u tabeli
        return "zadaci/lista";
    }

    @GetMapping("/novi")
    public String novaForma(Model model) {
        model.addAttribute("zadatak", new Zadatak());
        model.addAttribute("projekti", service.getSveProjekte());
        model.addAttribute("inzenjeri", service.getSveInzenjere());
        return "zadaci/forma";
    }

    @PostMapping("/sacuvaj")
    public String sacuvaj(@ModelAttribute("zadatak") Zadatak zadatak) {
        service.sacuvajZadatak(zadatak);
        return "redirect:/zadaci";
    }

    @GetMapping("/izmeni/{id}")
    public String izmeniForma(@PathVariable("id") Long id, Model model) {
        Zadatak z = service.getZadatakById(id);
        if (z == null) {
            return "redirect:/zadaci";
        }
        model.addAttribute("zadatak", z);
        model.addAttribute("projekti", service.getSveProjekte());
        model.addAttribute("inzenjeri", service.getSveInzenjere());
        return "zadaci/forma";
    }

    @GetMapping("/obrisi/{id}")
    public String obrisi(@PathVariable("id") Long id) {
        service.obrisiZadatak(id);
        return "redirect:/zadaci";
    }
}
