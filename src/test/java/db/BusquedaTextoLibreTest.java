package db;

import ar.edu.utn.frba.dds.dominio.archivos.FileUtils;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class BusquedaTextoLibreTest implements SimplePersistenceTest {
  Hecho hecho = new Hecho("Prueba1", "Prueba1", "Prueba1", new Ubicacion(30.2, 30.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
  Hecho hecho2 = new Hecho("Prueba2", "Prueba2", "Prueba2", new Ubicacion(20.2, 10.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.FUENTE_PROXY);
  FileUtils fileUtils = new FileUtils();

  @BeforeEach
  void persistir2Hechos() {
    HechosRepository repo = HechosRepository.getInstancia();
    repo.cargarHecho(hecho);
    repo.cargarHecho(hecho2);

    entityManager().flush();
    entityManager().getTransaction().commit();
  }

  @BeforeEach
  void borrarIndices() {
    fileUtils.borrarArchivosCarpeta("./indexes");
  }

  @Test
  void buscarHecho1() {
    List<Hecho> hechosEncontrados = HechosRepository.getInstancia().buscarPorTexto("Prueba1");
    Assertions.assertNotNull(hechosEncontrados);
  }

  @Test
  void buscarHecho2() {
    List<Hecho> hechosEncontrados = HechosRepository.getInstancia().buscarPorTexto("Prueba2");
    Assertions.assertNotNull(hechosEncontrados);
  }

  @Test
  void busquedaHechoFail() {
    List<Hecho> hechosEncontrados = HechosRepository.getInstancia().buscarPorTexto("Cualquier cosa");
    Assertions.assertTrue(hechosEncontrados.isEmpty());
  }
}
