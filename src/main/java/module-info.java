module ahlem_C {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens Controllers to javafx.fxml;
    exports Controllers;

    opens Models to javafx.base;
    exports Models;

    opens Services to javafx.base;
    exports Services;

    opens Tests to javafx.base, javafx.fxml; // <-- Combined here!
    exports Tests;
}
