package com.monnouveauprojet.monappli.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "annonces")
public class AnnonceImmobiliere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Le champ est présent, il nous faut juste ses accès plus bas
    private String statut = "EN_ATTENTE"; // Valeurs possibles : EN_ATTENTE, VALIDER, REJETER

    private String titre;
    private String description;
    private double prix;
    private String ville;
    private String adresse;
    private String image;

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

    // 🚀 AJOUT DES ACCÈS POUR LE STATUT (Pour corriger l'erreur de compilation)
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

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

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}