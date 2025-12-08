package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.estadisticas.Estadistica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.config.EntityManagerProvider;
import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EstadisticasRepository {

    private static final EstadisticasRepository instance = new EstadisticasRepository();

    private List<Estadistica> estadisticasPendientes = new ArrayList<>();

    private EstadisticasRepository() {
    }

    public static EstadisticasRepository getInstancia() {
        return instance;
    }

    private EntityManager entityManager() {
        return EntityManagerProvider.getEntityManager();
    }

    public void addEstadistica(Estadistica estadistica) {
        this.estadisticasPendientes.add(estadistica);
    }

    public void actualizarEstadistica(Estadistica estadistica, String respuesta) {
        entityManager()
                .createQuery("UPDATE Estadistica SET respuesta = :respuesta WHERE id = :id")
                .setParameter("respuesta", respuesta)
                .setParameter("id", estadistica.getId());
    }

    public void persistirEstadistica(Estadistica estadistica) {
        EntityManagerProvider.withTransaction(() -> entityManager().persist(estadistica));
    }

    public List<Estadistica> getEstadisticas() {
        return entityManager()
                .createQuery("FROM Estadistica e", Estadistica.class)
                .getResultList();
    }

    public List<Estadistica> getEstadisticasPendientes() {
        return this.estadisticasPendientes;
    }

    public void calcular(List<Hecho> hechos) {
        EntityManagerProvider.withTransaction(() -> {
            for (Estadistica estadistica : estadisticasPendientes) {
                estadistica.calcular(hechos);
                entityManager().persist(estadistica);
            }
            estadisticasPendientes.clear();
        });
    }
}
