package com.monnouveauprojet.monappli.Controller;

import com.monnouveauprojet.monappli.Model.AnnonceImmobiliere;
import com.monnouveauprojet.monappli.Service.AnnonceImmobiliereService;
import com.monnouveauprojet.monappli.config.UserPrincipal; // ✅ Vérifie l'import de ton UserPrincipal
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // ✅ Import indispensable
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceImmobiliereController {

    private final AnnonceImmobiliereService service;

    @Autowired
    public AnnonceImmobiliereController(AnnonceImmobiliereService service) {
        this.service = service;
    }

    // ----------------- CREER UNE ANNONCE -----------------
    @PostMapping
    public ResponseEntity<AnnonceImmobiliere> creerAnnonce(
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("prix") double prix,
            @RequestParam("ville") String ville,
            @RequestParam(value = "adresse", required = false) String adresse,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Authentication authentication // ✅ Injection de l'authentification
    ) {
        try {
            // 1. Récupérer l'ID de l'utilisateur connecté
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Long userId = principal.getId();

            String imageName = null;
            if (image != null && !image.isEmpty()) {
                String uploadDir = "uploads/";
                imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + imageName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, image.getBytes());
            }

            AnnonceImmobiliere annonce = new AnnonceImmobiliere();
            annonce.setTitre(titre);
            annonce.setDescription(description);
            annonce.setPrix(prix);
            annonce.setVille(ville);
            annonce.setAdresse(adresse);
            annonce.setImage(imageName);

            // ✅ On passe maintenant l'annonce ET le userId au service
            AnnonceImmobiliere saved = service.creerAnnonce(annonce, userId);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // ----------------- LISTER TOUTES LES ANNONCES -----------------
    @GetMapping
    public List<AnnonceImmobiliere> afficherToutesLesAnnonces() {
        return service.listerToutesLesAnnonces();
    }

    // ----------------- RÉCUPÉRER UNE ANNONCE PAR ID -----------------
    @GetMapping("/{id}")
    public ResponseEntity<AnnonceImmobiliere> getAnnonceById(@PathVariable Long id) {
        AnnonceImmobiliere annonce = service.trouverParId(id);
        if (annonce != null) {
            return ResponseEntity.ok(annonce);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ----------------- RÉCUPÉRER L'IMAGE D'UNE ANNONCE -----------------
    @GetMapping("/image/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            byte[] imageBytes = Files.readAllBytes(imagePath);

            String ext = imageName.substring(imageName.lastIndexOf('.') + 1).toLowerCase();
            MediaType mediaType = switch (ext) {
                case "png" -> MediaType.IMAGE_PNG;
                case "gif" -> MediaType.IMAGE_GIF;
                default -> MediaType.IMAGE_JPEG;
            };

            return ResponseEntity.ok().contentType(mediaType).body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ----------------- METTRE À JOUR UNE ANNONCE -----------------
    @PutMapping("/update/{id}")
    public ResponseEntity<AnnonceImmobiliere> updateAnnonce(
            @PathVariable Long id,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("prix") double prix,
            @RequestParam("ville") String ville,
            @RequestParam("adresse") String adresse,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            AnnonceImmobiliere details = new AnnonceImmobiliere();
            details.setTitre(titre);
            details.setDescription(description);
            details.setPrix(prix);
            details.setVille(ville);
            details.setAdresse(adresse);

            if (image != null && !image.isEmpty()) {
                String uploadDir = "uploads/";
                String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + imageName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, image.getBytes());
                details.setImage(imageName);
            }

            AnnonceImmobiliere updated = service.updateAnnonce(id, details);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // ----------------- SUPPRIMER UNE ANNONCE -----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerAnnonce(@PathVariable Long id) {
        AnnonceImmobiliere annonce = service.trouverParId(id);
        if (annonce == null) return ResponseEntity.notFound().build();

        try {
            if (annonce.getImage() != null) {
                Path imagePath = Paths.get("uploads/" + annonce.getImage());
                Files.deleteIfExists(imagePath);
            }
        } catch (Exception ignored) {}

        service.supprimerAnnonce(id);
        return ResponseEntity.ok("Annonce supprimée avec succès !");
    }
}