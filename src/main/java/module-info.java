module table {
    requires javafx.controls;
    requires javafx.fxml;

    opens table to javafx.fxml;
    exports table;
}
