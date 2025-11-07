package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.estadisticas.Estadistica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EstadisticasRepository {

    private static final EstadisticasRepository instance = new EstadisticasRepository();

    private List<Estadistica> estadisticas = new ArrayList<>();

    private EstadisticasRepository() {
    }

    public static EstadisticasRepository getInstancia() {
        return instance;
    }

    public List<Estadistica> getEstadisticas() {
        return estadisticas;
    }

    public void addEstadistica(Estadistica estadistica) {
        this.estadisticas.add(estadistica);
    }

    public List<String> getRespuestas() {
        return estadisticas.stream()
                .map(Estadistica::getRespuesta)
                .filter(Objects::nonNull)
                .toList();
    }


    public List<String> calcular(List<Hecho> hechos) {

        return estadisticas.stream()
               .flatMap(e -> e.calcular(hechos).lines())
               .toList();
    }

}
