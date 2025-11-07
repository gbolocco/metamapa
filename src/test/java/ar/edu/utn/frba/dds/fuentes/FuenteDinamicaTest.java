package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeCargaHecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.dominio.usuario.Contribuyente;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepository;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class FuenteDinamicaTest implements SimplePersistenceTest {
  List<Filtro> filtros;
  Hecho hecho;
  RepresentacionDeHecho representacionDeHecho;
  SolicitudDeCargaHecho solicitud;
  FuenteDinamica fuente;
  Contribuyente contribuyente;
  Hecho hechoContribuyente;
  RepresentacionDeHecho representacionDeHechoContribuyente;
  @BeforeEach
  void setUp() {
    representacionDeHecho = new RepresentacionDeHecho("prueba", "prueba", "prueba",mock(Ubicacion.class),LocalDateTime.now(),LocalDateTime.now(),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);

    hecho = new Hecho("prueba", "prueba", "prueba",mock(Ubicacion.class),LocalDateTime.now(),LocalDateTime.now(),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
    solicitud = new SolicitudDeCargaHecho(representacionDeHecho); // ya se carga en el repositorio por el constructor
    fuente = new FuenteDinamica();
    filtros = new ArrayList<>();
  }

  public  boolean sonEquivalentes(Hecho h1, RepresentacionDeHecho h2) {
    return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
        && h1.getDescripcion().equals(h2.getDescripcion())
        && h1.getCategoria().equals(h2.getCategoria())
        && h1.getUbicacion().getLatitud().equals(h2.getUbicacion().getLatitud())
        && h1.getUbicacion().getLongitud().equals(h2.getUbicacion().getLongitud())
        && h1.getFechaAcontecimiento().equals(h2.getFechaAcontecimiento());
  }

  public boolean cumpleCondicionDias(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
    long dias = Math.abs(ChronoUnit.DAYS.between(fechaInicial, fechaFinal));
    return dias >= 0 && dias <= 7;
  }

  public void crearSolicitudDeCarga(LocalDateTime fecha){
    contribuyente = new Contribuyente("juan", 21);
/*
    representacionDeHechoContribuyente = contribuyente.crearHecho(
        "incendio en la pampa",
        "incendio forestal en la pampa",
        "incendios forestales",
        mock(Ubicacion.class),
        mock(LocalDateTime.class),
        fecha);
    SolicitudDeCargaHecho solicitudContribuyente = new SolicitudDeCargaHecho(representacionDeHechoContribuyente);
    solicitudContribuyente.aceptar();

 */
  }

  @Test
  void contribuyentePuedeGenerarUnaSolicitudCreacion(){
    //entityManager().getTransaction().commit();
    Assertions.assertTrue(
        SolicitudesRepository.getInstancia().mostrarSolicitudes(TipoSolicitud.CARGA_HECHO).contains(solicitud));
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
    Assertions.assertFalse(HechosRepository.getInstancia().mostrarHechos().isEmpty());
    Assertions.assertEquals(representacionDeHechoContribuyente.getHecho().getContribuyente().getId(),contribuyente.getId());
  }

  @Test
  void puedeAceptarSolicitudDespuesDeApagar(){
    Solicitud solicitud1 = SolicitudesRepository.getInstancia().buscarSolicitudPorId(solicitud.getId());
    solicitud1.aceptar();
    //entityManager().getTransaction().commit();
    Assertions.assertTrue(solicitud1.getEstadoSolicitud() == EstadoSolicitud.ACEPTADA);
  }

  @Test
  void contribuyenteRegistradoPuedeModificarHechoAFuenteDinamica() {
    crearSolicitudDeCarga(LocalDateTime.now());
    hechoContribuyente = representacionDeHechoContribuyente.getHecho();
    Assertions.assertNotNull(hechoContribuyente.getId());

    SolicitudModificacion solicitudModificacion = new SolicitudModificacion(representacionDeHecho,hechoContribuyente.getId());
    solicitudModificacion.aceptar();
    Assertions.assertTrue(sonEquivalentes(HechosRepository.getInstancia().buscar(hechoContribuyente.getId()),representacionDeHecho));
  }

}

