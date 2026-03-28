package com.monnouveauprojet.monappli.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "annonces")
public class AnnonceImmobiliere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private double prix;
    private String ville;
    private String adresse;

    // ✅ nouveau champ pour l'image
    private String image;

    // ✅ AJOUT DE LA RELATION AVEC L'UTILISATEUR
    // FetchType.LAZY est recommandé pour les performances
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructeurs
    public AnnonceImmobiliere() {}

    public AnnonceImmobiliere(String titre, String description, double prix, String ville) {
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.ville = ville;
    }

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    // ✅ NOUVEAU GETTER ET SETTER POUR L'UTILISATEUR
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}