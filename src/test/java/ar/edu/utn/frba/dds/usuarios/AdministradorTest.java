package ar.edu.utn.frba.dds.usuarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


public class AdministradorTest {

  private Coleccion coleccion;
  private List<Hecho> hechos= new ArrayList<Hecho>();
  private ColeccionRepository colectionRep;
  private SolicitudEliminacionRepositoryMemory solicitudRep;

  private SolicitudEliminacion solicitud;

  @BeforeEach
  void setUp() {
    List<Filtro> filtros = List.of(
        new FiltroContieneTexto("Incendio", CampoDeHecho.CATEGORIA)
    );
    colectionRep = ColeccionRepositoryMemory.getInstancia();
    solicitudRep = SolicitudEliminacionRepositoryMemory.getInstancia();

    LectorCsv lectorMock = mock(LectorCsv.class);
    when(lectorMock.leer(any())).thenReturn(hechos);

    Hecho hecho = mock(Hecho.class);
    String justificacion = "a".repeat(501);
    solicitud = new SolicitudEliminacion(
        hecho,
        justificacion
    );
    solicitudRep.agregar(solicitud);
    coleccion = new Coleccion(
        "Incendios 2025",
        "Hechos de incendios",
        filtros,
        "ruta.csv",
        TipoCombinacion.AND,
        lectorMock);
  }

  @Test
  void puedeCrearUnaColeccionyAgregarlaALaListaDeColecciones() {
    assertEquals("Incendios 2025", coleccion.getTitulo());
    assertEquals("Hechos de incendios", coleccion.getDescripcion());
    coleccion.cargarColeccion();
    assertTrue(colectionRep.mostrarColecciones().contains(coleccion));
  }
  @Test
  void puedeCargarHechosDesdeFuente() {
    coleccion.cargarHechosDesdeFuente();
    assertTrue(coleccion.mostrarHechos().isEmpty());
  }
  @Test
  void seAceptaUnaSolicitudDeEliminacion() {
    assertTrue(solicitud.estaPendiente());
    assertTrue(solicitudRep.mostrarSolicitudes().contains(solicitud));
    solicitud.aceptar();
    assertFalse(solicitud.estaPendiente());
    assertTrue(solicitudRep.mostrarSolicitudes().isEmpty());
  }
}
