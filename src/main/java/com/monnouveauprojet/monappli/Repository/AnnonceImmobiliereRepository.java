package com.monnouveauprojet.monappli.Repository;

import com.monnouveauprojet.monappli.Model.AnnonceImmobiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnonceImmobiliereRepository extends JpaRepository<AnnonceImmobiliere, Long> {

    // ✅ Cette méthode permet de filtrer les annonces par statut (EN_ATTENTE, VALIDER, etc.)
    List<AnnonceImmobiliere> findByStatut(String statut);

}