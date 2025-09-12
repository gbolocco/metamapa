package csv;

import static ar.edu.utn.frba.dds.dominio.exportador.ExportadorCsv.exportToCsv;

import java.util.List;
import org.junit.jupiter.api.Test;

public class CsvTest {

  List<String[]> data = List.of(
      new String[]{"John", "Doe", "30"},
      new String[]{"Jane", "Smith", "25"}
  );

  @Test
  void pruebaExportacion() {
    exportToCsv("./estadisticas/","people_opencsv.csv", data);
  }
}
