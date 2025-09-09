package ar.edu.utn.frba.dds.usuarios;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.slf4j.Logger;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import ar.edu.utn.frba.dds.dominio.spam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.colecciones.contratos.ColeccionRepository;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContribuyenteTest implements SimplePersistenceTest  {

  private static final Logger logger = AppLogger.getLogger(ContribuyenteTest.class);

  private List<Hecho> hechos = new ArrayList<Hecho>();
  private Hecho hecho;
  private Coleccion coleccion;
  private ColeccionRepository colectionRep;
  private SolicitudesRepositoryMemory solicitudRep;
  private DetectorDeSpam detectorDeSpam;
  private SolicitudEliminacion solicitud;

  private SolicitudEliminacion crearUnaSolicitudDeEliminacionParaTest(Hecho hecho, DetectorDeSpam detectorDeSpam) {
    String justificacionLarga = "a".repeat(501);
    SolicitudEliminacion s =  new SolicitudEliminacion(hecho, justificacionLarga);
    SolicitudesRepositoryMemory.getInstancia().agregar(s);
    return s;
  }

  @BeforeEach
  void setUp() {



    // Repositorios en memoria
    solicitudRep = SolicitudesRepositoryMemory.getInstancia();

    // Crear mock de Hecho
    hecho = mock(Hecho.class);

    // Mockeo el detector
    detectorDeSpam = mock(DetectorDeSpam.class);
  }

  @Test
  void puedeCrearUnaSolicitudDeEliminacion() {

    //solicitud = crearUnaSolicitudDeEliminacionParaTest(hecho, detectorDeSpam);

    String justificacionLarga = "a".repeat(501);
    solicitud = new SolicitudEliminacion(hecho, justificacionLarga);
    entityManager().getTransaction().commit();
    assertTrue(solicitudRep.mostrarSolicitudes(TipoSolicitud.ELIMINACION_HECHO).contains(solicitud));
    assertTrue(solicitud.estaPendiente());
  }
}
