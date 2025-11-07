package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.time.LocalDateTime;
import java.util.List;

public class EstadisticaCantidadPorCategoria extends Estadistica {

    public EstadisticaCantidadPorCategoria(String categoria, boolean publica) {
        super(publica);
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
