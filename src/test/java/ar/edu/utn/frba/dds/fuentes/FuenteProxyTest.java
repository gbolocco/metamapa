package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFechaDeCargaDesde;
import ar.edu.utn.frba.dds.dominio.fuentes.Conexion;
import ar.edu.utn.frba.dds.dominio.fuentes.FiltroUtils;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDemo;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteMetaMapaAdapter;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FuenteProxyTest implements SimplePersistenceTest {

  public Coleccion crearColeccionConFuenteProxy(Fuente fuente, String handle) {
    FiltroContieneTexto filtroTexto1 = new FiltroContieneTexto("incendio en la rioja",
        CampoDeHecho.TITULO);
    FiltroFechaDeCargaDesde filtroFechaDeCargaDesde = new FiltroFechaDeCargaDesde(
        LocalDateTime.of(2024, 5, 1,10,0,0),
        CampoDeHecho.FECHA_DE_CARGA);

    return new Coleccion(
        "Incendios 2025",
        "Hechos de incendios",
        List.of(filtroTexto1,filtroFechaDeCargaDesde),
        fuente,
        handle
    );

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
        mock(Ubicacion.class), mock(LocalDateTime.class),
        LocalDateTime.of(2024, 5, 1,0,0,0),
        origen);
    Hecho hecho2 = new Hecho("incendio en la rioja",
        "incendio forestal en la pampa", "incendios forestales",
        mock(Ubicacion.class), mock(LocalDateTime.class),
        LocalDateTime.of(2024, 5, 1,0,0,0),
        origen);
    Hecho hecho3 = new Hecho("incendio en la cordoba",
        "incendio forestal en la cordoba", "incendios forestales",
        mock(Ubicacion.class), mock(LocalDateTime.class),
        LocalDateTime.of(2024, 5, 1,0,0,0),
        origen);
    List<Hecho> hechos = new ArrayList<>(Arrays.asList(hecho1, hecho2, hecho3));
    return hechos;
  }



  @Test
  void seCargarCorrectamenteLosHechosDeUnaFuenteMetaMapaEnLaColeccion() {
    List<Hecho> listaDeHechos= listaDeHechos(OrigenHecho.FUENTE_PROXY);
    Fuente fuente= fuenteMetaMapa(listaDeHechos);
    Coleccion coleccion = crearColeccionConFuenteProxy(fuente, "A103");
    Assertions.assertEquals(3, coleccion.mostrarHechos().size());
  }

  @Test
  void seEnvianCorrectamenteLosFiltrosComoUnMapParaLaQuery() {
    List<Hecho> listaDeHechos= listaDeHechos(OrigenHecho.FUENTE_PROXY);
    Fuente fuente= fuenteMetaMapa(listaDeHechos);
    Coleccion coleccion = crearColeccionConFuenteProxy(fuente, "B102");
    Map<String, String> filtrosQuary = FiltroUtils
        .convertirfiltrosaMap(coleccion.getCriteriosDePertenencia());
    Assertions.assertEquals(2, filtrosQuary.size());

  }

  @Test
  void seCarganHechosAlaColeccionDeUnaFuenteDemoYSeFiltranPorFechaDeCargaMenorAUnaHora() {

    Conexion conexion = mock(Conexion.class);

    Map<String, Object> datosHecho1 = new HashMap<>();
    datosHecho1.put("titulo", "incendio en la rioja");
    datosHecho1.put("descripcion", "Otro Fuego activo en sector norte del parque");
    datosHecho1.put("categoria", "incendios");
    datosHecho1.put("latitud", 54.25);
    datosHecho1.put("longitud", -54.25);
    datosHecho1.put("fechaAcontecimiento", LocalDateTime.of(2025, 11, 15,0,0,0));
    datosHecho1.put("fechaDeCarga",  LocalDateTime.of(2024, 5, 1,10,30,0));

    Map<String, Object> datosHecho2 = new HashMap<>();
    datosHecho2.put("titulo", "incendio en la rioja");
    datosHecho2.put("descripcion", "Fuego activo en sector norte del parque");
    datosHecho2.put("categoria", "incendios");
    datosHecho2.put("latitud", 4.25);
    datosHecho2.put("longitud", -44.25);
    datosHecho2.put("fechaAcontecimiento", LocalDateTime.of(2023, 11, 15,0,0,0));
    datosHecho2.put("fechaDeCarga",  LocalDateTime.of(2024, 5, 1,10,30,0));

    //no pasa el filtro por la provincia
    Map<String, Object> datosHecho3 = new HashMap<>();
    datosHecho3.put("titulo", "incendio en la santa fe");
    datosHecho3.put("descripcion", "Fuego activo en sector norte del parque");
    datosHecho3.put("categoria", "incendios");
    datosHecho3.put("latitud", 4.25);
    datosHecho3.put("longitud", -44.25);
    datosHecho3.put("fechaAcontecimiento", LocalDateTime.of(2025, 11, 15,0,0,0));
    datosHecho3.put("fechaDeCarga", LocalDateTime.of(2024, 5, 1,9,30,0));

    //no pasa el filtro por la hora
    Map<String, Object> datosHecho4 = new HashMap<>();
    datosHecho4.put("titulo", "incendio en la rioja");
    datosHecho4.put("descripcion", "Fuego activo en sector norte del parque");
    datosHecho4.put("categoria", "incendios");
    datosHecho4.put("latitud", 4.25);
    datosHecho4.put("longitud", -44.25);
    datosHecho4.put("fechaAcontecimiento", LocalDateTime.of(2023, 11, 15,0,0,0));
    datosHecho4.put("fechaDeCarga",  LocalDateTime.of(2024, 5, 1,9,30,0));

    FuenteDemo fuente = new FuenteDemo(conexion);

    when(conexion.siguienteHecho(anyString(), any(LocalDateTime.class)))
        .thenReturn(datosHecho1).thenReturn(datosHecho2).thenReturn(datosHecho3).thenReturn(datosHecho4).thenReturn(null);

    fuente.incorporarNuevosHechosSiLosHay(LocalDateTime.now());
    Coleccion coleccion = crearColeccionConFuenteProxy(fuente, "C102");
    //coleccion.cargarHechos();
    //entityManager().getTransaction().commit();
    Assertions.assertEquals(2, coleccion.mostrarHechos().size());

    Assertions.assertEquals(datosHecho1.get("titulo"), coleccion.mostrarHechos().get(0).getTitulo());
    Assertions.assertEquals(datosHecho2.get("titulo"), coleccion.mostrarHechos().get(1).getTitulo());
  }



}
