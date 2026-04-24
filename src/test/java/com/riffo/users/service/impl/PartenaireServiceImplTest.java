package com.riffo.users.service.impl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riffo.users.entity.Partenaire;
import com.riffo.users.exception.ResourceNotFoundException;
import com.riffo.users.repository.PartenaireRepository;

/**
 * Tests unitaires pour le service PartenaireServiceImpl.
 * Utilise Mockito pour simuler le comportement du repository.
 */
@ExtendWith(MockitoExtension.class)
public class PartenaireServiceImplTest {

    @Mock
    private PartenaireRepository partenaireRepository;

    @InjectMocks
    private PartenaireServiceImpl partenaireService;

    private Partenaire partenaire;

    @BeforeEach
    void setUp() {
        // Initialisation d'un partenaire de test avant chaque méthode de test
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
     * Test de récupération de tous les partenaires.
     */
    @Test
    void getAllPartenaires_ShouldReturnList() {
        when(partenaireRepository.findAll()).thenReturn(java.util.List.of(partenaire));
        java.util.List<Partenaire> list = partenaireService.getAllPartenaires();
        assertThat(list).hasSize(1);
    }

    /**
     * Test de recherche d'un partenaire par son identifiant.
     */
    @Test
    void getPartenaireById_ShouldReturnPartenaire_WhenIdExists() {
        when(partenaireRepository.findById(1L)).thenReturn(Optional.of(partenaire));
        Optional<Partenaire> found = partenaireService.getPartenaireById(1L);
        assertThat(found).isPresent();
    }

    /**
     * Test de recherche d'un partenaire par son nom.
     */
    @Test
    void getPartenaireByNom_ShouldReturnPartenaire() {
        when(partenaireRepository.findByNom("Test Partenaire")).thenReturn(Optional.of(partenaire));
        Optional<Partenaire> found = partenaireService.getPartenaireByNom("Test Partenaire");
        assertThat(found).isPresent();
    }

    /**
     * Test de recherche par catégorie.
     */
    @Test
    void getPartenairesByCategorie_ShouldReturnList() {
        when(partenaireRepository.findByCategorie("Santé")).thenReturn(java.util.List.of(partenaire));
        java.util.List<Partenaire> list = partenaireService.getPartenairesByCategorie("Santé");
        assertThat(list).hasSize(1);
    }

    /**
     * Test de recherche par email.
     */
    @Test
    void getPartenaireByEmail_ShouldReturnPartenaire() {
        when(partenaireRepository.findByEmail("test@example.com")).thenReturn(Optional.of(partenaire));
        Optional<Partenaire> found = partenaireService.getPartenaireByEmail("test@example.com");
        assertThat(found).isPresent();
    }

    /**
     * Test de l'ajout réussi d'un partenaire.
     */
    @Test
    void addPartenaire_ShouldReturnSavedPartenaire() {
        when(partenaireRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(partenaireRepository.save(any(Partenaire.class))).thenReturn(partenaire);
        Partenaire saved = partenaireService.addPartenaire(partenaire);
        assertThat(saved).isNotNull();
        verify(partenaireRepository).save(any(Partenaire.class));
    }

    /**
     * Test de l'ajout d'un partenaire avec un email déjà existant.
     */
    @Test
    void addPartenaire_ShouldThrowException_WhenEmailExists() {
        when(partenaireRepository.findByEmail(anyString())).thenReturn(Optional.of(partenaire));
        assertThrows(com.riffo.users.exception.DuplicateResourceException.class, () -> {
            partenaireService.addPartenaire(partenaire);
        });
    }

    /**
     * Test de mise à jour partielle des champs d'un partenaire.
     */
    @Test
    void updatePartenaire_ShouldUpdateFields() {
        Partenaire updatedInfo = new Partenaire();
        updatedInfo.setNom("Nouveau Nom");
        updatedInfo.setEmail("nouveau@example.com");

        when(partenaireRepository.findById(1L)).thenReturn(Optional.of(partenaire));
        when(partenaireRepository.findByEmail("nouveau@example.com")).thenReturn(Optional.empty());
        when(partenaireRepository.save(any(Partenaire.class))).thenReturn(partenaire);

        Partenaire result = partenaireService.updatePartenaire(1L, updatedInfo);
        assertThat(result.getNom()).isEqualTo("Nouveau Nom");
        assertThat(result.getEmail()).isEqualTo("nouveau@example.com");
    }

    /**
     * Test de la suppression d'un partenaire existant.
     */
    @Test
    void deletePartenaire_ShouldDelete_WhenIdExists() {
        when(partenaireRepository.existsById(1L)).thenReturn(true);
        partenaireService.deletePartenaire(1L);
        verify(partenaireRepository).deleteById(1L);
    }

    /**
     * Test de la suppression d'un partenaire inexistant (doit lancer une exception).
     */
    @Test
    void deletePartenaire_ShouldThrowException_WhenIdDoesNotExist() {
        when(partenaireRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> {
            partenaireService.deletePartenaire(99L);
        });
    }

    /**
     * Test du comptage des partenaires.
     */
    @Test
    void countPartenaires_ShouldReturnCount() {
        when(partenaireRepository.count()).thenReturn(10L);
        long count = partenaireService.countPartenaires();
        assertThat(count).isEqualTo(10L);
    }

    /**
     * Test de recherche par statut.
     */
    @Test
    void getPartenairesByStatut_ShouldReturnList() {
        when(partenaireRepository.findByStatut("Actif")).thenReturn(java.util.List.of(partenaire));
        java.util.List<Partenaire> list = partenaireService.getPartenairesByStatut("Actif");
        assertThat(list).hasSize(1);
    }

    /**
     * Test de recherche par ville.
     */
    @Test
    void getPartenairesByVille_ShouldReturnList() {
        when(partenaireRepository.findByVille("Dakar")).thenReturn(java.util.List.of(partenaire));
        java.util.List<Partenaire> list = partenaireService.getPartenairesByVille("Dakar");
        assertThat(list).hasSize(1);
    }

    /**
     * Test de validation : le partenaire ne peut pas être null lors de l'ajout.
     */
    @Test
    void addPartenaire_ShouldThrowException_WhenPartenaireIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            partenaireService.addPartenaire(null);
        });
    }

    /**
     * Test de validation : ID invalide lors de la mise à jour.
     */
    @Test
    void updatePartenaire_ShouldThrowException_WhenIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            partenaireService.updatePartenaire(0L, partenaire);
        });
    }

    /**
     * Test de validation : partenaire null lors de la mise à jour.
     */
    @Test
    void updatePartenaire_ShouldThrowException_WhenPartenaireIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            partenaireService.updatePartenaire(1L, null);
        });
    }

    /**
     * Test de validation : ID invalide lors de la suppression.
     */
    @Test
    void deletePartenaire_ShouldThrowException_WhenIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            partenaireService.deletePartenaire(-1L);
        });
    }

    /**
     * Test de l'existence par email.
     */
    @Test
    void existsByEmail_ShouldReturnTrue_WhenExists() {
        when(partenaireRepository.findByEmail("test@example.com")).thenReturn(Optional.of(partenaire));
        boolean exists = partenaireService.existsByEmail("test@example.com");
        assertThat(exists).isTrue();
    }

    /**
     * Test de mise à jour avec un email qui appartient déjà à un autre partenaire.
     */
    @Test
    void updatePartenaire_ShouldThrowException_WhenEmailAlreadyExists() {
        Partenaire updatedInfo = new Partenaire();
        updatedInfo.setEmail("existing@example.com");

        when(partenaireRepository.findById(1L)).thenReturn(Optional.of(partenaire));
        when(partenaireRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new Partenaire()));

        assertThrows(com.riffo.users.exception.DuplicateResourceException.class, () -> {
            partenaireService.updatePartenaire(1L, updatedInfo);
        });
    }

    /**
     * Test de la mise à jour de l'ensemble des champs d'un partenaire.
     */
    @Test
    void updatePartenaire_ShouldUpdateAllFields() {
        Partenaire updatedInfo = new Partenaire();
        updatedInfo.setNom("New Nom");
        updatedInfo.setCategorie("New Cat");
        updatedInfo.setAdresse("New Addr");
        updatedInfo.setVille("New Ville");
        updatedInfo.setTelephone("New Tel");
        updatedInfo.setLatitude(1.0);
        updatedInfo.setLongitude(2.0);
        updatedInfo.setStatut("New Status");
        updatedInfo.setPlafondPriseEnCharge(5000.0);

        when(partenaireRepository.findById(1L)).thenReturn(Optional.of(partenaire));
        when(partenaireRepository.save(any(Partenaire.class))).thenReturn(partenaire);

        Partenaire result = partenaireService.updatePartenaire(1L, updatedInfo);
        
        assertThat(result.getNom()).isEqualTo("New Nom");
        assertThat(result.getCategorie()).isEqualTo("New Cat");
        assertThat(result.getAdresse()).isEqualTo("New Addr");
        assertThat(result.getVille()).isEqualTo("New Ville");
        assertThat(result.getTelephone()).isEqualTo("New Tel");
        assertThat(result.getLatitude()).isEqualTo(1.0);
        assertThat(result.getLongitude()).isEqualTo(2.0);
        assertThat(result.getStatut()).isEqualTo("New Status");
        assertThat(result.getPlafondPriseEnCharge()).isEqualTo(5000.0);
    }
}
