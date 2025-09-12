package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.CalculadorProvincia;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.Provincia;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticaProvincia extends Estadistica {

  private CalculadorProvincia calculadorProvincia;

  public EstadisticaProvincia(CalculadorProvincia calculadorProvincia) {
    this.calculadorProvincia = calculadorProvincia;
  }

  @Override
  public String calcular(List<Hecho> hechos) {

    Map<Provincia, Long> conteo = hechos.stream()
        .map(h -> calculadorProvincia.calcularProvincia(h.getUbicacion()))
        .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

    return conteo.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(e -> e.getKey().getNombre() + " (" + e.getValue() + " hechos)")
        .orElse("Sin datos");
  }

}
