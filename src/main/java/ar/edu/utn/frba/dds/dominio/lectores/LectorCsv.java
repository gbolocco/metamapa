package ar.edu.utn.frba.dds.dominio.lectores;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import ar.edu.utn.frba.dds.dominio.hechos.CampoEsperado;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;



public class LectorCsv implements Lector {

  private static final Logger logger = AppLogger.getLogger(LectorCsv.class);

  private int errores = 0;

  public List<Hecho> leer(String rutaArchivo) {
    if (!rutaArchivo.toLowerCase().endsWith(".csv")) {
      throw new IllegalArgumentException("Solo se permiten archivos con extensión .csv");
    }
    List<Hecho> hechos = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate fechaCarga = LocalDate.now();
    OrigenHecho origen = OrigenHecho.FUENTE_ESTATICA;

    CSVReader lector = null;
    String[] encabezados = null;

    try {
      lector = new CSVReaderBuilder(
          new InputStreamReader(
              new FileInputStream(rutaArchivo),
              StandardCharsets.UTF_8
          ))
          .withCSVParser(new CSVParserBuilder()
              .withSeparator(';')
              .build())
          .build();

      encabezados = lector.readNext();


    } catch (Exception e) {
      logger.info("Error al intentar con separador '" + ";" + "': " + e.getMessage());
    }

    if (lector == null || encabezados == null) {
      throw new RuntimeException(
          "No se pudo leer el archivo: separador inválido o archivo mal formado."
      );
    }

    try {
      Map<CampoEsperado, Integer> indices = new HashMap<>();
      for (int i = 0; i < encabezados.length; i++) {
        CampoEsperado campo = CampoEsperado.buscarPorEncabezado(encabezados[i]);
        if (campo != null) {
          indices.put(campo, i);
        }
      }

      String[] fila;

      while ((fila = lector.readNext()) != null) {
        try {
          if (validarCamposObligatorios(fila, indices)) {
            throw new IllegalArgumentException("Faltan campos obligatorios");
          }

          String titulo = getCampo(fila, indices.get(CampoEsperado.TITULO));
          String descripcion = getCampo(fila, indices.get(CampoEsperado.DESCRIPCION));
          String categoria = getCampo(fila, indices.get(CampoEsperado.CATEGORIA));
          String latitud = getCampo(fila, indices.get(CampoEsperado.LATITUD));
          String longitud = getCampo(fila, indices.get(CampoEsperado.LONGITUD));
          String fecha = getCampo(fila, indices.get(CampoEsperado.FECHA_ACONTECIMIENTO));

          String latitudLimpia = latitud.trim().replace(",", ".");
          Double latitudDouble = Double.parseDouble(latitudLimpia);

          String longitudLimpia = longitud.trim().replace(",", ".");
          Double longitudDouble = Double.parseDouble(longitudLimpia);

          LocalDate fechaHecho = LocalDate.parse(fecha, formatter);

          Hecho hecho = new Hecho(
              titulo,
              descripcion,
              categoria,
              new Ubicacion(latitudDouble, longitudDouble),
              fechaHecho,
              fechaCarga,
              origen
          );
          hechos.add(hecho);

        } catch (IllegalArgumentException
                 | IndexOutOfBoundsException
                 | DateTimeParseException e) {
          errores++;
          logger.warn("Error procesando fila: {} - {}", Arrays.toString(fila), e.getMessage());
        }
      }
    } catch (IOException | CsvValidationException e) {
      logger.error("Error al procesar el archivo CSV: {}", e.getMessage(), e);
      throw new RuntimeException("Error en la lectura del archivo CSV", e);
    }

    logger.info("Cantidad de Errores {}", errores);
    for (Hecho hecho : hechos) {
      logger.info("{} {}", hecho.getTitulo(), hecho.getUbicacion());
    }
    return hechos;
  }

  private static String getCampo(String[] fila, Integer indice) {

    boolean noExisteCampo = indice == null || indice < 0 || indice >= fila.length;
    if (noExisteCampo) {
      return null;
    }
    return fila[indice].trim();
  }

  private static boolean validarCamposObligatorios(
      String[] fila,
      Map<CampoEsperado,
          Integer> indices
  ) {
    return Objects.requireNonNull(getCampo(fila, indices.get(CampoEsperado.TITULO))).isEmpty()
        || Objects.requireNonNull(getCampo(fila, indices.get(CampoEsperado.LATITUD))).isEmpty()
        || Objects.requireNonNull(getCampo(fila, indices.get(CampoEsperado.LONGITUD))).isEmpty();
  }
}

