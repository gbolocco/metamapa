package ar.edu.utn.frba.dds.dominio.lectores;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OriginHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ar.edu.utn.frba.dds.dominio.hechos.CampoEsperado;
import java.util.Objects;

public class LectorCsv implements Lector {
  private int errores = 0;
  public List<Hecho> leer(String rutaArchivo) {
    if (!rutaArchivo.toLowerCase().endsWith(".csv")) {
      throw new IllegalArgumentException("Solo se permiten archivos con extensión .csv");
    }
    List<Hecho> hechos = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate fechaCarga = LocalDate.now();
    OriginHecho origen = OriginHecho.FUENTE;

    CSVReader lector = null;
    String[] encabezados = null;

    try {
      lector = new CSVReaderBuilder(new FileReader(rutaArchivo))
          .withCSVParser(new CSVParserBuilder()
              .withSeparator(';')
              .build())
          .build();

      encabezados = lector.readNext();


    } catch (Exception e) {
      System.out.println("Error al intentar con separador '" + ";" + "': " + e.getMessage());
    }

    if (lector == null || encabezados == null) {
      throw new RuntimeException("No se pudo leer el archivo: separador inválido o archivo mal formado.");
    }

    try {
      Map<CampoEsperado, Integer> indices = new HashMap<>();
      for (int posicionDeCampo = 0; posicionDeCampo < encabezados.length; posicionDeCampo++) {
        CampoEsperado campo = CampoEsperado.buscarPorEncabezado(encabezados[posicionDeCampo]);
        if (campo != null) {
          indices.put(campo, posicionDeCampo);
        }
      }

      String[] fila;

      while ((fila = lector.readNext()) != null ) {
        try {
          if(validarCamposObligatorios(fila,indices)){
            throw new RuntimeException("Faltan campos obligatorios");
          }

          String titulo = getCampo(fila, indices.get(CampoEsperado.TITULO));
          String descripcion = getCampo(fila, indices.get(CampoEsperado.DESCRIPCION));
          String categoria = getCampo(fila, indices.get(CampoEsperado.CATEGORIA));
          String latitud = getCampo(fila, indices.get(CampoEsperado.LATITUD));
          String longitud = getCampo(fila, indices.get(CampoEsperado.LONGITUD));
          LocalDate fechaHecho = LocalDate.parse(getCampo(fila, indices.get(CampoEsperado.FECHA_ACONTECIMIENTO)), formatter);

          // TODO filtro por titulo unico

          String latitudLimpia = latitud.trim().replace(",", "."); // Por si viene con coma decimal
          Double latitudDouble = Double.parseDouble(latitudLimpia);

          String longitudLimpia = longitud.trim().replace(",", ".");
          Double longitudDouble = Double.parseDouble(longitudLimpia);
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

        } catch (Exception e) {
          errores ++;
          continue;
        }
      }
    }catch(Exception e){
        System.out.println("Error al procesar el archivo: " + e.getMessage());
        e.printStackTrace();
      }
    System.out.println("Cantidad de Errores " + errores);
    for (Hecho hecho : hechos) {
      System.out.println(hecho.getTitulo() + ' ' + hecho.getUbicacion());
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

  private static boolean validarCamposObligatorios(String[] fila, Map<CampoEsperado, Integer> indices){
    return Objects.requireNonNull(getCampo(fila, indices.get(CampoEsperado.TITULO))).isEmpty()
        || Objects.requireNonNull(getCampo(fila, indices.get(CampoEsperado.LATITUD))).isEmpty()
        || Objects.requireNonNull(getCampo(fila, indices.get(CampoEsperado.LONGITUD))).isEmpty();
  }

}

