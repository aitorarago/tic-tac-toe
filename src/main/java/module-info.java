module com.example.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;

    exports com.example.tictactoe.controller to javafx.fxml;
    exports com.example.tictactoe;
    opens com.example.tictactoe to javafx.fxml;
    opens com.example.tictactoe.controller to javafx.fxml;

}