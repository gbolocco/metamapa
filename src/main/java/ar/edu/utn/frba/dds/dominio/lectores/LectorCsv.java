package ar.edu.utn.frba.dds.dominio.lectores;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OriginHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.Console;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ar.edu.utn.frba.dds.dominio.hechos.CampoEsperado;

public class LectorCsv implements Lector {

  public List<Hecho> leer(String rutaArchivo) {
    if (!rutaArchivo.toLowerCase().endsWith(".csv")) {
      throw new IllegalArgumentException("Solo se permiten archivos con extensión .csv");
    }
    List<Hecho> hechos = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
        System.out.println("Encabezados: " + encabezados[1]);

      } catch (Exception e) {
        System.out.println("Error al intentar con separador '" + ";" + "': " + e.getMessage());
      }

    if (lector == null || encabezados == null) {
      throw new RuntimeException("No se pudo leer el archivo: separador inválido o archivo mal formado.");
    }

    /*boolean noEsElFormatoAdecuado = ( encabezados.length < CampoEsperado.listado().size());
    if (noEsElFormatoAdecuado) {
      throw new RuntimeException("El archivo está vacío o tiene menos columnas de las requeridas.");
    }*/

    try {
      Map<CampoEsperado, Integer> indices = new HashMap<>();
      for (int posicionDeCampo = 0; posicionDeCampo < encabezados.length; posicionDeCampo++) {
        CampoEsperado campo =      CampoEsperado.buscarPorEncabezado(encabezados[posicionDeCampo]);
        if (campo != null) {
          indices.put(campo, posicionDeCampo);
        }
      }
      System.out.println("indices" + indices);

      for (CampoEsperado campo : CampoEsperado.listado()) {
        if (!indices.containsKey(campo)) {
          throw new RuntimeException("Falta el campo obligatorio: " + campo.name());
        }
      }

      String[] fila;
      while ((fila = lector.readNext()) != null) {
        String titulo = getCampo(fila, indices.get(CampoEsperado.TITULO));
        String descripcion = getCampo(fila, indices.get(CampoEsperado.DESCRIPCION));
        String categoria = getCampo(fila, indices.get(CampoEsperado.CATEGORIA));
        Double latitud = Double.parseDouble(getCampo(fila, indices.get(CampoEsperado.LATITUD)));
        Double longitud = Double.parseDouble(getCampo(fila, indices.get(CampoEsperado.LONGITUD)));
        LocalDate fechaHecho = LocalDate.parse(getCampo(fila, indices.get(CampoEsperado.FECHA_ACONTECIMIENTO)), formatter);


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
      }
    } catch (Exception e) {
      System.out.println("Error al procesar el archivo: " + e.getMessage());
      e.printStackTrace();
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

}

