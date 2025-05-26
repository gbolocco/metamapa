package ar.edu.utn.frba.dds.usuarios;

import ar.edu.utn.frba.dds.AplicacionTest;
import ar.edu.utn.frba.dds.AppLogger;
import org.slf4j.Logger;

import org.slf4j.Logger;
import ar.edu.utn.frba.dds.AppLogger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.AplicacionTest;
import ar.edu.utn.frba.dds.AppLogger;
import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.colecciones.contratos.ColeccionRepository;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.filtros.TipoCombinacion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudEliminacionRepositoryMemory;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

public class ContribuyenteTest {

  private static final Logger logger = AppLogger.getLogger(ContribuyenteTest.class);

  private List<Hecho> hechos = new ArrayList<Hecho>();
  private Hecho hecho;
  private Coleccion coleccion;
  private ColeccionRepository colectionRep;
  private SolicitudEliminacionRepositoryMemory solicitudRep;

  private SolicitudEliminacion solicitud;

  private SolicitudEliminacion crearUnaSolicitudDeEliminacionParaTest(Hecho hecho) {
    String justificacionLarga = "a".repeat(501);
    SolicitudEliminacion s =  new SolicitudEliminacion(hecho, justificacionLarga);
    SolicitudEliminacionRepositoryMemory.getInstancia().agregar(s);
    return s;
  }

  @BeforeEach
  void setUp() {

    logger.info("Iniciando test de Contribuyente");

    // Repositorios en memoria
    solicitudRep = SolicitudEliminacionRepositoryMemory.getInstancia();

    // Crear mock de Hecho
    hecho = mock(Hecho.class);

  }

  @Test
  void puedeCrearUnaSolicitudDeEliminacion() {

    solicitud = crearUnaSolicitudDeEliminacionParaTest(hecho);

    String justificacionLarga = "a".repeat(501);
    solicitud = new SolicitudEliminacion(hecho, justificacionLarga);
    logger.info("Solicitud creada");
    solicitudRep.agregar(solicitud);
    logger.info("Solicitud agregada al repositorio de solicitudes");

    assertTrue(solicitudRep.mostrarSolicitudes().contains(solicitud));
    assertTrue(solicitud.estaPendiente());
  }
}
