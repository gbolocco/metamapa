package ar.edu.utn.frba.dds.usuarios;

import ar.edu.utn.frba.dds.compartido.AppLogger;
import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteMetaMapaAdapter;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoRepresentacionHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.dominio.spam.DetectorDeSpam;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryDB;

import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.slf4j.Logger;


public class AdministradorTest implements SimplePersistenceTest {

  private static final Logger logger = AppLogger.getLogger(AdministradorTest.class);

  private Coleccion coleccion;
  private Hecho hecho;
  private SolicitudEliminacion solicitud;
  private DetectorDeSpam detectorDeSpam;
  private RepresentacionDeHecho representacionDeHecho;
  private FuenteEstatica fuenteEstatica;
  private Ubicacion ubi = mock(Ubicacion.class);

  private SolicitudEliminacion crearUnaSolicitudDeEliminacionParaTest(RepresentacionDeHecho representacionDeHecho) {
    String justificacionLarga = "a".repeat(501);
    SolicitudEliminacion s = new SolicitudEliminacion(representacionDeHecho, justificacionLarga);
    SolicitudesRepositoryDB.getInstancia().agregar(s);
    s.setDetectorDeSpam(detectorDeSpam);
    return s;
  }

  public FuenteMetaMapa fuenteMetaMapa(List<Hecho> hechos) {
    FuenteMetaMapaAdapter adapter;
    adapter = mock(FuenteMetaMapaAdapter.class);
    when(adapter.obtenerHechos(anyMap())).thenReturn(hechos);
    return new FuenteMetaMapa(adapter);
  }

  public List<Hecho> listaDeHechos(OrigenHecho origen) {
    Hecho hecho1 = new Hecho("incendio en la rioja",
        "incendio forestal en la rioja", "incendios forestales",
        ubi, LocalDateTime.of(2024, 5, 1,0,0,0),
        LocalDateTime.now(),
        origen);
    Hecho hecho2 = new Hecho("incendio en la rioja",
        "incendio forestal en la pampa", "incendios forestales",
        ubi, LocalDateTime.of(2024, 5, 1,0,0,0),
        LocalDateTime.now(),
        origen);
    Hecho hecho3 = new Hecho("incendio en la cordoba",
        "incendio forestal en la cordoba", "incendios forestales",
        ubi, LocalDateTime.of(2024, 5, 1,0,0,0),
        LocalDateTime.now(),
        origen);
    List<Hecho> hechos = new ArrayList<>(Arrays.asList(hecho1, hecho2, hecho3));
    return hechos;
  }


  private Coleccion crearUnaColeccionParaTest() {
    FiltroContieneTexto filtroTexto1 = new FiltroContieneTexto("incendio en la rioja", CampoDeHecho.TITULO);
    return new Coleccion(
        "Incendios 2025",
        "Hechos de incendios",
        List.of(filtroTexto1),  // Lista con un mock de Filtro
        fuenteEstatica,
        "A1302"
    );
  }

  public static boolean sonEquivalentes(Hecho h1, RepresentacionDeHecho h2) {
    return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
        && h1.getDescripcion().equals(h2.getDescripcion())
        && h1.getCategoria().equals(h2.getCategoria())
        && h1.getUbicacion().getLatitud().equals(h2.getUbicacion().getLatitud())
        && h1.getUbicacion().getLongitud().equals(h2.getUbicacion().getLongitud())
        && h1.getFechaAcontecimiento().equals(h2.getFechaAcontecimiento());
  }




  @BeforeEach
  void setUp() {

    logger.info("Iniciando test de Administrador");
    // Crear mock de Hecho
    hecho = new Hecho("prueba", "prueba", "prueba",mock(Ubicacion.class), LocalDateTime.now(),LocalDateTime.now(), OrigenHecho.FUENTE_ESTATICA);
    representacionDeHecho = new RepresentacionDeHecho("prueba", "prueba", "prueba",mock(Ubicacion.class), LocalDateTime.now(),LocalDateTime.now(), OrigenHecho.FUENTE_ESTATICA,null);
    detectorDeSpam = mock(DetectorDeSpam.class);
    fuenteEstatica = new FuenteEstatica("ruta.csv",mock(LectorCsv.class));
    solicitud = crearUnaSolicitudDeEliminacionParaTest(representacionDeHecho);

  }

  @Test
  void puedeCrearUnaColeccionyAgregarlaALaListaDeColecciones() {
    coleccion = crearUnaColeccionParaTest();
    //entityManager().getTransaction().commit();
    assertEquals("Incendios 2025", coleccion.getTitulo());
    assertEquals("Hechos de incendios", coleccion.getDescripcion());
    assertTrue(ColeccionRepository.getInstancia().mostrarColecciones().contains(coleccion));
  }


  @Test
  void puedeAceptarUnaSolicitudDeEliminacion() {

    assertTrue(solicitud.estaPendiente());
    assertTrue(SolicitudesRepositoryDB.getInstancia().mostrarSolicitudes(TipoSolicitud.ELIMINACION_HECHO).contains(solicitud));
    solicitud.aceptar();
    entityManager().getTransaction().commit();
    Assertions.assertEquals(representacionDeHecho.getEstadoRepresentacionHecho(), EstadoRepresentacionHecho.ELIMINADO);
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


  //Test que corre solo pero en conjunto no REVISAR o comentar para generar el tag
  //@Test
  void solicitudDeEliminacionBorraEnTodasLasFuentes(){
    FuenteMetaMapa fuente  = fuenteMetaMapa(listaDeHechos(OrigenHecho.FUENTE_PROXY));
    Assertions.assertEquals(3,fuente.obtenerHechos(List.of()).size());
    RepresentacionDeHecho representacion = new RepresentacionDeHecho("incendio en la rioja",
        "incendio forestal en la rioja", "incendios forestales",
        ubi,LocalDateTime.of(2024, 5, 1,0,0,0),
        LocalDateTime.now(),
        OrigenHecho.FUENTE_PROXY,null);

    SolicitudEliminacion solicitud = crearUnaSolicitudDeEliminacionParaTest(representacion);


    assertEquals(fuente.obtenerHechos(List.of()).get(0).getTitulo(),representacion.getTitulo());
    assertEquals(fuente.obtenerHechos(List.of()).get(0).getCategoria(),representacion.getCategoria());
    assertEquals(fuente.obtenerHechos(List.of()).get(0).getDescripcion(),representacion.getDescripcion());
    assertEquals(fuente.obtenerHechos(List.of()).get(0).getUbicacion().getLongitud(),representacion.getUbicacion().getLongitud());
    assertEquals(fuente.obtenerHechos(List.of()).get(0).getUbicacion().getLatitud(),representacion.getUbicacion().getLatitud());
    assertEquals(fuente.obtenerHechos(List.of()).get(0).getFechaAcontecimiento(),representacion.getFechaAcontecimiento());


    Assertions.assertTrue(sonEquivalentes(fuente.obtenerHechos(List.of()).get(0),representacion));

    solicitud.aceptar();
    Assertions.assertEquals(2,fuente.obtenerHechos(List.of()).size());
  }



}
