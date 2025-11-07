package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.exportador.ExportadorCsv;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class GestorDeEstadisticas {

  private final List<Estadistica> estadisticas;
  private final List<String> resultados;
  private ExportadorCsv exportador = new ExportadorCsv();

  public GestorDeEstadisticas(List<Estadistica> estadisticas) {
    this.estadisticas = estadisticas;
    this.resultados = new ArrayList<>();
  }

  public List<String> calcular(List<Hecho> hechos) {
    resultados.clear();

    List<String> res = estadisticas.stream()
        .flatMap(e -> e.calcular(hechos).lines()) // aplana los streams de líneas
        .toList();

    resultados.addAll(res);
    return res;
  }

  public void generarArchivoCsv(String path, String fileName) {
    fileName = fileName + ".csv";
    List<String[]> data = IntStream.range(0, estadisticas.size())
        .mapToObj(i -> new String[] {
            estadisticas.get(i).getClass().getSimpleName(),
            resultados.get(i)
        })
        .toList();

    exportador.exportToCsv(path, fileName, data);
  }

}
