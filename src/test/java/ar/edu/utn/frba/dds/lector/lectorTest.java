package ar.edu.utn.frba.dds.lector;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.Lector;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class lectorTest {
  Lector lector;
  List<Hecho> hechos;
  @BeforeEach
  void setUp() {
    lector = new LectorCsv();
    hechos=lector.leer("datos/desastres_naturales_first_8 (1).csv");
  }
  @Test
  void testLectorCsv() {
    System.out.println(hechos.size());
    assertTrue(hechos.size()>0);
  }
}
