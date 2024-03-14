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
    exports com.example.gestiunemagazin.controller;
    opens com.example.gestiunemagazin.controller to javafx.fxml;
}