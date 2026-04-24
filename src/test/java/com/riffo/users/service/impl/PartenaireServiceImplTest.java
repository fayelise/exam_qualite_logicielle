package com.riffo.users.service.impl;

import com.riffo.users.entity.Partenaire;
import com.riffo.users.exception.ResourceNotFoundException;
import com.riffo.users.repository.PartenaireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartenaireServiceImplTest {

    @Mock
    private PartenaireRepository partenaireRepository;

    @InjectMocks
    private PartenaireServiceImpl partenaireService;

    private Partenaire partenaire;

    @BeforeEach
    void setUp() {
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
    void addPartenaire_ShouldReturnSavedPartenaire() {
        // Arrange
        when(partenaireRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(partenaireRepository.save(any(Partenaire.class))).thenReturn(partenaire);

        // Act
        Partenaire savedPartenaire = partenaireService.addPartenaire(partenaire);

        // Assert
        assertThat(savedPartenaire).isNotNull();
        assertThat(savedPartenaire.getNom()).isEqualTo("Test Partenaire");
        verify(partenaireRepository, times(1)).save(any(Partenaire.class));
    }

    @Test
    void getPartenaireById_ShouldReturnPartenaire_WhenIdExists() {
        // Arrange
        when(partenaireRepository.findById(1L)).thenReturn(Optional.of(partenaire));

        // Act
        Optional<Partenaire> foundPartenaire = partenaireService.getPartenaireById(1L);

        // Assert
        assertThat(foundPartenaire).isPresent();
        assertThat(foundPartenaire.get().getId()).isEqualTo(1L);
    }

    @Test
    void deletePartenaire_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(partenaireRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            partenaireService.deletePartenaire(99L);
        });
        verify(partenaireRepository, never()).deleteById(anyLong());
    }
}
