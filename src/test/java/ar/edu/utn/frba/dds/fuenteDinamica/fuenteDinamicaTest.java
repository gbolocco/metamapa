package ar.edu.utn.frba.dds.fuenteDinamica;

import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeCargaHecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosYSolicitudesRepository;
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
    HechosYSolicitudesRepository.getInstancia().cargarHecho(hecho);
  }

  @Test
  void contribuyentePuedeGenerarUnaSolicitud(){
    SolicitudDeCargaHecho solicitudContribuyente = new SolicitudDeCargaHecho(hecho);
    Assertions.assertTrue(
        HechosYSolicitudesRepository.getInstancia().mostrarSolicitudes().contains(solicitudContribuyente));
  }


  @Test
  void administradorPuedeAceptarUnaSolicitud(){
    solicitud.aceptar();
    Assertions.assertFalse(HechosYSolicitudesRepository.getInstancia().mostrarSolicitudes().contains(solicitud));
  }

  @Test
  void fuenteDinamicaPuedeCargarHechosDesdeFuente(){
    List<Hecho> lista = fuente.cargarHechos();
    Assertions.assertFalse(lista.isEmpty());
  }



}
