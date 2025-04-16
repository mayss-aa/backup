package Models;

import java.util.Date;

public class Ressource {
    private int id;
    private int depot_id;
    private String nom_ressource;
    private String type_ressource;
    private int quantite_ressource;
    private String unite_mesure;
    private Date date_ajout_ressource;
    private Date date_expiration_ressource;
    private String statut_ressource;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepot_id() {
        return depot_id;
    }

    public void setDepot_id(int depot_id) {
        this.depot_id = depot_id;
    }

    public String getNom_ressource() {
        return nom_ressource;
    }

    public void setNom_ressource(String nom_ressource) {
        this.nom_ressource = nom_ressource;
    }

    public String getType_ressource() {
        return type_ressource;
    }

    public void setType_ressource(String type_ressource) {
        this.type_ressource = type_ressource;
    }

    public int getQuantite_ressource() {
        return quantite_ressource;
    }

    public void setQuantite_ressource(int quantite_ressource) {
        this.quantite_ressource = quantite_ressource;
    }

    public String getUnite_mesure() {
        return unite_mesure;
    }

    public void setUnite_mesure(String unite_mesure) {
        this.unite_mesure = unite_mesure;
    }

    public Date getDate_ajout_ressource() {
        return date_ajout_ressource;
    }

    public void setDate_ajout_ressource(Date date_ajout_ressource) {
        this.date_ajout_ressource = date_ajout_ressource;
    }

    public Date getDate_expiration_ressource() {
        return date_expiration_ressource;
    }

    public void setDate_expiration_ressource(Date date_expiration_ressource) {
        this.date_expiration_ressource = date_expiration_ressource;
    }

    public String getStatut_ressource() {
        return statut_ressource;
    }

    public void setStatut_ressource(String statut_ressource) {
        this.statut_ressource = statut_ressource;
    }

    public Ressource(int depot_id, String nom_ressource, String type_ressource, int quantite_ressource, String unite_mesure, Date date_ajout_ressource, Date date_expiration_ressource, String statut_ressource) {
        this.depot_id = depot_id;
        this.nom_ressource = nom_ressource;
        this.type_ressource = type_ressource;
        this.quantite_ressource = quantite_ressource;
        this.unite_mesure = unite_mesure;
        this.date_ajout_ressource = date_ajout_ressource;
        this.date_expiration_ressource = date_expiration_ressource;
        this.statut_ressource = statut_ressource;
    }

    public Ressource(int id, int depot_id, String nom_ressource, String type_ressource, int quantite_ressource, String unite_mesure, Date date_ajout_ressource, Date date_expiration_ressource, String statut_ressource) {
        this.id = id;
        this.depot_id = depot_id;
        this.nom_ressource = nom_ressource;
        this.type_ressource = type_ressource;
        this.quantite_ressource = quantite_ressource;
        this.unite_mesure = unite_mesure;
        this.date_ajout_ressource = date_ajout_ressource;
        this.date_expiration_ressource = date_expiration_ressource;
        this.statut_ressource = statut_ressource;
    }

    public Ressource() {
    }

    @Override
    public String toString() {
        return "Ressource{" +
                "id=" + id +
                ", depot_id=" + depot_id +
                ", nom_ressource='" + nom_ressource + '\'' +
                ", type_ressource='" + type_ressource + '\'' +
                ", quantite_ressource=" + quantite_ressource +
                ", unite_mesure='" + unite_mesure + '\'' +
                ", date_ajout_ressource=" + date_ajout_ressource +
                ", date_expiration_ressource=" + date_expiration_ressource +
                ", statut_ressource='" + statut_ressource + '\'' +
                '}';
    }
}
