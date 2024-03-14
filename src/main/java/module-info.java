module com.example.gestiunemagazin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires jbcrypt;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.gestiunemagazin to javafx.fxml;
    exports com.example.gestiunemagazin;
    opens com.example.gestiunemagazin.entity to javafx.base;
}