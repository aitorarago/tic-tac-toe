package com.example.tictactoe.controller;

import com.example.tictactoe.HelloApplication;
import com.example.tictactoe.marcador.Marcador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.example.tictactoe.controller.MarcadorController.marcadorlist;


public class Controller implements Initializable {
    String turn = "";
    String ganador;
    boolean winner = false;
    int IA = 0;
    boolean turnIA;
    String[][] playingTableString;
    @FXML
    GridPane playingTable;
    @FXML
    Button btnStart;
    @FXML
    Text playerTurnText;
    @FXML
    ToggleGroup gameModeGroup;
    @FXML
    SplitMenuButton estilocheck;
    @FXML
    MenuItem estiloclaro;
    @FXML
    MenuItem estilooscuro;
    @FXML
    MenuItem sinestilo;

    @FXML
    MenuBar barmenu;
    @FXML
    MenuItem marcadorbutton;
    @FXML
    MenuItem restaurarmarcador;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MarcadorController.leerMarcador();
    }

    /**
     * Metodo que hace que hasta que no se ha seleccionado el modo de juego no se puede inicializar la partida.
     */
    @FXML
    protected void onClickStartBtn() {
        //Reads the stats file to have it updated
        RadioButton gameMenuButton = (RadioButton) gameModeGroup.getSelectedToggle();
        IA = 0;
        turnIA = true;

        if (gameMenuButton == null) {
            playerTurnText.setText("Selecciona una opción para poder jugar.");
        }
        else {
            limpiarTable("first");
            playingTableString = new String[playingTable.getColumnCount()][playingTable.getRowCount()];
            turn = "X";
            btnStart.setDisable(true);
            playerTurnText.setText("Es el turno de "+turn+"!");
            playerTurnText.setStyle("-fx-fill: black;");
            switch (gameMenuButton.getId()) {
                case "compvscomp" -> {
                    IA = 2;
                    movimientoPC();
                }
                case "plavscomp" -> IA = 1;
            }
        }
    }

    /**
     * Cambia los estados de los botones del tablero y consulta si alguien ha ganado con el metodo checkwin()
     */
    @FXML
    protected void onClickCell(@NotNull ActionEvent event) {
        Button btn = (Button) event.getSource();
        btn.setText(turn);
        btn.setDisable(true);
        cargarTablero();
        ganador = comprobarGanador();
        if (ganador != null) {
            resultadoPopApp(ganador);
            playerTurnText.setText("El ganador es: "+ganador);
        } else cambiarJugador();
    }


    /**
     * Instrucciones menu superior.
     */
    @FXML
    protected void onClickInstrucciones() {
        Stage stage = new Stage();
        Label label = new Label("""
                Aconsegueix fer 3 en ratlla i guanya la partida,
                Pots jugar contra un altre amic (PLAYER VS PLAYER),
                Contra l'ordinador (PLAYER VS PC),
                Veure com jugan 2 Ordinadors (Pc VS PC).
                """);
        label.setTextAlignment(TextAlignment.CENTER);

        VBox vBox = new VBox(label);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10, 20, 20, 20));
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.setTitle("Tic-Tac-Toe");
        stage.show();
    }

    /**
     * Change the Scene into the Stats Grid.
     */
    @FXML
    protected void onClickMarcador() {
        try {
            HelloApplication.cambiarEscena("marcador-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onClickRestMarcador(){
        MarcadorController.onClickReeeset();
    }

    /**
     * Restarts the table to play another Game.
     * @param from String than defines if it is the first game.
     */
    protected void limpiarTable(String from) {
        for (Node node : playingTable.getChildren()) {
            Button btnCell = (Button) node;
            if (from.equals("first")) {
                btnCell.setDisable(false);
                btnCell.setText("");
            } else {
                btnCell.setDisable(true);
                btnStart.setDisable(false);
            }
        }
    }

    /**
     * Updates the Table every time anyone make a play, and shows the differences.
     */
    protected void cargarTablero() {
        List<Node> nodes = playingTable.getChildren();
        int pos = 0;
        for (int i = 0; i < playingTable.getColumnCount(); i++) {
            for (int j = 0; j < playingTable.getRowCount(); j++) {
                Button btnCell = (Button) nodes.get(pos);
                playingTableString[i][j] = btnCell.getText();
                pos++;
            }
        }
    }

    /**
     * Method to make the player change and detects if it is the IA or Player turn.
     */
    protected void cambiarJugador() {
        if (turn.equals("X")) turn = "O";
        else turn = "X";
        playerTurnText.setText("Es el turno de "+turn+"!");

        if (IA == 1) {
            if (turnIA) {
                movimientoPC();
            } else {
                turnIA = true;
            }
        } else if (IA == 2) {
            movimientoPC();
        }
    }

    /**
     * Makes an IA move. Selects Random Grid Cell and checks if it is possible to click on it.
     */
    protected void movimientoPC() {
        //Randomize the position
        List<Node> nodes = playingTable.getChildren();
        boolean clicked;
        int randomBtnClick;
        turnIA = false;

        do {
            clicked = false;
            randomBtnClick = (int) (Math.random() * 9);
            Button btnCell = (Button) nodes.get(randomBtnClick);
            if (!btnCell.isDisabled()) {
                btnCell.fire();
            } else {
                clicked = true;
            }

        } while (clicked);


    }

    /**
     * Checks if any option of winning happens in every signle move.
     *
     * @return Players ID
     */
    protected String comprobarGanador() {
        String player;
        //Ganador en horizontal
        for (int i = 0; i < playingTable.getColumnCount(); i++) {
            winner = true;
            player = playingTableString[i][0];

            if (!player.equals("")) {
                for (int j = 1; j < playingTable.getRowCount(); j++) {
                    if (!player.equals(playingTableString[i][j])) {
                        winner = false;
                    }
                }
                if (winner) {
                    return player;
                }
            }
        }

        //Ganador en vertical
        for (int j = 0; j < playingTable.getRowCount(); j++) {
            winner = true;
            player = playingTableString[0][j];

            if (!player.equals("")) {
                for (int i = 1; i < playingTable.getColumnCount(); i++) {
                    if (!player.equals(playingTableString[i][j])) {
                        winner = false;
                    }
                }
                if (winner) {
                    return player;
                }
            }
        }

        //Ganador en diagonal
        if ((Objects.equals(playingTableString[0][0], playingTableString[1][1]) && Objects.equals(playingTableString[1][1], playingTableString[2][2])) && !playingTableString[0][0].equals("")) {
            return playingTableString[0][0];
        } else if ((Objects.equals(playingTableString[0][2], playingTableString[1][1]) && Objects.equals(playingTableString[1][1], playingTableString[2][0])) && !playingTableString[0][2].equals("")) {
            return playingTableString[0][2];
        }

        int blanks = 0;
        for (String[] s : playingTableString) {
            if (!Arrays.stream(s).toList().contains("")) blanks++;
        }

        if (blanks == 3) return "noBlanks";
        return null;

    }

    /**
     * If anyone has won, this create the Stage to show and read diferents values
     * @param player Player who has won the match.
     */
    protected void resultadoPopApp(String player) {
        Insets indvPadding = new Insets(10, 10, 10, 10);
        Stage winnerStage = new Stage();

        //Image TicTacToe PNG
        Text winnerText = new Text();
        HBox hBox = new HBox(winnerText);
        hBox.setPadding(indvPadding);
        hBox.setSpacing(35);
        hBox.setAlignment(Pos.CENTER);

        //Player X Form
        Label playerXLabel = new Label("Player X Name:");
        TextField playerXName = new TextField();
        playerXName.setPrefHeight(10);
        playerXName.setPrefWidth(145);
        HBox hBox1 = new HBox(playerXLabel, playerXName);
        hBox1.setPadding(indvPadding);
        hBox1.setPrefHeight(30);
        hBox1.setSpacing(10);
        hBox1.setAlignment(Pos.CENTER);

        //Player O Form
        Label playerOLabel = new Label("Player O Name:");
        TextField playerOName = new TextField();
        playerOName.setPrefHeight(15);
        playerOName.setPrefWidth(145);
        HBox hBox2 = new HBox(playerOLabel, playerOName);
        hBox2.setPadding(indvPadding);
        hBox2.setSpacing(10);
        hBox2.setAlignment(Pos.CENTER);

        //Buttons to Send Form.
        Button submitBtn = new Button("Guardar");
        submitBtn.setPrefWidth(60);
        submitBtn.onActionProperty().set(e -> {
            if (player.equals("X")) {
                guardarPartidaMarcador(playerXName.getText().trim(), "win");
                guardarPartidaMarcador(playerOName.getText().trim(), "no");
            } else if (player.equals("O")) {
                guardarPartidaMarcador(playerXName.getText().trim(), "no");
                guardarPartidaMarcador(playerOName.getText().trim(), "win");
            } else {
                guardarPartidaMarcador(playerXName.getText().trim(), "no");
                guardarPartidaMarcador(playerOName.getText().trim(), "no");
            }
            winnerStage.close();
        });
        Button cancelBtn = new Button("NO GUARDAR");
        cancelBtn.onActionProperty().set(e -> winnerStage.close());
        cancelBtn.setPrefWidth(60);
        HBox buttonsBox = new HBox(submitBtn, cancelBtn);
        buttonsBox.setSpacing(10);
        buttonsBox.setPadding(indvPadding);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);

        if (IA >= 1) {
            playerOName.setDisable(true);
            if (IA == 2) {
                playerXName.setDisable(true);
                submitBtn.setDisable(true);
            }
        }

        VBox vBox = new VBox(hBox, hBox1, hBox2, buttonsBox);
        Scene scene = new Scene(vBox);
        winnerStage.setTitle("Match Finished!");
        winnerStage.setScene(scene);
        winnerStage.show();

        if (player.equals("noBlanks")) {
            winnerText.setText("NO GANA NADIE");
        } else {
            winnerText.setText("El jugador " + player + " ha GANADO!");
        }


        limpiarTable("none");
    }

    /**
     * Actualizar marcador.csv con el resultado de la partida
     * @param nombrejugador Player Code (X or O)
     * @param resultado Stats to Increment
     */
    @FXML
    protected static void guardarPartidaMarcador(String nombrejugador, String resultado) {
        if (!Objects.equals(nombrejugador, "")) {
            if (marcadorlist.get(nombrejugador) != null) {
                switch (resultado) {
                    case "win" -> marcadorlist.get(nombrejugador).sumarVictoria();
                    case "no" -> marcadorlist.get(nombrejugador).sumarPartidaJugada();
                }
            } else {
                switch (resultado) {
                    case "win" -> marcadorlist.put(nombrejugador, new Marcador(nombrejugador, 1, 1));
                    case "no" -> marcadorlist.put(nombrejugador, new Marcador(nombrejugador, 0, 1));
                }
            }
            ArrayList<String[]> list = new ArrayList<>();
            for (Marcador marcador : marcadorlist.values()) {
                String[] marcadorPlayer = new String[4];
                marcadorPlayer[0] = marcador.getNombre();
                marcadorPlayer[1] = marcador.getVictorias() + "";
                marcadorPlayer[2] = marcador.getPartidasjugadas() + "";
                list.add(marcadorPlayer);
            }
            try {
                Marcador.escribirMarcadorCSV(list, "marcador.csv");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onClickCambioEscena(ActionEvent actionEvent) {
        estiloclaro.setOnAction(e -> setClaro());
        estilooscuro.setOnAction(e -> setOscuro());
        sinestilo.setOnAction(e -> setSinestilo());
    }
    public void setClaro(){
        estilocheck.getScene().getStylesheets().clear();
        estilocheck.getScene().getStylesheets().add((HelloApplication.class.getResource("estiloclaro.css")).toExternalForm());
    }
    public void setOscuro(){
        estilocheck.getScene().getStylesheets().clear();
        estilocheck.getScene().getStylesheets().add((HelloApplication.class.getResource("estilooscuro.css")).toExternalForm());
    }
    public void setSinestilo(){
        estilocheck.getScene().getStylesheets().clear();
        estilocheck.getScene().getStylesheets().add((HelloApplication.class.getResource("sinestilo.css")).toExternalForm());
    }
}