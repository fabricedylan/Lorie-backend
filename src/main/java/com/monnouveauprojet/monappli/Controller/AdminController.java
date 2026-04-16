package com.monnouveauprojet.monappli.Controller;


import com.monnouveauprojet.monappli.Model.AnnonceImmobiliere;
import com.monnouveauprojet.monappli.Service.AnnonceImmobiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AnnonceImmobiliereService service;

    @Autowired
    public AdminController(AnnonceImmobiliereService service) {
        this.service = service;
    }

    // ✅ 1. VALIDER UNE ANNONCE
    @PutMapping("/annonces/{id}/valider")
    public ResponseEntity<AnnonceImmobiliere> validerAnnonce(@PathVariable Long id) {
        AnnonceImmobiliere annonce = service.trouverParId(id);
        if (annonce == null) return ResponseEntity.notFound().build();

        annonce.setStatut("VALIDER");
        return ResponseEntity.ok(service.updateStatut(id, "VALIDER"));
    }

    // ✅ 2. REJETER UNE ANNONCE (Optionnel mais pro)
    @PutMapping("/annonces/{id}/rejeter")
    public ResponseEntity<AnnonceImmobiliere> rejeterAnnonce(@PathVariable Long id) {
        return ResponseEntity.ok(service.updateStatut(id, "REJETER"));
    }

    // ✅ 3. VOIR TOUTES LES ANNONCES EN ATTENTE
    @GetMapping("/annonces/en-attente")
    public List<AnnonceImmobiliere> getAnnoncesEnAttente() {
        // Il faudra ajouter cette méthode dans ton service
        return service.listerAnnoncesParStatut("EN_ATTENTE");
    }

    // ✅ 4. GERER LES UTILISATEURS (Exemple simple)
    @GetMapping("/utilisateurs")
    public ResponseEntity<List<?>> listerTousLesUtilisateurs() {
        // Appelle ton service utilisateur pour l'admin
        return ResponseEntity.ok().build();
    }
}
