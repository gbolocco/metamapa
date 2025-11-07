package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.CalculadorProvincia;
import ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia.Provincia;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("maxProv")
public class EstadisticaProvincia extends Estadistica {

    @Transient
    private CalculadorProvincia calculadorProvincia;

    public EstadisticaProvincia() {}

    public EstadisticaProvincia(CalculadorProvincia calculadorProvincia,  boolean publica) {
    this.publica = publica;
    this.calculadorProvincia = calculadorProvincia;
    }

    
    @Override
    public String calcular(List<Hecho> hechos) {
    
    Map<Provincia, Long> conteo = hechos.stream()
        .map(h -> calculadorProvincia.calcularProvincia(h.getUbicacion()))
        .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    
      this.respuesta = conteo.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(e -> "La provincia con mas hechos ocurridos es: " + e.getKey().getNombre() + " (" + e.getValue() + " hechos)")
        .orElse("Sin datos");
    
      this.fechaDeCalculo = LocalDateTime.now();
      return respuesta;
    }

}
