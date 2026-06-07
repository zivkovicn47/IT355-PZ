package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.model.Inzenjer;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inzenjeri")
public class InzenjerController {

    private final AutomatizacijaService service;

    public InzenjerController(AutomatizacijaService service) {
        this.service = service;
    }

    @GetMapping("")
    public String listaInzenjera(Model model) {
        model.addAttribute("inzenjeri", service.getSveInzenjere());
        return "inzenjeri/lista";
    }

    @GetMapping("/novi")
    public String novaForma(Model model) {
        model.addAttribute("inzenjer", new Inzenjer());
        return "inzenjeri/forma";
    }

    @PostMapping("/sacuvaj")
    public String sacuvaj(@ModelAttribute("inzenjer") Inzenjer inzenjer) {
        service.sacuvajInzenjer(inzenjer);
        return "redirect:/inzenjeri";
    }

    @GetMapping("/izmeni/{id}")
    public String izmeniForma(@PathVariable("id") Long id, Model model) {
        Inzenjer i = service.getInzenjerById(id);
        if (i == null) {
            return "redirect:/inzenjeri";
        }
        model.addAttribute("inzenjer", i);
        return "inzenjeri/forma";
    }

    @GetMapping("/obrisi/{id}")
    public String obrisi(@PathVariable("id") Long id) {
        service.obrisiInzenjer(id);
        return "redirect:/inzenjeri";
    }
}
