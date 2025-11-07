package ar.edu.utn.frba.dds.dominio.estadisticas;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;

public class EstadisticaCantidadPorCategoria extends Estadistica {

    private String categoria;

    public EstadisticaCantidadPorCategoria(String categoria, boolean publica) {
        super(publica);
        this.categoria = categoria;
    }

    @Override
    public String calcular(List<Hecho> hechos) {
        if (categoria == null || categoria.isEmpty()) {
            return "No se especificó una categoría.";
        }

        long cantidad = hechos.stream()
                .filter(h -> h.getCategoria() != null && h.getCategoria().equalsIgnoreCase(categoria))
                .count();

        if (cantidad == 0) {
            return "No se encontraron hechos de la categoría '" + categoria + "'.";
        }

        return "La categoría '" + categoria + "' tiene " + cantidad + " hechos reportados.";
    }
}
