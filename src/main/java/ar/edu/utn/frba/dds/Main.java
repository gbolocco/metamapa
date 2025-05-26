package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.preprocesadores.PreProcesadorCSV;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    String rutaEntrada = "datos/desastres_naturales.csv";

    PreProcesadorCSV procesador = new PreProcesadorCSV();
    try {
      File archivoProcesado = procesador.preprocesar(rutaEntrada);
      System.out.println("El archivo fue procesado"); //cambiar por logger
      System.out.println("Ruta de salida: " + archivoProcesado.getAbsolutePath());
    } catch (IOException | CsvException e) {
      System.err.println("Error al procesar el CSV:");
      e.printStackTrace();
    }
  }
}