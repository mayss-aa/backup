package Models;

public class Role {
    private int id;
    private String nomRole;
    private String description;

    public Role() {
    }

    // Constructor without ID
    public Role(String nomRole, String description) {
        this.nomRole = nomRole;
        this.description = description;
    }

    // Full constructor
    public Role(int id, String nomRole, String description) {
        this.id = id;
        this.nomRole = nomRole;
        this.description = description;
    }

    // Getters & Setters
    public int getrId() {
        return id;
    }

    public void setrId(int id) {
        this.id = id;
    }

    public String getNomRole() {
        return nomRole;
    }

    public void setNomRole(String nomRole) {
        this.nomRole = nomRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", nomRole='" + nomRole + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}