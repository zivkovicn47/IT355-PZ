package com.metropolitan.it355pz.service;

import com.metropolitan.it355pz.model.Projekat;
import com.metropolitan.it355pz.repository.ProjekatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class AutomatizacijaServiceTest {

    @Mock
    private ProjekatRepository projekatRepository;

    @InjectMocks
    private AutomatizacijaService service;

    @Test
    public void testGetSveProjekte() {
        // given
        Projekat p1 = new Projekat(1L, "Proj1", "Klijent1", "U radu");
        Projekat p2 = new Projekat(2L, "Proj2", "Klijent2", "Zavrseno");
        given(projekatRepository.findAll()).willReturn(Arrays.asList(p1, p2));

        // when
        List<Projekat> projekti = service.getSveProjekte();

        // then
        assertNotNull(projekti);
        assertEquals(2, projekti.size());
        assertEquals("Proj1", projekti.get(0).getNaziv());
        verify(projekatRepository, times(1)).findAll();
    }

    @Test
    public void testSacuvajProjekat() {
        // given
        Projekat p = new Projekat(null, "ProjNew", "KlijentNew", "U radu");
        given(projekatRepository.save(p)).willReturn(p);

        // when
        service.sacuvajProjekat(p);

        // then
        verify(projekatRepository, times(1)).save(p);
    }

    @Test
    public void testObrisiProjekat() {
        // given
        Long id = 1L;
        willDoNothing().given(projekatRepository).deleteById(id);

        // when
        service.obrisiProjekat(id);

        // then
        verify(projekatRepository, times(1)).deleteById(id);
    }
}
