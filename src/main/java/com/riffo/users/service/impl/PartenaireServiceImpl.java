package com.riffo.users.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riffo.users.entity.Partenaire;
import com.riffo.users.exception.DuplicateResourceException;
import com.riffo.users.exception.ResourceNotFoundException;
import com.riffo.users.repository.PartenaireRepository;
import com.riffo.users.service.PartenaireService;
/**
 * Implémentation du service de gestion des partenaires
 */
@Service
@Transactional
public class PartenaireServiceImpl implements PartenaireService {

    private final PartenaireRepository partenaireRepository;

    @Autowired
    public PartenaireServiceImpl(PartenaireRepository partenaireRepository) {
        this.partenaireRepository = partenaireRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partenaire> getAllPartenaires() {
        return partenaireRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partenaire> getPartenaireById(Long id) {
        return partenaireRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partenaire> getPartenaireByNom(String nom) {
        return partenaireRepository.findByNom(nom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partenaire> getPartenairesByCategorie(String categorie) {
        return partenaireRepository.findByCategorie(categorie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partenaire> getPartenairesByStatut(String statut) {
        return partenaireRepository.findByStatut(statut);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partenaire> getPartenairesByVille(String ville) {
        return partenaireRepository.findByVille(ville);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partenaire> getPartenaireByEmail(String email) {
        return partenaireRepository.findByEmail(email);
    }

    @Override
    public Partenaire addPartenaire(Partenaire partenaire) {
        if (partenaire == null) {
            throw new IllegalArgumentException("Le partenaire ne peut pas être null");
        }
        
        // Vérifier si l'email existe déjà
        if (existsByEmail(partenaire.getEmail())) {
            throw new DuplicateResourceException("Un partenaire avec l'email " + partenaire.getEmail() + " existe déjà");
        }
        
        return partenaireRepository.save(partenaire);
    }

    @Override
    public Partenaire updatePartenaire(Long id, Partenaire partenaire) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("L'ID du partenaire est invalide");
        }
        
        if (partenaire == null) {
            throw new IllegalArgumentException("Le partenaire ne peut pas être null");
        }
        
        Partenaire p = partenaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Le partenaire avec l'ID " + id + " n'existe pas"));
        
        // Mettre à jour les champs
        if (partenaire.getNom() != null) {
            p.setNom(partenaire.getNom());
        }
        if (partenaire.getCategorie() != null) {
            p.setCategorie(partenaire.getCategorie());
        }
        if (partenaire.getAdresse() != null) {
            p.setAdresse(partenaire.getAdresse());
        }
        if (partenaire.getVille() != null) {
            p.setVille(partenaire.getVille());
        }
        if (partenaire.getTelephone() != null) {
            p.setTelephone(partenaire.getTelephone());
        }
        
        // Vérification de l'email si modifié
        if (partenaire.getEmail() != null && !partenaire.getEmail().equals(p.getEmail())) {
            if (existsByEmail(partenaire.getEmail())) {
                throw new DuplicateResourceException("Un partenaire avec l'email " + partenaire.getEmail() + " existe déjà");
            }
            p.setEmail(partenaire.getEmail());
        }
        
        if (partenaire.getLatitude() != null) {
            p.setLatitude(partenaire.getLatitude());
        }
        if (partenaire.getLongitude() != null) {
            p.setLongitude(partenaire.getLongitude());
        }
        if (partenaire.getStatut() != null) {
            p.setStatut(partenaire.getStatut());
        }
        if (partenaire.getPlafondPriseEnCharge() != null) {
            p.setPlafondPriseEnCharge(partenaire.getPlafondPriseEnCharge());
        }
        
        return partenaireRepository.save(p);
    }

    @Override
    public void deletePartenaire(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("L'ID du partenaire est invalide");
        }
        
        if (!partenaireRepository.existsById(id)) {
            throw new ResourceNotFoundException("Le partenaire avec l'ID " + id + " n'existe pas");
        }
        
        partenaireRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPartenaires() {
        return partenaireRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return partenaireRepository.findByEmail(email).isPresent();
    }
}
