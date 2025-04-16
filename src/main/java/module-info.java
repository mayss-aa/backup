module ahlem_C {
    requires javafx.controls;
    requires javafx.fxml;
requires java.sql;
    opens com.esprit.Controllers.back to javafx.fxml;
    exports com.esprit.Controllers.back;

    // si tu as d'autres packages :
    opens com.esprit.Models to javafx.base;
    exports com.esprit.Models;

    opens com.esprit.Services to javafx.base;
    exports com.esprit.Services;
    opens com.esprit.Tests to javafx.base;
    exports com.esprit.Tests;
}
