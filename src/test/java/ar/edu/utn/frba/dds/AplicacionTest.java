package ar.edu.utn.frba.dds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import org.slf4j.Logger;
import ar.edu.utn.frba.dds.compartido.AppLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class AplicacionTest {

  private static final Logger logger = AppLogger.getLogger(AplicacionTest.class);

  @Test
  public void testLogger(){
    logger.info("log info");
    logger.debug("log debug");
    logger.warn("log warn");
    logger.error("log error");
    logger.trace("log trace");

    // para ver los logs de "debug" y "trace" hay que cambiarle el nivel en logback.xml
    // <root level="DEBUG"> o <root level="TRACE">
  }

  @Test
  @DisplayName("Validaciones de justificacion de solicitud")
  public void testValidarJustificacionSolicitud() { // para probar validaciones
    Hecho hecho = new Hecho("Prueba", "Prueba", "Prueba", new Ubicacion(1.1,1.1),LocalDate.now(),LocalDate.now(), OrigenHecho.FUENTE_ESTATICA);

    String justificacionCorta = "no se";

    assertThrows(IllegalArgumentException.class, () -> {
      new SolicitudEliminacion(hecho, justificacionCorta); // justificacion larga
    });
    assertThrows(IllegalArgumentException.class, () -> {
      new SolicitudEliminacion(hecho, null); // sin justificacion
    });
  }
}