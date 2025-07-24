package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
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
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class FuenteDinamicaTest {
  List<Filtro> filtros;
  Hecho hecho;
  SolicitudDeCargaHecho solicitud;
  FuenteDinamica fuente;
  Contribuyente contribuyente;
  Hecho hechoContribuyente;
  @BeforeEach
  void setUp() {
    hecho = mock(Hecho.class);
    solicitud = new SolicitudDeCargaHecho(hecho); // ya se carga en el repositorio por el constructor
    fuente = new FuenteDinamica();
    HechosRepositoryMemory.getInstancia().cargarHecho(hecho);
    filtros = new ArrayList<>();
  }

  public void crearSolicitudDeCarga(LocalDateTime fecha){
    contribuyente = new Contribuyente("juan", 21);
    hechoContribuyente = contribuyente.crearHecho(
        "incendio en la pampa",
        "incendio forestal en la pampa",
        "incendios forestales",
        mock(Ubicacion.class),
        mock(LocalDate.class),
        fecha);

    SolicitudDeCargaHecho solicitudContribuyente = new SolicitudDeCargaHecho(hechoContribuyente);
    solicitudContribuyente.aceptar();
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

    crearSolicitudDeCarga(LocalDateTime.now());
    Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente)); // se acepto correctamente y se agrego
    Assertions.assertTrue(contribuyente == hechoContribuyente.getOrigenHecho().getContribuyenteHecho());

  }

  @Test
  void contribuyenteRegistradoPuedeModificarHechoAFuenteDinamica() {
    crearSolicitudDeCarga(LocalDateTime.now());
    Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente));
    SolicitudModificacion solicitudModificacion = new SolicitudModificacion(hechoContribuyente,hecho,contribuyente);
    solicitudModificacion.aceptar();

    Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hecho));
    Assertions.assertFalse(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente));
  }

  @Test
  void contribuyenteNoPuedeModificarHechoAFuenteDinamicaFueCreadoHaceMasDeSieteDias() {
    crearSolicitudDeCarga(LocalDateTime.of(2025,5,20, 13,00,00));
    Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente));
    Assertions.assertThrows(UnsupportedOperationException.class, () -> new SolicitudModificacion(hechoContribuyente,hecho,contribuyente));
  }

  @Test
  void contribuyenteQuiereSolicitaModificarUnHechoQueNoEsSuyo(){
    crearSolicitudDeCarga(LocalDateTime.of(2025,5,20, 13,00,00));
    Contribuyente contribuyenteChorro = new Contribuyente("gian", 21);
    Assertions.assertThrows(UnsupportedOperationException.class, () -> new SolicitudModificacion(hechoContribuyente,hecho,contribuyenteChorro));
  }

  @Test
  void administradorPuedeAceptarConSugerenciaDeCambios(){
      Contribuyente contribuyente = new Contribuyente("juan", 21);
      Hecho hechoContribuyente = contribuyente.crearHecho(
          "incendio en la pampa",
          "incendio forestal en la pampa",
          "incendios forestales",
          mock(Ubicacion.class),
          mock(LocalDate.class),
          LocalDateTime.of(2025,5,20,13,00,00));

      SolicitudDeCargaHecho solicitudContribuyente = new SolicitudDeCargaHecho(hechoContribuyente);
      solicitudContribuyente.aceptarConSugerenciaDeCambio(hecho);

      Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hecho));
      Assertions.assertFalse(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente));
  }

}
