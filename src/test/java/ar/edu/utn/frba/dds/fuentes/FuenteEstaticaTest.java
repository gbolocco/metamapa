package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FuenteEstaticaTest {
  public FuenteEstatica fuenteEstatica;
  @BeforeEach
  void setUp() {
    fuenteEstatica = new FuenteEstatica("./datos/desastres_naturales_processed.csv",new LectorCsv());
  }

  @Test
  void seTraeLosDatosDelCSV() {
    Assertions.assertFalse(fuenteEstatica.obtenerHechos(new ArrayList<>()).isEmpty());
  }

  @Test
  void testLectorCsv() {
    LectorCsv lector = new LectorCsv();
    List<Hecho> hechos = lector.leer("./datos/desastres_naturales_processed.csv");
    Assertions.assertFalse(hechos.isEmpty());
  }


}
