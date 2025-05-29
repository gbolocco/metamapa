package ar.edu.utn.frba.dds.dominio.preprocesadores;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;


public class PreProcesadorCsv {

  private static final Logger logger = AppLogger.getLogger(PreProcesadorCsv.class);

  public File preprocesar(String inputPath) throws IOException, CsvException {

    String outputPath = inputPath.replaceFirst("(\\.csv)$", "_processed$1");
    File inputFile = new File(inputPath);
    String inputFileName = inputFile.getName().replaceFirst("\\.csv$", "");
    // 1. Renombrando columnas
    Map<String, String> columnasParaRenombrar = Map.of(
        "Latitude", "latitud",
        "Longitude", "longitud"
    );

    // 2. Columnas nuevas que van al inicio
    List<String> extraColumns = List.of(
        "titulo",
        "descripcion",
        "categoria",
        "fecha_acontecimiento"
    );

    try (
        CSVReader reader = new CSVReaderBuilder(
            new InputStreamReader(
                new FileInputStream(inputFile),
                StandardCharsets.UTF_8
            ))
            .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
            .build();

        CSVWriter writer = new CSVWriter(
            new OutputStreamWriter(
                new FileOutputStream(outputPath),
                StandardCharsets.UTF_8
            ),
            ';',
            CSVWriter.NO_QUOTE_CHARACTER,
            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END
        );
    ) {
      String[] encabezadoOriginal = reader.readNext();
      if (encabezadoOriginal == null) {
        throw new IOException("Archivo CSV vacío");
      }

      // 3. Construimos el encabezado nuevo: columnas nuevas al inicio, luego renombradas
      List<String> nuevoEncabezado = new ArrayList<>(extraColumns);
      for (String encabezadoNombre : encabezadoOriginal) {
        nuevoEncabezado.add(columnasParaRenombrar.getOrDefault(encabezadoNombre, encabezadoNombre));
      }
      writer.writeNext(nuevoEncabezado.toArray(new String[0]));

      // 4. Procesar fila por fila
      String[] row;

      int numeroFila = 1;
      while ((row = reader.readNext()) != null) {
        numeroFila++;
        if (row.length < encabezadoOriginal.length) {
          logger.info("Fila incompleta (línea {}): {}", numeroFila, Arrays.toString(row));
          logger.info(
              "Se esperaban {} columnas, pero hay {}", encabezadoOriginal.length, row.length
          );
          continue;
        }
        Map<String, String> filaMapeada = new HashMap<>();
        for (int i = 0; i < encabezadoOriginal.length; i++) {
          String originalCol = encabezadoOriginal[i];
          String renamedCol = columnasParaRenombrar.getOrDefault(originalCol, originalCol);
          filaMapeada.put(renamedCol, row[i]);
        }
        List<String> filaConstruida = construirFila(nuevoEncabezado, filaMapeada, inputFileName);
        writer.writeNext(filaConstruida.toArray(new String[0]));
      }
    }

    return new File(outputPath);
  }

  public static List<String> construirFila(
      List<String> nuevoEncabezado,
      Map<String, String> filaMapeada,
      String inputFileName
  ) {
    List<String> newRow = new ArrayList<>();

    for (String col : nuevoEncabezado) {
      switch (col) {
        case "titulo":
          newRow.add(inputFileName + "_" + filaMapeada.getOrDefault("Location", ""));
          break;
        case "fecha_acontecimiento":
          String anio = filaMapeada.getOrDefault("Start Year", "").trim();
          String mes = filaMapeada.getOrDefault("Start Month", "").trim();
          String dia = filaMapeada.getOrDefault("Start Day", "").trim();

          if (!anio.isEmpty() && !mes.isEmpty()) {

            dia  = !dia.isEmpty()
                ? String.format("%02d", Integer.parseInt(dia)) : String.format("%02d", 1);

            mes = String.format("%02d", Integer.parseInt(mes));

            newRow.add(dia + "/" + mes + "/" + anio);
          }
          break;
        case "categoria":
          String grupo = filaMapeada.getOrDefault("Disaster Group", "").trim();
          String subgrupo = filaMapeada.getOrDefault("Disaster Subgroup", "").trim();
          String tipo = filaMapeada.getOrDefault("Disaster Type", "").trim();

          if (!grupo.isEmpty() && !subgrupo.isEmpty() && !tipo.isEmpty()) {
            newRow.add(grupo + " - " + subgrupo + " - " + tipo);
          } else {
            newRow.add("");
          }
          break;
        case "descripcion":
          newRow.add("");
          break;
        default:
          newRow.add(filaMapeada.getOrDefault(col, ""));
      }
    }

    return newRow;
  }
}
