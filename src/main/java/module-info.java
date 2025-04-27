module ahlem_C {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.httpserver;
    requires java.datatransfer;

    // QR Code
    requires com.google.zxing;
    requires com.google.zxing.javase;

    // PDF
    requires com.github.librepdf.openpdf;
    requires java.desktop;

    opens Controllers to javafx.fxml;
    exports Controllers;

    opens Models to javafx.base;
    exports Models;

    opens Services to javafx.base;
    exports Services;

    opens Tests to javafx.base, javafx.fxml;
    exports Tests;

    opens Utils to javafx.base;
    exports Utils;
}
