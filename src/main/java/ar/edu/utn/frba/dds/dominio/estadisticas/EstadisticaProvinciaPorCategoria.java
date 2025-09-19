package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.estadisticas.serviciocalculadorprovincia.CalculadorProvincia;
import ar.edu.utn.frba.dds.dominio.estadisticas.serviciocalculadorprovincia.Provincia;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticaProvinciaPorCategoria extends Estadistica {

  private final CalculadorProvincia calculadorProvincia;
  private final String categoriaBuscada;

  public EstadisticaProvinciaPorCategoria(CalculadorProvincia calculadorProvincia,
                                          String categoriaBuscada, boolean publica) {
    super(publica);
    this.calculadorProvincia = calculadorProvincia;
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

    // Contamos por provincia usando el calculador externo
    Map<Provincia, Long> conteo = filtrados.stream()
        .map(h -> calculadorProvincia.calcularProvincia(h.getUbicacion()))
        .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

    return conteo.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(e -> e.getKey().getNombre() + " (" + e.getValue() + " hechos)")
        .orElse("Sin datos");
  }

}
