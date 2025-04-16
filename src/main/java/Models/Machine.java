package Models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Machine {

    private final IntegerProperty id;
    private final StringProperty nomMachine;
    private final StringProperty etatMachine;
    private final StringProperty brandMachine;
    private final ObjectProperty<LocalDate> dateAchat;

    public Machine(int id, String nomMachine, String etatMachine, String brandMachine, LocalDate dateAchat) {
        this.id = new SimpleIntegerProperty(id);
        this.nomMachine = new SimpleStringProperty(nomMachine);
        this.etatMachine = new SimpleStringProperty(etatMachine);
        this.brandMachine = new SimpleStringProperty(brandMachine);
        this.dateAchat = new SimpleObjectProperty<>(dateAchat);
    }
    

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nomMachineProperty() {
        return nomMachine;
    }

    public StringProperty etatMachineProperty() {
        return etatMachine;
    }

    public StringProperty brandMachineProperty() {
        return brandMachine;
    }

    public ObjectProperty<LocalDate> dateAchatProperty() {
        return dateAchat;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNomMachine() {
        return nomMachine.get();
    }

    public void setNomMachine(String nomMachine) {
        this.nomMachine.set(nomMachine);
    }

    public String getEtatMachine() {
        return etatMachine.get();
    }

    public void setEtatMachine(String etatMachine) {
        this.etatMachine.set(etatMachine);
    }

    public String getBrandMachine() {
        return brandMachine.get();
    }

    public void setBrandMachine(String brandMachine) {
        this.brandMachine.set(brandMachine);
    }

    public LocalDate getDateAchat() {
        return dateAchat.get();
    }

    public void setDateAchat(LocalDate dateAchat) {
        this.dateAchat.set(dateAchat);
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id.get() +
                ", nomMachine='" + nomMachine.get() + '\'' +
                ", etatMachine='" + etatMachine.get() + '\'' +
                ", brandMachine='" + brandMachine.get() + '\'' +
                ", dateAchat=" + dateAchat.get() +
                '}';
    }
}
