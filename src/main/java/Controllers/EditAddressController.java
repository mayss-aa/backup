package Controllers;

import Models.Address;
import Services.AddressService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class EditAddressController {
    @FXML private TextField addressLineField;
    @FXML private TextField cityField;
    @FXML private TextField stateField;
    @FXML private TextField postalCodeField;
    @FXML private TextField countryField;

    private Address currentAddress;
    private final AddressService addressService = new AddressService(); // Add service

    public void setAddressData(Address address) {
        this.currentAddress = address;
        // Initialize fields with address data
        addressLineField.setText(address.getAddress_line());
        cityField.setText(address.getCity());
        stateField.setText(address.getState());
        postalCodeField.setText(address.getPostal_code());
        countryField.setText(address.getCountry());
    }

    @FXML
    private void handleSave() {
        try {
            // Update the current address object
            currentAddress.setAddress_line(addressLineField.getText());
            currentAddress.setCity(cityField.getText());
            currentAddress.setState(stateField.getText());
            currentAddress.setPostal_code(postalCodeField.getText());
            currentAddress.setCountry(countryField.getText());

            // Save changes to the database
            addressService.updateAddress(currentAddress); // Critical: Update database

            closeWindow();
        } catch (SQLException e) {
            showAlert("Error", "Failed to update address: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        postalCodeField.getScene().getWindow().hide();
    }

    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}