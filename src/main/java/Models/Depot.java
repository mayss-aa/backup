package Models;

public class Depot {
    private int id;
    private String nom_depot;

    public Depot() {
    }

    private String localisation_depot;
    private int capacite_depot;
    private String unite_cap_depot;
    private String type_stockage_depot;
    private String statut_depot;


    public Depot(String nom_depot, String localisation_depot, int capacite_depot, String unite_cap_depot, String type_stockage_depot, String statut_depot) {
        this.nom_depot = nom_depot;
        this.localisation_depot = localisation_depot;
        this.capacite_depot = capacite_depot;
        this.unite_cap_depot = unite_cap_depot;
        this.type_stockage_depot = type_stockage_depot;
        this.statut_depot = statut_depot;
    }



    public Depot(int id, String nom_depot, String localisation_depot, int capacite_depot, String unite_cap_depot, String type_stockage_depot, String statut_depot) {
        this.id = id;
        this.nom_depot = nom_depot;
        this.localisation_depot = localisation_depot;
        this.capacite_depot = capacite_depot;
        this.unite_cap_depot = unite_cap_depot;
        this.type_stockage_depot = type_stockage_depot;
        this.statut_depot = statut_depot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_depot() {
        return nom_depot;
    }

    public void setNom_depot(String nom_depot) {
        this.nom_depot = nom_depot;
    }

    public String getLocalisation_depot() {
        return localisation_depot;
    }

    public void setLocalisation_depot(String localisation_depot) {
        this.localisation_depot = localisation_depot;
    }

    public int getCapacite_depot() {
        return capacite_depot;
    }

    public void setCapacite_depot(int capacite_depot) {
        this.capacite_depot = capacite_depot;
    }

    public String getUnite_cap_depot() {
        return unite_cap_depot;
    }

    public void setUnite_cap_depot(String unite_cap_depot) {
        this.unite_cap_depot = unite_cap_depot;
    }

    public String getType_stockage_depot() {
        return type_stockage_depot;
    }

    public void setType_stockage_depot(String type_stockage_depot) {
        this.type_stockage_depot = type_stockage_depot;
    }

    public String getStatut_depot() {
        return statut_depot;
    }

    @Override
    public String toString() {
        return "Depot{" +
                "id=" + id +
                ", nom_depot='" + nom_depot + '\'' +
                ", localisation_depot='" + localisation_depot + '\'' +
                ", capacite_depot=" + capacite_depot +
                ", unite_cap_depot='" + unite_cap_depot + '\'' +
                ", type_stockage_depot='" + type_stockage_depot + '\'' +
                ", statut_depot='" + statut_depot + '\'' +
                '}';
    }

    public void setStatut_depot(String statut_depot) {
        this.statut_depot = statut_depot;
    }
}
