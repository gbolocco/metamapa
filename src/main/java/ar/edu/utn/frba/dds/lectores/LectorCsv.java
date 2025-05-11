package ar.edu.utn.frba.dds.lectores;

import ar.edu.utn.frba.dds.hecho.Hecho;
import ar.edu.utn.frba.dds.hecho.OriginHecho;
import ar.edu.utn.frba.dds.hecho.Ubicacion;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LectorCsv implements Lector {
  private static final LectorCsv instance = new LectorCsv();
  private LectorCsv() {}

  public static LectorCsv getInstancia() {
    return instance;
  }
  @Override
  public List<Hecho> leer(String rutaArchivo) {
    if (!rutaArchivo.toLowerCase().endsWith(".csv")) {
      throw new IllegalArgumentException("El archivo debe tener extensión .csv");
    }
    List<Hecho> hechos = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate fechaCarga = LocalDate.now();
    OriginHecho origen = OriginHecho.FUENTE;


    try (
        Reader fileReader = new InputStreamReader(
            new FileInputStream(rutaArchivo), StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fileReader)
    ) {
      reader.skip(1);
      String[] fila;
      while ((fila = reader.readNext()) != null) {
        try {
          if (fila.length >= 6) {
            String titulo = fila[0].trim();
            String descripcion = fila[1].trim();
            String categoria = fila[2].trim();
            double latitud = Double.parseDouble(fila[3].trim());
            double longitud = Double.parseDouble(fila[4].trim());
            LocalDate fechaHecho = LocalDate.parse(fila[5].trim(), formatter);

            Hecho hecho = new Hecho(
                titulo,
                descripcion,
                categoria,
                new Ubicacion(latitud, longitud),
                fechaHecho,
                fechaCarga,
                origen
            );

            hechos.add(hecho);
          } else {
            System.err.println("Fila ignorada - Campos insuficientes: " + String.join(",", fila));
          }
        } catch (Exception e) {
          System.err.println("Error al procesar fila: " + String.join(",", fila));
          e.printStackTrace();
        }
      }
    } catch (IOException | CsvException e) {
      e.printStackTrace();
      throw new RuntimeException("Error al leer el archivo CSV", e);
    }

    return hechos;
  }
}
