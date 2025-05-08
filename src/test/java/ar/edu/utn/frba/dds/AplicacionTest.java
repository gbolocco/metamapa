package ar.edu.utn.frba.dds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.hecho.Hecho;
import ar.edu.utn.frba.dds.hecho.OriginHecho;
import ar.edu.utn.frba.dds.hecho.Ubicacion;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class AplicacionTest {

  @Test
  public void testValidarJustificacionSolicitud() { // para probar validaciones
    Hecho hecho = new Hecho("Prueba", "Prueba", "Prueba", new Ubicacion(1.1,1.1),LocalDate.now(),LocalDate.now(), OriginHecho.FUENTE);

    String justificacionCorta = "no se";

    assertThrows(IllegalArgumentException.class, () -> {
      new SolicitudEliminacion(hecho, justificacionCorta); // justificacion larga
    });
    assertThrows(IllegalArgumentException.class, () -> {
      new SolicitudEliminacion(hecho, null); // sin justificacion
    });
  }
}