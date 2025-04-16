package com.esprit.Models;

public class Categorie {
    private String nomCategorie;
    private String descriptionCategorie;
    private String saisonDeRecolte;
    private String temperatureIdeale;

    public Categorie() {}

    public Categorie(String nomCategorie, String descriptionCategorie, String saisonDeRecolte, String temperatureIdeale) {
        this.nomCategorie = nomCategorie;
        this.descriptionCategorie = descriptionCategorie;
        this.saisonDeRecolte = saisonDeRecolte;
        this.temperatureIdeale = temperatureIdeale;
    }

    public Categorie(String categorie) {
        this.nomCategorie=categorie;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getDescriptionCategorie() {
        return descriptionCategorie;
    }

    public void setDescriptionCategorie(String descriptionCategorie) {
        this.descriptionCategorie = descriptionCategorie;
    }

    public String getSaisonDeRecolte() {
        return saisonDeRecolte;
    }

    public void setSaisonDeRecolte(String saisonDeRecolte) {
        this.saisonDeRecolte = saisonDeRecolte;
    }

    public String getTemperatureIdeale() {
        return temperatureIdeale;
    }

    public void setTemperatureIdeale(String temperatureIdeale) {
        this.temperatureIdeale = temperatureIdeale;
    }

    @Override
    public String toString() {
        return nomCategorie;
    }
}
