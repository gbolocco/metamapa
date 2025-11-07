package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticaCategoria extends Estadistica {

  public EstadisticaCategoria(boolean publica) {
    super(publica);
  }

  public String calcular(List<Hecho> hechos) {

    Map<String, Long> conteo = hechos.stream()
        .collect(Collectors.groupingBy(Hecho::getCategoria, Collectors.counting()));


    this.respuesta = conteo.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(e -> "La categoria con la mayor cantidad de hechos del sistema es:" + e.getKey() + " (" + e.getValue() + " hechos)")
        .orElse("Sin datos");

    return respuesta;
  }
}
