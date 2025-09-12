package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticaCategoria extends Estadistica {
  public String calcular(List<Hecho> hechos) {

    Map<String, Long> conteo = hechos.stream()
        .collect(Collectors.groupingBy(Hecho::getCategoria, Collectors.counting()));


    return conteo.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(e -> e.getKey() + " (" + e.getValue() + " hechos)")
        .orElse("Sin datos");
  }
}
