package Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Utilisateur {
    private int id;
    private int role_id;
    private String prenom;
    private String nom;
    private String email;
    private String genre;
    private LocalDate date_naissance;
    private String num_tel;
    private String password;
    private LocalDateTime cree_le;
    private LocalDateTime mis_ajour_le;
    private String token;
    private String photo;
    private String reset_code;
    private LocalDateTime reset_code_expires_at;

    public Utilisateur() {
    }

    // Simplified constructor without auto-generated fields
    public Utilisateur(int role_id, String prenom, String nom, String email,
                       String genre, LocalDate date_naissance, String num_tel,
                       String password) {
        this.role_id = role_id;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.genre = genre;
        this.date_naissance = date_naissance;
        this.num_tel = num_tel;
        this.password = password;
    }

    // Full constructor
    public Utilisateur(int id, int role_id, String prenom, String nom, String email,
                       String genre, LocalDate date_naissance, String num_tel,
                       String password, LocalDateTime cree_le, LocalDateTime mis_ajour_le,
                       String token, String photo, String reset_code, LocalDateTime reset_code_expires_at) {
        this.id = id;
        this.role_id = role_id;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.genre = genre;
        this.date_naissance = date_naissance;
        this.num_tel = num_tel;
        this.password = password;
        this.cree_le = cree_le;
        this.mis_ajour_le = mis_ajour_le;
        this.token = token;
        this.photo = photo;
        this.reset_code = reset_code;
        this.reset_code_expires_at = reset_code_expires_at;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRole_id() { return role_id; }
    public void setRole_id(int role_id) { this.role_id = role_id; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public LocalDate getDate_naissance() { return date_naissance; }
    public void setDate_naissance(LocalDate date_naissance) { this.date_naissance = date_naissance; }

    public String getNum_tel() { return num_tel; }
    public void setNum_tel(String num_tel) { this.num_tel = num_tel; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getCree_le() { return cree_le; }
    public void setCree_le(LocalDateTime cree_le) { this.cree_le = cree_le; }

    public LocalDateTime getMis_ajour_le() { return mis_ajour_le; }
    public void setMis_ajour_le(LocalDateTime mis_ajour_le) { this.mis_ajour_le = mis_ajour_le; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getReset_code() { return reset_code; }
    public void setReset_code(String reset_code) { this.reset_code = reset_code; }

    public LocalDateTime getReset_code_expires_at() { return reset_code_expires_at; }
    public void setReset_code_expires_at(LocalDateTime reset_code_expires_at) {
        this.reset_code_expires_at = reset_code_expires_at;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", role_id=" + role_id +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", genre='" + genre + '\'' +
                ", date_naissance=" + date_naissance +
                ", num_tel='" + num_tel + '\'' +
                ", password='" + password + '\'' +
                ", cree_le=" + cree_le +
                ", mis_ajour_le=" + mis_ajour_le +
                ", token='" + token + '\'' +
                ", photo='" + photo + '\'' +
                ", reset_code='" + reset_code + '\'' +
                ", reset_code_expires_at=" + reset_code_expires_at +
                '}';
    }
}