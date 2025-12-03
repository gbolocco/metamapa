package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;

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

  FuenteDinamica fuente;
  Usuario usuario;
  Hecho hechoContribuyente;
  RepresentacionDeHecho representacionDeHechoContribuyente;

  @BeforeEach
  void setUp() {
    representacionDeHecho = new RepresentacionDeHecho("prueba", "prueba", "prueba", mock(Ubicacion.class),
        LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);

    hecho = new Hecho("prueba", "prueba", "prueba", mock(Ubicacion.class), LocalDateTime.now(), LocalDateTime.now(),
        OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);

    fuente = new FuenteDinamica();
    filtros = new ArrayList<>();
  }

  public boolean sonEquivalentes(Hecho h1, RepresentacionDeHecho h2) {
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

  public void crearHechoDirectamente(LocalDateTime fecha) {
    // Simular creación directa de hecho
    hechoContribuyente = new Hecho(
        "incendio en la pampa",
        "incendio forestal en la pampa",
        "incendios forestales",
        mock(Ubicacion.class),
        fecha,
        LocalDateTime.now(),
        OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
    hechoContribuyente.setUsuario(usuario);
    HechosRepository.getInstancia().cargarHecho(hechoContribuyente);

    representacionDeHechoContribuyente = new RepresentacionDeHecho(
        hechoContribuyente.getTitulo(),
        hechoContribuyente.getDescripcion(),
        hechoContribuyente.getCategoria(),
        hechoContribuyente.getUbicacion(),
        hechoContribuyente.getFechaAcontecimiento(),
        hechoContribuyente.getFechaDeCarga(),
        hechoContribuyente.getOrigenHecho());
  }

  @Test
  void contribuyentePuedeCargarHechoDirectamente() {
    crearHechoDirectamente(LocalDateTime.now());
    Assertions.assertFalse(HechosRepository.getInstancia().mostrarHechos().isEmpty());
  }

  @Test
  void contribuyenteRegistradoPuedeCargarHechoAFuenteDinamica() {
    crearHechoDirectamente(LocalDateTime.now());
    Assertions.assertFalse(HechosRepository.getInstancia().mostrarHechos().isEmpty());
    Assertions.assertEquals(hechoContribuyente.getUsuario(), usuario);
  }

  @Test
  void contribuyenteRegistradoPuedeModificarHechoAFuenteDinamica() {
    crearHechoDirectamente(LocalDateTime.now());
    hechoContribuyente = representacionDeHechoContribuyente.getHecho();
    Assertions.assertNotNull(hechoContribuyente.getId());

    SolicitudModificacion solicitudModificacion = new SolicitudModificacion(representacionDeHecho,
        hechoContribuyente.getId());
    solicitudModificacion.aceptar();
    Assertions.assertTrue(
        sonEquivalentes(HechosRepository.getInstancia().buscar(hechoContribuyente.getId()), representacionDeHecho));
  }

}
