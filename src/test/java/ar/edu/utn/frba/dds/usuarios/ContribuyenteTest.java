package ar.edu.utn.frba.dds.usuarios;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.slf4j.Logger;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import ar.edu.utn.frba.dds.dominio.spam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.colecciones.contratos.ColeccionRepository;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryDB;
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
  private SolicitudesRepositoryDB solicitudRep;
  private DetectorDeSpam detectorDeSpam;
  private SolicitudEliminacion solicitud;
  private RepresentacionDeHecho representacionDeHecho;

  private SolicitudEliminacion crearUnaSolicitudDeEliminacionParaTest(RepresentacionDeHecho representacionDeHecho) {
    String justificacionLarga = "a".repeat(501);
    SolicitudEliminacion s =  new SolicitudEliminacion( representacionDeHecho, justificacionLarga);
    SolicitudesRepositoryDB.getInstancia().agregar(s);
    return s;
  }

  @BeforeEach
  void setUp() {



    // Repositorios en memoria
    solicitudRep = SolicitudesRepositoryDB.getInstancia();

    // Crear mock de Hecho
    hecho = mock(Hecho.class);
    representacionDeHecho = mock(RepresentacionDeHecho.class);
    // Mockeo el detector
    detectorDeSpam = mock(DetectorDeSpam.class);
  }

  @Test
  void puedeCrearUnaSolicitudDeEliminacion() {

    solicitud = crearUnaSolicitudDeEliminacionParaTest(representacionDeHecho);

    String justificacionLarga = "a".repeat(501);
    //entityManager().getTransaction().commit();
    assertTrue(solicitudRep.mostrarSolicitudes(TipoSolicitud.ELIMINACION_HECHO).contains(solicitud));
    assertTrue(solicitud.estaPendiente());
  }
}
