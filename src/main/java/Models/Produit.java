package com.esprit.Models;
import java.time.LocalDate;

public class Produit {
    private int id;
    private String nomProduit;
    private String cycleCulture;
    private int quantiteProduit;
    private int quantiteVendue;
    private String uniteQuantProd;
    private LocalDate dateSemisProd;
    private LocalDate dateRecolteProd;
    private LocalDate creeLeProd;
    private LocalDate misAJourLeProd;
    private String categorie;
    private int fournisseur;
    // Constructeurs, getters et setters
    public Produit() {
    }

    public Produit(String nomProduit, String cycleCulture, int quantiteProduit, int quantiteVendue, String uniteQuantProd, LocalDate dateSemisProd, LocalDate dateRecolteProd, LocalDate creeLeProd, LocalDate misAJourLeProd, String categorie, int fournisseur) {
        this.nomProduit = nomProduit;
        this.cycleCulture = cycleCulture;
        this.quantiteProduit = quantiteProduit;
        this.quantiteVendue = quantiteVendue;
        this.uniteQuantProd = uniteQuantProd;
        this.dateSemisProd = dateSemisProd;
        this.dateRecolteProd = dateRecolteProd;
        this.creeLeProd = creeLeProd;
        this.misAJourLeProd = misAJourLeProd;
        this.categorie = categorie;
        this.fournisseur = fournisseur;
    }

    public Produit(int id, String nomProduit, String cycleCulture, int quantiteProduit, int quantiteVendue, String uniteQuantProd, LocalDate dateSemisProd, LocalDate dateRecolteProd, LocalDate creeLeProd, LocalDate misAJourLeProd, String categorie, int fournisseur) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.cycleCulture = cycleCulture;
        this.quantiteProduit = quantiteProduit;
        this.quantiteVendue = quantiteVendue;
        this.uniteQuantProd = uniteQuantProd;
        this.dateSemisProd = dateSemisProd;
        this.dateRecolteProd = dateRecolteProd;
        this.creeLeProd = creeLeProd;
        this.misAJourLeProd = misAJourLeProd;
        this.categorie = categorie;
        this.fournisseur = fournisseur;
    }

    public int getId() {
        return id;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public String getCycleCulture() {
        return cycleCulture;
    }

    public int getQuantiteProduit() {
        return quantiteProduit;
    }

    public int getQuantiteVendue() {
        return quantiteVendue;
    }

    public String getUniteQuantProd() {
        return uniteQuantProd;
    }

    public LocalDate getDateSemisProd() {
        return dateSemisProd;
    }

    public LocalDate getDateRecolteProd() {
        return dateRecolteProd;
    }

    public LocalDate getCreeLeProd() {
        return creeLeProd;
    }

    public LocalDate getMisAJourLeProd() {
        return misAJourLeProd;
    }

    public String getCategorie() {
        return categorie;
    }

    public int getFournisseur() {
        return fournisseur;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public void setCycleCulture(String cycleCulture) {
        this.cycleCulture = cycleCulture;
    }

    public void setQuantiteProduit(int quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
    }

    public void setQuantiteVendue(int quantiteVendue) {
        this.quantiteVendue = quantiteVendue;
    }

    public void setUniteQuantProd(String uniteQuantProd) {
        this.uniteQuantProd = uniteQuantProd;
    }

    public void setDateSemisProd(LocalDate dateSemisProd) {
        this.dateSemisProd = dateSemisProd;
    }

    public void setDateRecolteProd(LocalDate dateRecolteProd) {
        this.dateRecolteProd = dateRecolteProd;
    }

    public void setCreeLeProd(LocalDate creeLeProd) {
        this.creeLeProd = creeLeProd;
    }

    public void setMisAJourLeProd(LocalDate misAJourLeProd) {
        this.misAJourLeProd = misAJourLeProd;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setFournisseur(int fournisseur) {
        this.fournisseur = fournisseur;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nomProduit='" + nomProduit + '\'' +
                ", cycleCulture='" + cycleCulture + '\'' +
                ", quantiteProduit=" + quantiteProduit +
                ", quantiteVendue=" + quantiteVendue +
                ", uniteQuantProd='" + uniteQuantProd + '\'' +
                ", dateSemisProd=" + dateSemisProd +
                ", dateRecolteProd=" + dateRecolteProd +
                ", creeLeProd=" + creeLeProd +
                ", misAJourLeProd=" + misAJourLeProd +
                ", categorie='" + categorie + '\'' +
                ", fournisseur='" + fournisseur + '\'' +
                '}';
    }
}
