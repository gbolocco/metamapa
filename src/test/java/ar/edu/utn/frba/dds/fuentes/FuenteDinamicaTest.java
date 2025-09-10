package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeCargaHecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.dominio.usuario.Contribuyente;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class FuenteDinamicaTest implements SimplePersistenceTest {
  List<Filtro> filtros;
  Hecho hecho;
  SolicitudDeCargaHecho solicitud;
  FuenteDinamica fuente;
  Contribuyente contribuyente;
  Hecho hechoContribuyente;
  @BeforeEach
  void setUp() {
    hecho = new Hecho("prueba", "prueba", "prueba",mock(Ubicacion.class),LocalDateTime.now(),LocalDateTime.now(),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
    solicitud = new SolicitudDeCargaHecho(hecho); // ya se carga en el repositorio por el constructor
    fuente = new FuenteDinamica();
    filtros = new ArrayList<>();
  }

  public void crearSolicitudDeCarga(LocalDateTime fecha){
    contribuyente = new Contribuyente("juan", 21);
    hechoContribuyente = contribuyente.crearHecho(
        "incendio en la pampa",
        "incendio forestal en la pampa",
        "incendios forestales",
        mock(Ubicacion.class),
        mock(LocalDateTime.class),
        fecha);
    SolicitudDeCargaHecho solicitudContribuyente = new SolicitudDeCargaHecho(hechoContribuyente);
    solicitudContribuyente.aceptar();
  }

  @Test
  void contribuyentePuedeGenerarUnaSolicitudCreacion(){
    //entityManager().getTransaction().commit();
    Assertions.assertTrue(
        SolicitudesRepositoryMemory.getInstancia().mostrarSolicitudes(TipoSolicitud.CARGA_HECHO).contains(solicitud));
  }

  @Test
  void administradorPuedeAceptarUnaSolicitudCreacion(){
    solicitud.aceptar();
    //entityManager().getTransaction().commit();
    Assertions.assertTrue(solicitud.getEstadoSolicitud() == EstadoSolicitud.ACEPTADA);

  }


  @Test
  void contribuyenteRegistradoPuedeCargarHechoAFuenteDinamica(){
    crearSolicitudDeCarga(LocalDateTime.now());
    //entityManager().getTransaction().commit();
    Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente)); // se acepto correctamente y se agrego
    Assertions.assertTrue(contribuyente == hechoContribuyente.getOrigenHecho().getContribuyenteHecho());

  }

  @Test
  void puedeAceptarSolicitudDespuesDeApagar(){
    Solicitud solicitud1 = SolicitudesRepositoryMemory.getInstancia().buscarSolicitudPorId(solicitud.getId());
    solicitud1.aceptar();
    //entityManager().getTransaction().commit();
    Assertions.assertTrue(solicitud1.getEstadoSolicitud() == EstadoSolicitud.ACEPTADA);
  }

  @Test
  void contribuyenteRegistradoPuedeModificarHechoAFuenteDinamica() {
    crearSolicitudDeCarga(LocalDateTime.now());
    Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente));
    Assertions.assertNotEquals(hechoContribuyente.getTitulo(),hecho.getTitulo());
    SolicitudModificacion solicitudModificacion = new SolicitudModificacion(hecho,hechoContribuyente,contribuyente);
    solicitudModificacion.aceptar();

    //entityManager().getTransaction().commit();
    Assertions.assertEquals(hechoContribuyente.getTitulo(),hecho.getTitulo());
  }

  @Test
  void contribuyenteNoPuedeModificarHechoAFuenteDinamicaFueCreadoHaceMasDeSieteDias() {
    crearSolicitudDeCarga(LocalDateTime.of(2025,5,20,0,0,0));
    Assertions.assertTrue(HechosRepositoryMemory.getInstancia().mostrarHechos().contains(hechoContribuyente));
    Assertions.assertThrows(UnsupportedOperationException.class, () -> new SolicitudModificacion(hechoContribuyente,hecho,contribuyente));
  }

  @Test
  void contribuyenteQuiereSolicitaModificarUnHechoQueNoEsSuyo(){
    crearSolicitudDeCarga(LocalDateTime.of(2025,5,20,0,0,0));
    Contribuyente contribuyenteChorro = new Contribuyente("gian", 21);
    Assertions.assertThrows(UnsupportedOperationException.class, () -> new SolicitudModificacion(hechoContribuyente,hecho,contribuyenteChorro));
  }

}
