module table {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens table to javafx.fxml;
    exports table;
}