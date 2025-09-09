package ar.edu.utn.frba.dds.usuarios;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.colecciones.contratos.ColeccionRepository;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.TipoCombinacion;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.dominio.spam.DetectorDeSpam;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.slf4j.Logger;


public class AdministradorTest {

  private static final Logger logger = AppLogger.getLogger(AdministradorTest.class);

  private Coleccion coleccion;
  private Hecho hecho;
  private SolicitudEliminacion solicitud;
  private DetectorDeSpam detectorDeSpam;

  private SolicitudEliminacion crearUnaSolicitudDeEliminacionParaTest(Hecho hecho) {
    String justificacionLarga = "a".repeat(501);
    SolicitudEliminacion s = new SolicitudEliminacion(hecho, justificacionLarga);
    SolicitudesRepositoryMemory.getInstancia().agregar(s);
    s.setDetectorDeSpam(detectorDeSpam);
    return s;
  }

  private Coleccion crearUnaColeccionParaTest() {
    FuenteEstatica fuente = new FuenteEstatica("ruta.csv", mock(LectorCsv.class));
    return new Coleccion(
        "Incendios 2025",
        "Hechos de incendios",
        List.of(mock(Filtro.class)),  // Lista con un mock de Filtro
        fuente,
        "A1302"
    );
  }


  @BeforeEach
  void setUp() {

    logger.info("Iniciando test de Administrador");

    // Crear mock de Hecho
    hecho = mock(Hecho.class);
    detectorDeSpam = mock(DetectorDeSpam.class);
    solicitud = crearUnaSolicitudDeEliminacionParaTest(hecho);

  }

  @Test
  void puedeCrearUnaColeccionyAgregarlaALaListaDeColecciones() {

    coleccion = crearUnaColeccionParaTest();

    assertEquals("Incendios 2025", coleccion.getTitulo());
    assertEquals("Hechos de incendios", coleccion.getDescripcion());
    coleccion.cargarColeccion();
    assertTrue(ColeccionRepositoryMemory.getInstancia().mostrarColecciones().contains(coleccion));
  }

  @Test
  void puedeCargarHechosDesdeFuente() {

    coleccion = crearUnaColeccionParaTest();
    coleccion.cargarHechos();

    assertTrue(coleccion.mostrarHechos().isEmpty());
  }

  @Test
  void puedeAceptarUnaSolicitudDeEliminacion() {

    assertTrue(solicitud.estaPendiente());
    assertTrue(SolicitudesRepositoryMemory.getInstancia().mostrarSolicitudes(TipoSolicitud.ELIMINACION_HECHO).contains(solicitud));
    solicitud.aceptar();
    assertFalse(solicitud.estaPendiente());
    assertTrue(solicitud.getEstadoSolicitud() == EstadoSolicitud.ACEPTADA);

  }

  @Test
  void solicitudEliminacionRechazadaPorSpam() {
    when(detectorDeSpam.esSpam(any())).thenReturn(true);
    solicitud.verificarSpam();
    assertEquals(EstadoSolicitud.RECHAZADA, solicitud.getEstadoSolicitud());

  }

  @Test
  void solicitudEliminacionNoRechazadaPorSpam() {
    when(detectorDeSpam.esSpam(any())).thenReturn(false);
    solicitud.verificarSpam();
    assertNotEquals(EstadoSolicitud.RECHAZADA, solicitud.getEstadoSolicitud());
  }
}
