package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("cantPorCat")
public class EstadisticaCantidadPorCategoria extends Estadistica {



    protected EstadisticaCantidadPorCategoria() {

    }

    public EstadisticaCantidadPorCategoria(String categoria, boolean publica) {
        this.publica = publica;
        this.categoria = categoria;
    }

    @Override
    public String calcular(List<Hecho> hechos) {

        long cantidad = hechos.stream()
                .filter(h -> h.getCategoria() != null && h.getCategoria().equalsIgnoreCase(categoria))
                .count();

        if (cantidad == 0) {
            this.respuesta = "No se encontraron hechos de la categoría '" + categoria + "'.";
        }

        this.fechaDeCalculo = LocalDateTime.now();

        this.respuesta = "La categoría '" + categoria + "' tiene " + cantidad + " hechos reportados.";

        return respuesta;
    }
}
