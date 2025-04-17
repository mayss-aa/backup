package Models;

public class Zone {
    private int id,plante_id;
    private int superficie;
    private String nom_zone;

    public Zone(){

    }

    public Zone(int plante_id, int superficie, String nom_zone) {
        this.plante_id = plante_id;
        this.superficie = superficie;
        this.nom_zone = nom_zone;
    }

    public Zone(int id, int plante_id,int superficie, String nom_zone) {
        this.id = id;
        this.superficie = superficie;
        this.plante_id = plante_id;
        this.nom_zone = nom_zone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlante_id() {
        return plante_id;
    }

    public void setPlante_id(int plante_id) {
        this.plante_id = plante_id;
    }

    public int getSuperficie() {
        return superficie;
    }

    public void setSuperficie(int superficie) {
        this.superficie = superficie;
    }

    public String getNom_zone() {
        return nom_zone;
    }

    public void setNom_zone(String nom_zone) {
        this.nom_zone = nom_zone;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", plante_id=" + plante_id +
                ", superficie=" + superficie +
                ", nom_zone='" + nom_zone + '\'' +
                '}';
    }
}
