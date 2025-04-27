package Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Address {
    private int address_id;
    private int user_id;
    private String address_line;
    private String city;
    private String state;
    private String postal_code;
    private String country;
    private LocalDate created_at;

    public Address() {
    }

    // Constructor without ID
    public Address(int user_id, String address_line, String city, String state,
                   String postal_code, String country, LocalDate created_at) {
        this.user_id = user_id;
        this.address_line = address_line;
        this.city = city;
        this.state = state;
        this.postal_code = postal_code;
        this.country = country;
        this.created_at = created_at;
    }

    // Full constructor
    public Address(int address_id, int user_id, String address_line, String city,
                   String state, String postal_code, String country, LocalDate created_at) {
        this.address_id = address_id;
        this.user_id = user_id;
        this.address_line = address_line;
        this.city = city;
        this.state = state;
        this.postal_code = postal_code;
        this.country = country;
        this.created_at = created_at;
    }

    public Address(Utilisateur user, String text, String text1, String text2, String text3, String text4) {
    }

    public Address(int userId, Object o) {
    }

    public Address(int userId, String trim, String trim1, String trim2, String trim3, String trim4) {
    }

    public Address(String trim, String trim1, String trim2, String trim3, String trim4, LocalDate now) {
    }

    public Address(int i, int id, String text, String text1, String text2, String text3, String text4, LocalDateTime now, Object o) {
    }

    // Getters & Setters
    public int getAddress_id() { return address_id; }
    public void setAddress_id(int address_id) { this.address_id = address_id; }

    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

    public String getAddress_line() { return address_line; }
    public void setAddress_line(String address_line) { this.address_line = address_line; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostal_code() { return postal_code; }
    public void setPostal_code(String postal_code) { this.postal_code = postal_code; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public LocalDate getCreated_at() { return created_at; }
    public void setCreated_at(LocalDate created_at) { this.created_at = created_at; }

    @Override
    public String toString() {
        return "Address{" +
                "address_id=" + address_id +
                ", user_id=" + user_id +
                ", address_line='" + address_line + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postal_code='" + postal_code + '\'' +
                ", country='" + country + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}