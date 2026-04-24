package com.riffo.users.controller;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.riffo.users.entity.Partenaire;
import com.riffo.users.service.PartenaireService;

/**
 * Tests d'intégration pour le contrôleur REST PartenaireRESTController.
 * Utilise MockMvc pour simuler des appels HTTP aux endpoints REST.
 */
public class PartenaireRESTControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PartenaireService partenaireService;

    @InjectMocks
    private PartenaireRESTController partenaireRESTController;

    private Partenaire partenaire;

    @BeforeEach
    void setUp() {
        // Initialisation de Mockito et configuration du contexte MockMvc
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(partenaireRESTController).build();

        // Initialisation d'un partenaire de test
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

    /**
     * Test de l'endpoint GET /api/partenaires pour récupérer tous les partenaires.
     */
    @Test
    void getAllPartenaires_ShouldReturnList() throws Exception {
        when(partenaireService.getAllPartenaires()).thenReturn(Arrays.asList(partenaire));

        mockMvc.perform(get("/api/partenaires"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Test Partenaire"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    /**
     * Test de l'endpoint GET /api/partenaires/{id} pour un partenaire existant.
     */
    @Test
    void getPartenaireById_ShouldReturnPartenaire_WhenExists() throws Exception {
        when(partenaireService.getPartenaireById(1L)).thenReturn(Optional.of(partenaire));

        mockMvc.perform(get("/api/partenaires/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Test Partenaire"));
    }

    /**
     * Test de l'endpoint GET /api/partenaires/{id} pour un partenaire inexistant (404).
     */
    @Test
    void getPartenaireById_ShouldReturn404_WhenNotExists() throws Exception {
        when(partenaireService.getPartenaireById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/partenaires/99"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test de l'endpoint POST /api/partenaires pour la création d'un partenaire.
     */
    @Test
    void addPartenaire_ShouldReturnCreatedPartenaire() throws Exception {
        when(partenaireService.addPartenaire(any(Partenaire.class))).thenReturn(partenaire);

        // Corps de la requête JSON (les champs doivent correspondre aux validations de l'entité)
        String json = "{\"nom\":\"Test Partenaire\",\"email\":\"test@example.com\",\"categorie\":\"Santé\",\"adresse\":\"Rue Test\",\"ville\":\"Dakar\",\"telephone\":\"771234567\",\"latitude\":14.7167,\"longitude\":-17.4677,\"statut\":\"Actif\",\"plafondPriseEnCharge\":100000.0}";

        mockMvc.perform(post("/api/partenaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Test Partenaire"));
    }

    /**
     * Test de l'endpoint PUT /api/partenaires/{id} pour la mise à jour d'un partenaire.
     */
    @Test
    void updatePartenaire_ShouldReturnOk() throws Exception {
        when(partenaireService.updatePartenaire(anyLong(), any(Partenaire.class))).thenReturn(partenaire);

        String json = "{\"nom\":\"Test Partenaire\",\"email\":\"test@example.com\",\"categorie\":\"Santé\",\"adresse\":\"Rue Test\",\"ville\":\"Dakar\",\"telephone\":\"771234567\",\"latitude\":14.7167,\"longitude\":-17.4677,\"statut\":\"Actif\",\"plafondPriseEnCharge\":100000.0}";

        mockMvc.perform(put("/api/partenaires/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Test Partenaire"));
    }

    /**
     * Test de l'endpoint DELETE /api/partenaires/{id} pour la suppression d'un partenaire.
     */
    @Test
    void deletePartenaire_ShouldReturnOk() throws Exception {
        doNothing().when(partenaireService).deletePartenaire(1L);

        mockMvc.perform(delete("/api/partenaires/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Test de l'endpoint GET /api/partenaires/count pour obtenir le nombre total de partenaires.
     */
    @Test
    void countPartenaires_ShouldReturnCount() throws Exception {
        when(partenaireService.countPartenaires()).thenReturn(5L);

        mockMvc.perform(get("/api/partenaires/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}
