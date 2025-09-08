package db;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class BusquedaTextoLibreTest implements SimplePersistenceTest {

  @Test
  void buscarHecho1() {
    List<Hecho> hechosEncontrados = HechosRepositoryMemory.getInstancia().buscarHechos("Prueba1");
    Assertions.assertNotNull(hechosEncontrados);
  }
}
