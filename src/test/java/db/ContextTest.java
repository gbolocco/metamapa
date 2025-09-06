package db;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContextTest implements SimplePersistenceTest {

  @Test
  void contextUp() {
    assertNotNull(entityManager());
  }

  @Test
  void contextUpWithTransaction() throws Exception {
    withTransaction(() -> {
    });
  }

  @Test
  void insertarYTraerSolicitudes() {
    Hecho hecho = new Hecho("Prueba2", "Prueba2", "Prueba2", new Ubicacion(20.2,10.2), LocalDate.now(), LocalDate.now(), OrigenHecho.FUENTE_PROXY);
    HechosRepositoryMemory repo=  HechosRepositoryMemory.getInstancia();
    repo.cargarHecho(hecho);
    entityManager().flush();
    entityManager().getTransaction().commit();
    assertEquals(3, repo.mostrarHechos().size());
  }

  @Test
  void insertarColeccionYTraerSolicitudes() {
    Fuente fuente = new FuenteDinamica();
    Coleccion coleccion = new Coleccion("coleccion piola","descripcion piola",new ArrayList<>(),fuente,"handle");
    coleccion.cargarHechos();
    ColeccionRepositoryMemory repo=  ColeccionRepositoryMemory.getInstancia();
    repo.agregarColeccion(coleccion);
    entityManager().flush();
    entityManager().getTransaction().commit();
    assertEquals(coleccion,repo.buscarColeccionPorId(coleccion.getId()));
  }

}