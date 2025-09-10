package db;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BusquedaTextoLibreTest implements SimplePersistenceTest {
  Hecho hecho = new Hecho("Prueba1", "Prueba1", "Prueba1", new Ubicacion(30.2, 30.2), LocalDate.now(), LocalDate.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
  Hecho hecho2 = new Hecho("Prueba2", "Prueba2", "Prueba2", new Ubicacion(20.2, 10.2), LocalDate.now(), LocalDate.now(), OrigenHecho.FUENTE_PROXY);

  @BeforeEach
  void persistir2Hechos() {
    HechosRepositoryMemory repo = HechosRepositoryMemory.getInstancia();
    repo.cargarHecho(hecho);
    repo.cargarHecho(hecho2);

    entityManager().flush();
    entityManager().getTransaction().commit();
  }

  @Test
  void buscarHecho1() {
    List<Hecho> hechosEncontrados = HechosRepositoryMemory.getInstancia().buscarPorTexto("Prueba1");
    Assertions.assertNotNull(hechosEncontrados);
  }

  @Test
  void buscarHecho2() {
    List<Hecho> hechosEncontrados = HechosRepositoryMemory.getInstancia().buscarPorTexto("Prueba2");
    Assertions.assertNotNull(hechosEncontrados);
  }

  @Test
  void busquedaHechoFail() {
    List<Hecho> hechosEncontrados = HechosRepositoryMemory.getInstancia().buscarPorTexto("Cualquier cosa");
    Assertions.assertTrue(hechosEncontrados.isEmpty());
  }
}
