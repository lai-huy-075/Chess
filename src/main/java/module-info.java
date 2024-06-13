module net.chess.chess {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.logging;

    opens net.chess.chess to javafx.fxml;
    exports net.chess.chess;
}