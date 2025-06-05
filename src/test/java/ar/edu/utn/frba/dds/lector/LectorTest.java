package ar.edu.utn.frba.dds.lector;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import org.slf4j.Logger;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.Lector;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class LectorTest {

  private static AppLogger AppLogger;
  private static final Logger logger = AppLogger.getLogger(LectorTest.class);

  Lector lector;
  List<Hecho> hechos;

  @BeforeEach
  void setUp() {
    lector = new LectorCsv();
    hechos=lector.leer("datos/desastres_naturales_processed.csv");
  }
  @Test
  void testLectorCsv() {
    logger.info(String.valueOf(hechos.size()));
    assertFalse(hechos.isEmpty());
  }
}
