package db;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersistenciaTest implements SimplePersistenceTest {

  @Test
  void persistir2Hechos() {
    Hecho hecho = new Hecho("Prueba1", "Prueba1", "Prueba1", new Ubicacion(30.2,30.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
    Hecho hecho2 = new Hecho("Prueba2", "Prueba2", "Prueba2", new Ubicacion(20.2,10.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.FUENTE_PROXY);

    HechosRepositoryMemory repo=  HechosRepositoryMemory.getInstancia();
    repo.cargarHecho(hecho);
    repo.cargarHecho(hecho2);

    entityManager().flush();
    entityManager().getTransaction().commit();
    assertEquals(2, repo.mostrarHechos().size());
  }

  @Test
  void persistirColeccion() {
    Fuente fuente = new FuenteDinamica();
    FiltroContieneTexto filtroTexto1 = new FiltroContieneTexto("incendio en la rioja", CampoDeHecho.TITULO);
    Coleccion coleccion = new Coleccion("coleccion","descripcion", List.of(filtroTexto1),fuente,"handle");

    ColeccionRepositoryMemory repo=  ColeccionRepositoryMemory.getInstancia();
    repo.agregarColeccion(coleccion);

    entityManager().flush();
    entityManager().getTransaction().commit();

    assertEquals(coleccion,repo.buscarColeccionPorId(coleccion.getId()));

  }

  @Test
  void seCarganLosHechosPersistidosDeUnaFuenteEnLaColeccion() {
    Fuente fuente = new FuenteDinamica();
    Hecho hecho = new Hecho("incendio en la rioja", "Prueba1", "Prueba1", new Ubicacion(30.2,30.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
    HechosRepositoryMemory repoHechos=  HechosRepositoryMemory.getInstancia();

    FiltroContieneTexto filtroTexto1 = new FiltroContieneTexto("incendio en la rioja", CampoDeHecho.TITULO);

    Coleccion coleccion = new Coleccion("coleccion","descripcion", List.of(filtroTexto1),fuente,"handle");
    ColeccionRepositoryMemory repo=  ColeccionRepositoryMemory.getInstancia();

    repo.agregarColeccion(coleccion);

    repoHechos.cargarHecho(hecho);

    coleccion.cargarHechos();

    entityManager().getTransaction().commit();

    assertEquals(hecho.getTitulo(),repoHechos.buscar(hecho.getId()).getTitulo());

    assertTrue(repoHechos.filtrarHechos(List.of(filtroTexto1), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE).contains(hecho));

  }

}
