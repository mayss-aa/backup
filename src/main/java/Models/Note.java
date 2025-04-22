package Models;


import java.time.LocalDateTime;

public class Note {
    private int id;
    private String titre;
    private String contenu;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private int utilisateur_id;

    public Note() {
    }

    public Note(String titre, String contenu, int utilisateur_id , LocalDateTime dateCreation, LocalDateTime dateModification) {
        this.titre = titre;
        this.contenu = contenu;
        this.utilisateur_id = utilisateur_id;
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    public Note(String titre, String contenu, int id) {
        this.titre = titre;
        this.contenu = contenu;
        this.utilisateur_id = id;

    }


    // Getters
    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getContenu() {
        return contenu;
    }

    public LocalDateTime getDateCreation() {
        return this.dateCreation != null ? this.dateCreation : LocalDateTime.now();
    }

    public LocalDateTime getDateModification() {
        return this.dateModification != null ? this.dateModification : getDateCreation();
    }

    public int getUtilisateur_id() {
        return utilisateur_id;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", titre=" + titre +
                ", contenu=" + contenu +
                ", dateCreation=" + dateCreation +
                ", dateModification=" + dateModification +
                ", utilisateur_id=" + utilisateur_id + '\'' +
                '}';
    }
}

