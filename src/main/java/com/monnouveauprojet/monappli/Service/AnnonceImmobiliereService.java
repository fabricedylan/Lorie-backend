package com.monnouveauprojet.monappli.Service;

import com.monnouveauprojet.monappli.Model.AnnonceImmobiliere;
import com.monnouveauprojet.monappli.Model.User;
import com.monnouveauprojet.monappli.Repository.AnnonceImmobiliereRepository;
import com.monnouveauprojet.monappli.Repository.UserRepository; // ✅ Ajout du repository User
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnonceImmobiliereService {

    private final AnnonceImmobiliereRepository repo;
    private final UserRepository userRepository; // ✅ Nouveau champ

    // ✅ Constructeur mis à jour pour injecter les deux repositories
    public AnnonceImmobiliereService(AnnonceImmobiliereRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

    // ✅ MODIFICATION : On lie l'annonce à l'utilisateur avant de sauvegarder
    public AnnonceImmobiliere creerAnnonce(AnnonceImmobiliere annonce, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + userId));

        annonce.setUser(user); // ✅ On attache le créateur à l'annonce
        return repo.save(annonce);
    }

    // ✅ Trouver une annonce par ID
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

        // Mise à jour des champs
        existing.setTitre(details.getTitre());
        existing.setDescription(details.getDescription());
        existing.setPrix(details.getPrix());
        existing.setVille(details.getVille());
        existing.setAdresse(details.getAdresse());

        // On ne remplace l'image que si une nouvelle a été fournie
        if (details.getImage() != null) {
            existing.setImage(details.getImage());
        }

        return repo.save(existing);
    }
}