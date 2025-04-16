package Models;

public class Fournisseur {
    private int id;
    private String nomFournisseur;
    private String email;
    private String telephone;
    private String adresseFournisseur;

    public Fournisseur(int fournisseur) {}

    public Fournisseur(int id, String nomFournisseur, String email, String telephone, String adresseFournisseur) {
        this.id = id;
        this.nomFournisseur = nomFournisseur;
        this.email = email;
        this.telephone = telephone;
        this.adresseFournisseur = adresseFournisseur;
    }

    public Fournisseur(String nomFournisseur, String email, String telephone, String adresseFournisseur) {
        this.nomFournisseur = nomFournisseur;
        this.email = email;
        this.telephone = telephone;
        this.adresseFournisseur = adresseFournisseur;
    }

    public Fournisseur(String fournisseur) {
        this.nomFournisseur=fournisseur;
    }

    public int getId() {
        return id;
    }

    public String getNomFournisseur() {
        return nomFournisseur;
    }

    public void setNomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresseFournisseur() {
        return adresseFournisseur;
    }

    public void setAdresseFournisseur(String adresseFournisseur) {
        this.adresseFournisseur = adresseFournisseur;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nomFournisseur;
    }
}
