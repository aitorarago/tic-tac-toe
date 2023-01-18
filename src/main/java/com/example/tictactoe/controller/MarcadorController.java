package com.example.tictactoe.controller;

import com.example.tictactoe.HelloApplication;
import com.example.tictactoe.marcador.Marcador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.layout.GridPane.setHalignment;

public class MarcadorController implements Initializable{
    @FXML
    GridPane statsGrid;
    @FXML
    Button continuar;
    protected static HashMap<String, Marcador> marcadorlist = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarFicheroMarcador();
    }

    /**
     * Gets the Stats information and sets the values into the Grid
     */
    @FXML
    public void cargarFicheroMarcador() {
        int rows = 1;
        for (Marcador marcador : marcadorlist.values()) {
            Label name = new Label(marcador.getNombre());
            Label wins = new Label(marcador.getVictorias() + "");
            Label jugadas = new Label(marcador.getPartidasjugadas() + "");

            statsGrid.add(name, 0, rows);
            statsGrid.add(wins, 1, rows);
            statsGrid.add(jugadas, 2, rows);

            setHalignment(name, HPos.CENTER);
            setHalignment(wins, HPos.CENTER);
            setHalignment(jugadas, HPos.CENTER);
            rows++;
        }
    }

    /**
     * Go back Button.
     */
    @FXML
    protected void onClickContinue() {
        try {
            HelloApplication.cambiarEscena("hello-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected static void onClickReeeset(){
        try{
            File file = new File("src/main/resources/com/example/tictactoe/marcador.csv");
            if(file.exists()) {
                Alert reset = new Alert(Alert.AlertType.CONFIRMATION);
                reset.setTitle("Marcador");
                reset.setContentText("Marcador reseteado correctamente");
                file.delete();
            }
            File file1 = new File("src/main/resources/com/example/tictactoe/marcador.csv");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads Stats from the file
     */
    protected static void leerMarcador() {
        List<String[]> marcadorr;
        marcadorr = Marcador.leerMarcadorCSV("src/main/resources/com/example/tictactoe/marcador.csv");
        marcadorr.forEach(campo -> marcadorlist.put(campo[0], new Marcador(campo[0], Integer.parseInt(campo[1]), Integer.parseInt(campo[2]))));
    }

    public void onClickReeeset(ActionEvent actionEvent) {
        onClickReeeset();
    }
}
