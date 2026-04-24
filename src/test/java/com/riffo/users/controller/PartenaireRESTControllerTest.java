package com.riffo.users.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.riffo.users.entity.Partenaire;
import com.riffo.users.service.PartenaireService;

public class PartenaireRESTControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PartenaireService partenaireService;

    @InjectMocks
    private PartenaireRESTController partenaireRESTController;

    private Partenaire partenaire;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(partenaireRESTController).build();

        partenaire = new Partenaire();
        partenaire.setId(1L);
        partenaire.setNom("Test Partenaire");
        partenaire.setEmail("test@example.com");
        partenaire.setCategorie("Santé");
        partenaire.setAdresse("Rue Test");
        partenaire.setVille("Dakar");
        partenaire.setTelephone("771234567");
        partenaire.setLatitude(14.7167);
        partenaire.setLongitude(-17.4677);
        partenaire.setStatut("Actif");
        partenaire.setPlafondPriseEnCharge(100000.0);
    }

    @Test
    void getAllPartenaires_ShouldReturnList() throws Exception {
        List<Partenaire> list = Arrays.asList(partenaire);
        when(partenaireService.getAllPartenaires()).thenReturn(list);

        mockMvc.perform(get("/api/partenaires"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nom").value("Test Partenaire"));
    }

    @Test
    void addPartenaire_ShouldReturnCreated() throws Exception {
        when(partenaireService.addPartenaire(any(Partenaire.class))).thenReturn(partenaire);

        String json = "{\"nom\":\"Test Partenaire\",\"email\":\"test@example.com\",\"categorie\":\"Santé\",\"adresse\":\"Rue Test\",\"ville\":\"Dakar\",\"telephone\":\"771234567\",\"latitude\":14.7167,\"longitude\":-17.4677,\"statut\":\"Actif\",\"plafondPriseEnCharge\":100000.0}";

        mockMvc.perform(post("/api/partenaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Test Partenaire"));
    }

    @Test
    void deletePartenaire_ShouldReturnNoContent() throws Exception {
        doNothing().when(partenaireService).deletePartenaire(1L);

        mockMvc.perform(delete("/api/partenaires/1"))
                .andExpect(status().isNoContent());
    }
}
