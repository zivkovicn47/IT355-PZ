package com.metropolitan.it355pz.controller;

import com.metropolitan.it355pz.config.SecurityConfig;
import com.metropolitan.it355pz.model.Projekat;
import com.metropolitan.it355pz.security.JwtUtil;
import com.metropolitan.it355pz.service.AutomatizacijaService;
import com.metropolitan.it355pz.service.ExcelExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjekatController.class)
@Import(SecurityConfig.class)
public class ProjekatControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutomatizacijaService automatizacijaService;

    @MockitoBean
    private ExcelExportService excelExportService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    public void testGetSveProjekte_vratiListu() throws Exception {
        given(automatizacijaService.getSveProjekte()).willReturn(List.of(
                new Projekat(1L, "Test projekat", "Test klijent", "U radu")
        ));

        mockMvc.perform(get("/api/projekti").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].naziv").value("Test projekat"))
                .andExpect(jsonPath("$[0].klijent").value("Test klijent"));
    }

    @Test
    public void testGetById_nePostoji_vrati404() throws Exception {
        given(automatizacijaService.getProjekatById(999L)).willReturn(null);

        mockMvc.perform(get("/api/projekti/999").with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSacuvaj_kaoUser_vrati403() throws Exception {
        mockMvc.perform(post("/api/projekti/sacuvaj")
                        .with(user("user").roles("USER"))
                        .contentType("application/json")
                        .content("{\"naziv\":\"Test\",\"klijent\":\"Klijent\",\"status\":\"U radu\"}"))
                .andExpect(status().isForbidden());
    }
}
