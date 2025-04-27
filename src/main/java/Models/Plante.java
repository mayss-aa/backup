package Models;

import java.util.Date;

public class Plante {
    private int id,rendement_estime;
    private String nom_plante;
    private Date date_plantation;

    public Plante() {}

    public Plante(int id, int rendement_estime, String nom_plante, Date date_plantation) {
        this.id = id;
        this.rendement_estime = rendement_estime;
        this.nom_plante = nom_plante;
        this.date_plantation = date_plantation;
    }

    public Plante(int rendement_estime, String nom_plante, Date date_plantation) {
        this.rendement_estime = rendement_estime;
        this.nom_plante = nom_plante;
        this.date_plantation = date_plantation;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRendement_estime() {
        return rendement_estime;
    }

    public void setRendement_estime(int rendement_estime) {
        this.rendement_estime = rendement_estime;
    }

    public String getNom_plante() {
        return nom_plante;
    }

    public void setNom_plante(String nom_plante) {
        this.nom_plante = nom_plante;
    }

    public Date getDate_plantation() {
        return date_plantation;
    }

    public void setDate_plantation(Date date_plantation) {
        this.date_plantation = date_plantation;
    }

    @Override
    public String toString() {
        return "Plante{" +
                "id=" + id +
                ", rendement_estime=" + rendement_estime +
                ", nom_plante='" + nom_plante + '\'' +
                ", date_plantation=" + date_plantation +
                '}';
    }
}
