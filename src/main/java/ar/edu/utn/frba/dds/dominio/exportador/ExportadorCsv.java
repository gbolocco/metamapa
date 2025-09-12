package ar.edu.utn.frba.dds.dominio.exportador;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportadorCsv {
  
  public ExportadorCsv() {}

  public static void exportToCsv(String path, String fileName, List<String[]> data) {
    try (CSVWriter writer = new CSVWriter(new FileWriter(path + fileName))) {

      for (String[] row : data) {
        writer.writeNext(row);
      }
      System.out.println("CSV file created successfully at: " + fileName);
    } catch (IOException e) {
      System.err.println("Error writing to CSV file: " + e.getMessage());
    }
  }

}
