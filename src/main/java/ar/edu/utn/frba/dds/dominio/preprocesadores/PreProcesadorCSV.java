package ar.edu.utn.frba.dds.dominio.preprocesadores;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.*;

public class PreProcesadorCSV {

  public File preprocesar(String inputPath) throws IOException, CsvException {

    String outputPath = inputPath.replaceFirst("(\\.csv)$", "_processed$1");
    File inputFile = new File(inputPath);
    String inputFileName = inputFile.getName().replaceFirst("\\.csv$", "");
    // 1. Renombrando columnas
    Map<String, String> columnasARenombrar = Map.of(
        "Latitude", "latitud",
        "Longitude", "longitud"
    );

    // 2. Columnas nuevas que van al inicio
    List<String> extraColumns = List.of("titulo","descripcion", "categoria", "fecha_acontecimiento");

    try (
        CSVReader reader = new CSVReaderBuilder(new FileReader(inputFile))
            .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
            .build();
        CSVWriter writer = new CSVWriter(
            new FileWriter(outputPath),
            ';',
            CSVWriter.NO_QUOTE_CHARACTER,
            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END
        );
    ) {
      String[] encabezadoOriginal = reader.readNext();
      if (encabezadoOriginal == null) throw new IOException("Archivo CSV vacío");

      // 3. Construimos el encabezado nuevo: columnas nuevas al inicio, luego renombradas
      List<String> nuevoEncabezado = new ArrayList<>(extraColumns);
      for (String encabezadoNombre : encabezadoOriginal) {
        nuevoEncabezado.add(columnasARenombrar.getOrDefault(encabezadoNombre, encabezadoNombre));
      }
      writer.writeNext(nuevoEncabezado.toArray(new String[0]));

      // 4. Procesar fila por fila
      String[] row;

      int numeroFila = 1;
      while ((row = reader.readNext()) != null) {
        numeroFila++;
        if (row.length < encabezadoOriginal.length) {
          System.out.println("⚠️ Fila incompleta (línea " + numeroFila + "): " + Arrays.toString(row));
          System.out.println("→ Se esperaban " + encabezadoOriginal.length + " columnas, pero hay " + row.length);
          continue;
        }
        Map<String, String> filaMapeada = new HashMap<>();
        for (int i = 0; i < encabezadoOriginal.length; i++) {
          String originalCol = encabezadoOriginal[i];
          String renamedCol = columnasARenombrar.getOrDefault(originalCol, originalCol);
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
          String año = filaMapeada.getOrDefault("Start Year", "").trim();
          String mes = filaMapeada.getOrDefault("Start Month", "").trim();
          String dia = filaMapeada.getOrDefault("Start Day", "").trim();

          if (!año.isEmpty() && !mes.isEmpty()) {
            dia  = !dia.isEmpty() ? String.format("%02d", Integer.parseInt(dia)) : String.format("%02d", 1);
            mes = String.format("%02d", Integer.parseInt(mes));
            newRow.add(año + "-" + mes + "-" + dia);
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
