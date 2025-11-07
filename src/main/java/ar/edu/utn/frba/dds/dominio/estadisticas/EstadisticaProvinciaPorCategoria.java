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
@DiscriminatorValue("provPorCat")
public class EstadisticaProvinciaPorCategoria extends Estadistica {

    @Transient
    private CalculadorProvincia calculadorProvincia;

      public EstadisticaProvinciaPorCategoria() {

      }

      public EstadisticaProvinciaPorCategoria(CalculadorProvincia calculadorProvincia, String categoria, boolean publica) {
        this.publica = publica;
        this.calculadorProvincia = calculadorProvincia;
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

        // Contamos por provincia usando el calculador externo
        Map<Provincia, Long> conteo = filtrados.stream()
            .map(h -> calculadorProvincia.calcularProvincia(h.getUbicacion()))
            .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

          this.respuesta = conteo.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(e -> "La Provincia con mas hechos para la categoria " + categoria + " es: " + e.getKey().getNombre() + " (" + e.getValue() + " hechos)")
            .orElse("No hay datos para la categoria "+ categoria);

          this.fechaDeCalculo = LocalDateTime.now();


          return respuesta;
      }

}
