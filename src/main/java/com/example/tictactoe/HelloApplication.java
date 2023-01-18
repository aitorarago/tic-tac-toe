package com.example.tictactoe;

import com.example.tictactoe.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    public static Stage pantallaprincipal;
    /*public static void reemplazarPantalla(String s) throws IOException {
        Parent page = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource(s)), null, new JavaFXBuilderFactory());
        Scene scene = HelloApplication.pantallaprincipal.getScene();
        if (scene == null) {
            System.out.println("SCENE");
            scene = new Scene(page);
            HelloApplication.pantallaprincipal.setScene(scene);
        } else {
            HelloApplication.pantallaprincipal.getScene().setRoot(page);
        }
        HelloApplication.pantallaprincipal.sizeToScene();
    }*/

    @Override
    public void start(Stage stage) throws IOException {
        pantallaprincipal=stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Controller controller = fxmlLoader.getController();
        stage.setScene(scene);
        stage.show();
    }

    public static void cambiarEscena(String escena) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(escena));
        Scene scene = new Scene(fxmlLoader.load());
        pantallaprincipal.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}