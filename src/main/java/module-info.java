module com.arlan.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.google.gson;

//    requires org.controlsfx.controls;
//    requires com.dlsc.formsfx;
//    requires org.kordamp.bootstrapfx.core;

    opens com.arlan.chess to javafx.fxml;
    exports com.arlan.chess;
}