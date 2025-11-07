package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticaHoraPorCategoria extends Estadistica {

  public EstadisticaHoraPorCategoria(String categoria, boolean publica) {
    super(publica);
    this.categoria = categoria;
  }

  @Override
  public String calcular(List<Hecho> hechos) {

    List<Hecho> filtrados = hechos.stream()
        .filter(h -> h.getCategoria().equalsIgnoreCase(categoria))
        .toList();

    if (filtrados.isEmpty()) {
        this.respuesta = "Sin hechos para la categoría: " + categoria;
    }

    Map<Integer, Long> conteoPorHora = filtrados.stream()
        .map(h -> h.getFechaAcontecimiento().getHour()) // Obtener la hora de LocalDateTime
        .collect(Collectors.groupingBy(h -> h, Collectors.counting()));

    Map.Entry<Integer, Long> maxHora = conteoPorHora.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .orElse(null);


    assert maxHora != null;

    this.respuesta = "Hora con más hechos de lacategoría " + categoria + ": "
        + maxHora.getKey() + " hs (" + maxHora.getValue() + " hechos)";

    this.fechaDeCalculo = LocalDateTime.now();

    return respuesta;
  }
}
