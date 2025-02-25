module net.chess.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.desktop;
    requires org.jetbrains.annotations;


    opens net.chess.chess to javafx.fxml;
    exports net.chess.chess;
}