package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.model.Licenca;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/licence")
public class LicencaController {

    private final AutomatizacijaService service;

    public LicencaController(AutomatizacijaService service) {
        this.service = service;
    }

    @GetMapping("")
    public String listaLicenci(Model model) {
        model.addAttribute("licence", service.getSveLicence());
        return "licence/lista";
    }

    @GetMapping("/novi")
    public String novaForma(Model model) {
        model.addAttribute("licenca", new Licenca());
        return "licence/forma";
    }

    @PostMapping("/sacuvaj")
    public String sacuvaj(@ModelAttribute("licenca") Licenca licenca) {
        service.sacuvajLicencu(licenca);
        return "redirect:/licence";
    }

    @GetMapping("/izmeni/{id}")
    public String izmeniForma(@PathVariable("id") Long id, Model model) {
        Licenca l = service.getLicencaById(id);
        if (l == null) {
            return "redirect:/licence";
        }
        model.addAttribute("licenca", l);
        return "licence/forma";
    }

    @GetMapping("/obrisi/{id}")
    public String obrisi(@PathVariable("id") Long id) {
        service.obrisiLicencu(id);
        return "redirect:/licence";
    }
}
