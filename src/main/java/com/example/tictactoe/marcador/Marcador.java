package com.example.tictactoe.marcador;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Marcador {

    private String nombre;
    private int ganados;
    private int jugados;

    public Marcador(String nombre, int ganados, int jugados) {
        this.nombre = nombre;
        this.ganados = ganados;
        this.jugados = jugados;
    }

    public Marcador() {
    }



    public String getNombre() {
        return nombre;
    }

    public int getVictorias() {
        return ganados;
    }

    public int getPartidasjugadas() {
        return jugados;
    }

    public void sumarVictoria() {
        this.ganados++;
        this.jugados++;
    }

    public void sumarPartidaJugada() {
        this.jugados++;
    }

    public static List<String[]> leerMarcadorCSV(String ruta) {
        List<String[]> marcador = new ArrayList<>();
        BufferedReader bufferLectura = null;
        try {
            bufferLectura = new BufferedReader(new FileReader(ruta));
            String linea = bufferLectura.readLine();

            while (linea != null) {
                String[] campos = linea.split(",");
                marcador.add(campos);
                linea = bufferLectura.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cierro el buffer de lectura
            if (bufferLectura != null) {
                try {
                    bufferLectura.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    return marcador;}
    public static void escribirMarcadorCSV(ArrayList<String[]> list, String s) throws IOException {
        File file = new File(s);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.newLine();
        for (String[] strings : list) {
            List<String> p = Arrays.stream(strings).toList();
            bw.write(p.get(0) + "," + p.get(1) + "," + p.get(2) + "\n");
        }
        bw.close();
        fileWriter.close();
    }

}
