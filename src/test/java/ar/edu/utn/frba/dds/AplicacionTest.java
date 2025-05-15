package ar.edu.utn.frba.dds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OriginHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class AplicacionTest {

  @Test
  @DisplayName("Validaciones de justificacion de solicitud")
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