module table {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.persistence;
    requires transitive org.hibernate.orm.core;
    requires java.sql;

    // requires org.xerial.sqlitejdbc; 
    requires org.xerial.sqlitejdbc;

    opens table to javafx.fxml;
    opens table.bd.models to org.hibernate.orm.core;

    exports table;
}
