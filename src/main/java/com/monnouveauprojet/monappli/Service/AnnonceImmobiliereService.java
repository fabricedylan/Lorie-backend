package com.monnouveauprojet.monappli.Service;

import com.monnouveauprojet.monappli.Model.AnnonceImmobiliere;
import com.monnouveauprojet.monappli.Model.User;
import com.monnouveauprojet.monappli.Repository.AnnonceImmobiliereRepository;
import com.monnouveauprojet.monappli.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnonceImmobiliereService {

    private final AnnonceImmobiliereRepository repo;
    private final UserRepository userRepository;

    public AnnonceImmobiliereService(AnnonceImmobiliereRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

    public AnnonceImmobiliere creerAnnonce(AnnonceImmobiliere annonce, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + userId));

        annonce.setUser(user);
        return repo.save(annonce);
    }

    public AnnonceImmobiliere trouverParId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<AnnonceImmobiliere> listerToutesLesAnnonces() {
        return repo.findAll();
    }

    public void supprimerAnnonce(Long id) {
        AnnonceImmobiliere annonce = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée avec l'id : " + id));
        repo.delete(annonce);
    }

    @Transactional
    public AnnonceImmobiliere updateAnnonce(Long id, AnnonceImmobiliere details) {
        AnnonceImmobiliere existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce introuvable"));

        existing.setTitre(details.getTitre());
        existing.setDescription(details.getDescription());
        existing.setPrix(details.getPrix());
        existing.setVille(details.getVille());
        existing.setAdresse(details.getAdresse());

        if (details.getImage() != null) {
            existing.setImage(details.getImage());
        }

        return repo.save(existing);
    }

    // ============================================================
    // 🛠️ AJOUTS POUR L'ADMINISTRATION (MODÉRATION)
    // ============================================================

    /**
     * ✅ Liste les annonces par statut (ex: "EN_ATTENTE")
     */
    public List<AnnonceImmobiliere> listerAnnoncesParStatut(String statut) {
        return repo.findByStatut(statut);
    }

    /**
     * ✅ Met à jour uniquement le statut (Validation ou Rejet)
     */
    @Transactional
    public AnnonceImmobiliere updateStatut(Long id, String nouveauStatut) {
        AnnonceImmobiliere existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce introuvable avec l'id : " + id));

        existing.setStatut(nouveauStatut);
        return repo.save(existing);
    }
}