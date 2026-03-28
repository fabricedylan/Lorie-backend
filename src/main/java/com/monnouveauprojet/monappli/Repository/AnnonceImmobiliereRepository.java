package com.monnouveauprojet.monappli.Repository;

import com.monnouveauprojet.monappli.Model.AnnonceImmobiliere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnonceImmobiliereRepository extends JpaRepository<AnnonceImmobiliere, Long> {
}
