package ar.edu.utn.frba.dds.usuarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.AppLogger;
import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.colecciones.contratos.ColeccionRepository;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.filtros.TipoCombinacion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudEliminacionRepositoryMemory;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import org.slf4j.Logger;


public class AdministradorTest {

  private static final Logger logger = AppLogger.getLogger(AdministradorTest.class);

  private Coleccion coleccion;
  private Hecho hecho;
  private List<Hecho> hechos= new ArrayList<Hecho>();
  private ColeccionRepository colectionRep;
  private SolicitudEliminacionRepositoryMemory solicitudRep;

  private SolicitudEliminacion solicitud;

  private SolicitudEliminacion crearUnaSolicitudDeEliminacionParaTest(Hecho hecho) {
    String justificacionLarga = "a".repeat(501);
    SolicitudEliminacion s =  new SolicitudEliminacion(hecho, justificacionLarga);
    SolicitudEliminacionRepositoryMemory.getInstancia().agregar(s);
    return s;
  }

  private Coleccion crearUnaColeccionParaTest() {
    return new Coleccion(
        "Incendios 2025",
        "Hechos de incendios",
        List.of(mock(Filtro.class)),  // Lista con un mock de Filtro
        "ruta.csv",
        TipoCombinacion.AND,
        mock(LectorCsv.class)
    );
  }


  @BeforeEach
  void setUp() {

    // Limpieza de estado antes de cada test
    ColeccionRepositoryMemory.getInstancia().vaciar();
    SolicitudEliminacionRepositoryMemory.getInstancia().vaciar();

    logger.info("Iniciando test de Administrador");

    // Repositorios en memoria
    colectionRep = ColeccionRepositoryMemory.getInstancia();
    solicitudRep = SolicitudEliminacionRepositoryMemory.getInstancia();

    // Crear mock de Hecho
    hecho = mock(Hecho.class);

    solicitud = crearUnaSolicitudDeEliminacionParaTest(hecho);

  }
  @Test
  void puedeCrearUnaColeccionyAgregarlaALaListaDeColecciones() {

    coleccion = crearUnaColeccionParaTest();

    assertEquals("Incendios 2025", coleccion.getTitulo());
    assertEquals("Hechos de incendios", coleccion.getDescripcion());
    coleccion.cargarColeccion();
    assertTrue(colectionRep.mostrarColecciones().contains(coleccion));
  }
  @Test
  void puedeCargarHechosDesdeFuente() {

    coleccion = crearUnaColeccionParaTest();
    
    coleccion.cargarHechosDesdeFuente();
    assertTrue(coleccion.mostrarHechos().isEmpty());
  }

  @Test
  void puedeAceptarUnaSolicitudDeEliminacion() {

    assertTrue(solicitud.estaPendiente());
    assertTrue(solicitudRep.mostrarSolicitudes().contains(solicitud));
    solicitud.aceptar();
    assertFalse(solicitud.estaPendiente());
    assertTrue(solicitudRep.mostrarSolicitudes().isEmpty());
  }
}
