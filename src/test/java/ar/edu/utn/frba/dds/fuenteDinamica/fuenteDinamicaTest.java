package ar.edu.utn.frba.dds.fuenteDinamica;

import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeCargaHecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.dominio.usuario.Contribuyente;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class fuenteDinamicaTest {

  Hecho hecho;
  SolicitudDeCargaHecho solicitud;
  FuenteDinamica fuente;
  @BeforeEach
  void setUp() {
    hecho = mock(Hecho.class);
    solicitud = new SolicitudDeCargaHecho(hecho); // ya se carga en el repositorio por el constructor
    fuente = new FuenteDinamica();
    HechosRepositoryMemory.getInstancia().cargarHecho(hecho);
  }

  @Test
  void contribuyentePuedeGenerarUnaSolicitudCreacion(){

    SolicitudDeCargaHecho solicitudContribuyente = new SolicitudDeCargaHecho(hecho);
    Assertions.assertTrue(
        SolicitudesRepositoryMemory.getInstancia().mostrarSolicitudes(TipoSolicitud.CARGA_HECHO).contains(solicitudContribuyente));
  }


  @Test
  void administradorPuedeAceptarUnaSolicitudCreacion(){
    solicitud.aceptar();
    Assertions.assertTrue(solicitud.getEstadoSolicitud() == EstadoSolicitud.ACEPTADA);
  }
/*
  @Test
  void fuenteDinamicaPuedeCargarHechosDesdeFuente(){
    List<Hecho> lista = fuente.obtenerHechos();
    Assertions.assertFalse(lista.isEmpty());
  }*/

  @Test
  void contribuyenteRegistradoPuedeCargarHechoAFuenteDinamica(){

    Contribuyente contribuyente = new Contribuyente("juan", 21);
    Hecho hechoContribuyente = contribuyente.crearHecho("incendio en la pampa","incendio forestal en la pampa","incendios forestales", mock(Ubicacion.class), mock(LocalDate.class),mock(LocalDate.class));

    SolicitudDeCargaHecho solicitudContribuyente = contribuyente.generarSolicitudDeCreacion(hechoContribuyente);

    solicitudContribuyente.aceptar();

    Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente)); // se acepto correctamente y se agrego
    Assertions.assertTrue(contribuyente == hechoContribuyente.getOrigenHecho().getContribuyenteHecho());

  }

  @Test
  void contribuyenteRegistradoPuedeModificarHechoAFuenteDinamica() {
    Contribuyente contribuyente = new Contribuyente("juan", 21);
    Hecho hechoContribuyente = contribuyente.crearHecho("incendio en la pampa", "incendio forestal en la pampa", "incendios forestales", mock(Ubicacion.class), mock(LocalDate.class), LocalDate.now());
    SolicitudDeCargaHecho solicitudContribuyente = contribuyente.generarSolicitudDeCreacion(hechoContribuyente);

    solicitudContribuyente.aceptar();
    Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente));

    SolicitudModificacion solicitudModificacion = new SolicitudModificacion(hechoContribuyente,hecho,contribuyente);


    solicitudModificacion.aceptar();

    Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hecho));
    Assertions.assertFalse(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente));
  }

  @Test
  void contribuyenteNoPuedeModificarHechoAFuenteDinamicaFueCreadoHaceMasDeSieteDias() {
    Contribuyente contribuyente = new Contribuyente("juan", 21);
    Hecho hechoContribuyente = contribuyente.crearHecho("incendio en la pampa", "incendio forestal en la pampa", "incendios forestales", mock(Ubicacion.class), mock(LocalDate.class), LocalDate.of(2025,5,20));
    SolicitudDeCargaHecho solicitudContribuyente = contribuyente.generarSolicitudDeCreacion(hechoContribuyente);

    solicitudContribuyente.aceptar();
    Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente));

    Assertions.assertThrows(UnsupportedOperationException.class, () -> new SolicitudModificacion(hechoContribuyente,hecho,contribuyente));
  }

  @Test
  void contribuyenteQuiereSolicitaModificarUnHechoQueNoEsSuyo(){
    Contribuyente contribuyente1 = new Contribuyente("juan", 21);
    Contribuyente contribuyenteChorro = new Contribuyente("gian", 21);

    Hecho hechoContribuyente = contribuyente1.crearHecho("incendio en la pampa", "incendio forestal en la pampa", "incendios forestales", mock(Ubicacion.class), mock(LocalDate.class), LocalDate.of(2025,5,20));

    SolicitudDeCargaHecho solicitudContribuyente1 = contribuyente1.generarSolicitudDeCreacion(hechoContribuyente);

    solicitudContribuyente1.aceptar();

    Assertions.assertThrows(UnsupportedOperationException.class, () -> new SolicitudModificacion(hechoContribuyente,hecho,contribuyenteChorro));
  }
}
