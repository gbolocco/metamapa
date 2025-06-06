package ar.edu.utn.frba.dds.fuenteProxy;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.dominio.fuentes.Conexion;
import ar.edu.utn.frba.dds.dominio.fuentes.FiltroUtils;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDemo;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteMetaMapaAdapter;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FuenteProxyTest {

  public Coleccion crearColeccionConFuenteMetaMapa(Fuente fuente) throws IOException {
    FiltroContieneTexto filtroTexto1 = new FiltroContieneTexto("incendio en la rioja",
        CampoDeHecho.TITULO);
    FiltroFechaHasta filtroFechaHasta = new FiltroFechaHasta(
        LocalDate.of(2024, 5, 1), CampoDeHecho.FECHA_ACONTECIMIENTO);


    return new Coleccion(
        "Incendios 2025",
        "Hechos de incendios",
        List.of(filtroTexto1, filtroFechaHasta),
        fuente
    );
  }

  public FuenteMetaMapa fuenteMetaMapa(List<Hecho> hechos) throws IOException {
    FuenteMetaMapaAdapter adapter;
    adapter = mock(FuenteMetaMapaAdapter.class);
    when(adapter.obtenerHechos(anyMap())).thenReturn(hechos);

    return new FuenteMetaMapa(adapter);
  }

  public List<Hecho> listaDeHechos(OrigenHecho origen) {
    Hecho hecho1 = new Hecho("incendio en la rioja",
        "incendio forestal en la rioja", "incendios forestales",
        mock(Ubicacion.class), mock(LocalDate.class),
        LocalDate.of(2024, 10, 10),
        origen);
    Hecho hecho2 = new Hecho("incendio en la pampa",
        "incendio forestal en la pampa", "incendios forestales",
        mock(Ubicacion.class), mock(LocalDate.class),
        LocalDate.of(2025, 2, 10),
        origen);
    Hecho hecho3 = new Hecho("incendio en la cordoba",
        "incendio forestal en la cordoba", "incendios forestales",
        mock(Ubicacion.class), mock(LocalDate.class),
        LocalDate.of(2023, 5, 2),
        origen);
    List<Hecho> hechos = new ArrayList<>(Arrays.asList(hecho1, hecho2, hecho3));
    return hechos;
  }


  @Test
  void seCargarCorrectamenteLosHechosDeUnaFuenteMetaMapaEnLaColeccion() throws IOException {

    List<Hecho> listaDeHechos= listaDeHechos(OrigenHecho.FUENTE_PROXY);
    Fuente fuente= fuenteMetaMapa(listaDeHechos);
    Coleccion coleccion = crearColeccionConFuenteMetaMapa(fuente);
    coleccion.cargarHechos();
    Assertions.assertEquals(3, coleccion.getHechos().size());

  }

  @Test
  void seEnvianCorrectamenteLosFiltrosComoUnMapParaLaQuary() throws IOException {
    List<Hecho> listaDeHechos= listaDeHechos(OrigenHecho.FUENTE_PROXY);
    Fuente fuente= fuenteMetaMapa(listaDeHechos);
    Coleccion coleccion = crearColeccionConFuenteMetaMapa(fuente);

    Map<String, String> filtrosQuary = FiltroUtils
        .convertirfiltrosaMap(coleccion.getCriteriosDePertenencia());
    System.out.println(filtrosQuary);
    Assertions.assertEquals(2, filtrosQuary.size());
  }

  @Test
  void fuenteDemo() throws IOException {
    List<Hecho> listaDeHechos= listaDeHechos(OrigenHecho.FUENTE_PROXY);
    Conexion conexion = mock(Conexion.class);

    Map<String, Object> datosHecho = new HashMap<>();
    datosHecho.put("titulo", "incendio en la rioja");
    datosHecho.put("descripcion", "Fuego activo en sector norte del parque");
    datosHecho.put("categoria", "incendios");
    datosHecho.put("latitud", 54.25);
    datosHecho.put("longitud", -54.25);
    datosHecho.put("fechaAcontecimiento", LocalDate.of(2023, 11, 15));
    datosHecho.put("fechaDeCarga", LocalDate.of(2023, 11, 16));

    FuenteDemo fuente = new FuenteDemo("URL", conexion);

    Coleccion coleccion = crearColeccionConFuenteMetaMapa(fuente);
    when(conexion.siguienteHecho(anyString(), any(LocalDate.class))).thenReturn(datosHecho).thenReturn(null);

    conexion.siguienteHecho("hola",LocalDate.of(2023, 11, 16));

    /*
    System.out.println(datosHecho);
    Hecho hecho= fuente.crearHechoDesdeMap(datosHecho);
    System.out.println(hecho.getTitulo());
    HechosRepositoryMemory.getInstancia().cargarHecho(hecho);
    System.out.println(HechosRepositoryMemory.getInstancia().mostrarHechos().size());
*/

    fuente.incorporarNuevosHechosSiLosHay(LocalDate.of(2023, 11, 16));
    coleccion.getHechos();
    Assertions.assertEquals(1, coleccion.getHechos().size());

  }



}
