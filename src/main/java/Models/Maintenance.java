package Models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Maintenance {

    private final IntegerProperty id;
    private final ObjectProperty<LocalDate> dateMaintenance;
    private final StringProperty description;
    private final DoubleProperty coutMaintenance;
    private final ObjectProperty<Machine> machine;  // Utilisation de ObjectProperty pour la machine

    // Constructeur sans paramètres
    public Maintenance() {
        this.id = new SimpleIntegerProperty();
        this.dateMaintenance = new SimpleObjectProperty<>();
        this.description = new SimpleStringProperty();
        this.coutMaintenance = new SimpleDoubleProperty();
        this.machine = new SimpleObjectProperty<>();
    }

    // Constructeur avec paramètres
    public Maintenance(int id, LocalDate dateMaintenance, String description, double coutMaintenance, Machine machine) {
        this();
        setId(id);
        setDateMaintenance(dateMaintenance);
        setDescription(description);
        setCoutMaintenance(coutMaintenance);
        setMachine(machine);
    }

    // Getters et Setters

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public LocalDate getDateMaintenance() {
        return dateMaintenance.get();
    }

    public void setDateMaintenance(LocalDate dateMaintenance) {
        this.dateMaintenance.set(dateMaintenance);
    }

    public ObjectProperty<LocalDate> dateMaintenanceProperty() {
        return dateMaintenance;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public double getCoutMaintenance() {
        return coutMaintenance.get();
    }

    public void setCoutMaintenance(double coutMaintenance) {
        this.coutMaintenance.set(coutMaintenance);
    }

    public DoubleProperty coutMaintenanceProperty() {
        return coutMaintenance;
    }

    public Machine getMachine() {
        return machine.get();
    }

    public void setMachine(Machine machine) {
        this.machine.set(machine);
    }

    public ObjectProperty<Machine> machineProperty() {
        return machine;
    }

    @Override
    public String toString() {
        return "Maintenance{" +
                "id=" + id.get() +
                ", dateMaintenance=" + dateMaintenance.get() +
                ", description='" + description.get() + '\'' +
                ", coutMaintenance=" + coutMaintenance.get() +
                ", machine=" + machine.get() +
                '}';
    }
}
