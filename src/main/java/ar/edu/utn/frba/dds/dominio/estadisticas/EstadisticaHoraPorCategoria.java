package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticaHoraPorCategoria extends Estadistica {

  private final String categoriaBuscada;

  public EstadisticaHoraPorCategoria(String categoriaBuscada, boolean publica) {
    super(publica);
    this.categoriaBuscada = categoriaBuscada;
  }

  @Override
  public String calcular(List<Hecho> hechos) {

    List<Hecho> filtrados = hechos.stream()
        .filter(h -> h.getCategoria().equalsIgnoreCase(categoriaBuscada))
        .toList();

    if (filtrados.isEmpty()) {
      return "Sin hechos para la categoría: " + categoriaBuscada;
    }

    Map<Integer, Long> conteoPorHora = filtrados.stream()
        .map(h -> h.getFechaAcontecimiento().getHour()) // Obtener la hora de LocalDateTime
        .collect(Collectors.groupingBy(h -> h, Collectors.counting()));

    Map.Entry<Integer, Long> maxHora = conteoPorHora.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .orElse(null);


    assert maxHora != null;

    return "Hora con más hechos de lacategoría " + categoriaBuscada + ": "
        + maxHora.getKey() + " hs (" + maxHora.getValue() + " hechos)";
  }
}
