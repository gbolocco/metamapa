package ar.edu.utn.frba.dds.algoritmos;

import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso.Absoluta;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso.MayoriaSimple;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteMetaMapaAdapter;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlgoritmosTest implements SimplePersistenceTest {

  public FuenteEstatica fuenteEstatica(List<Hecho> hechos) {
    LectorCsv lectorCsv;
    lectorCsv = mock(LectorCsv.class);
    when(lectorCsv.leer(anyString())).thenReturn(hechos);
    return new FuenteEstatica("ruta.csv", lectorCsv);
  }

  public FuenteMetaMapa fuenteMetaMapa(List<Hecho> hechos) {
    FuenteMetaMapaAdapter adapter;
    adapter = mock(FuenteMetaMapaAdapter.class);
    when(adapter.obtenerHechos(anyMap())).thenReturn(hechos);
    return new FuenteMetaMapa(adapter);
  }



    Hecho hecho1 = new Hecho("incendio en la rioja",
        "incendio forestal en la rioja", "incendios forestales",
        new Ubicacion(-34.6591644, -58.4694862), LocalDateTime.of(2020, 4, 1,0,0,0),
        LocalDateTime.of(2024, 5, 1, 9, 59, 0),
        mock(OrigenHecho.class));
    Hecho hecho2 = new Hecho("incendio en la rioja",
        "incendio forestal en la pampa", "incendios forestales",
        new Ubicacion(-34.5984145, -58.4222096), LocalDateTime.of(2020, 4, 2,0,0,0),
        LocalDateTime.of(2024, 5, 1, 9, 59, 0),
        mock(OrigenHecho.class));

    Hecho hecho3 = new Hecho("incendio en la cordoba",
        "incendio forestal en la cordoba", "incendios forestales",
        new Ubicacion(-34.6591644, -58.4694862), LocalDateTime.of(2020, 4, 3,0,0,0),
        LocalDateTime.of(2024, 5, 1, 13, 0, 0),
        mock(OrigenHecho.class));
    Hecho hecho4 = new Hecho("inundación en Rosario",
        "el desborde del río provocó inundaciones en varios barrios",
        "desastres naturales",
        new Ubicacion(-32.9442, -60.6505),
        LocalDateTime.of(2023, 11, 12,0,0,0),
        LocalDateTime.of(2023, 11, 12, 14, 30, 0),
        mock(OrigenHecho.class));

    Hecho hecho5 = new Hecho("protesta docente en Mendoza",
        "docentes marcharon por mejoras salariales en el centro de Mendoza",
        "manifestaciones sociales",
        new Ubicacion(-32.8908, -68.8272),
        LocalDateTime.of(2024, 3, 7,0,0,0),
        LocalDateTime.of(2024, 3, 7, 10, 0, 0),
        mock(OrigenHecho.class));

    Hecho hecho6 = new Hecho("accidente ferroviario en Buenos Aires",
        "una formación del tren Mitre colisionó con un auto en un paso a nivel",
        "accidentes de transporte",
        new Ubicacion(-34.6037, -58.3816),
        LocalDateTime.of(2024, 6, 20,0,0),
        LocalDateTime.of(2024, 6, 20, 8, 15, 0),
        mock(OrigenHecho.class));

    List<Hecho> hechosColeccion = List.of(hecho1, hecho2, hecho3, hecho4, hecho5, hecho6);




    FiltroContieneTexto filtroTexto1 = new FiltroContieneTexto("incendios forestales", CampoDeHecho.CATEGORIA);
    List <Filtro> filtrosCategoria = List.of(filtroTexto1);
    FiltroContieneTexto filtroTexto2 = new FiltroContieneTexto("incendio en la cordoba", CampoDeHecho.TITULO);
    List <Filtro> filtroTitulo = List.of(filtroTexto2);


  @Test
  public void algoritmoAbsoluta(){
    FuenteEstatica fuenteEstatica = fuenteEstatica(new ArrayList<>(Arrays.asList(hecho1,hecho2,hecho3)));
    FuenteMetaMapa fuenteMetaMapa = fuenteMetaMapa(List.of(hecho1,hecho2));

    Absoluta algoritmo = new Absoluta();
    List<Hecho> hechosConsensuados = algoritmo.hechosConsensuados(hechosColeccion,filtrosCategoria);

    Assertions.assertEquals(2, FuentesRepository.getInstancia().getFuentes().size());

    assertEquals(2, hechosConsensuados.size());

    assertTrue(hechosConsensuados.contains(hecho1));
    assertTrue(hechosConsensuados.contains(hecho2));

    Assertions.assertEquals(3,fuenteEstatica.obtenerHechos(filtrosCategoria).size());
    Assertions.assertEquals(2,fuenteMetaMapa.obtenerHechos(filtrosCategoria).size());

  }

  @Test
  public void noConsensuaAbsolutamente(){
    FuenteEstatica fuenteEstatica1 = fuenteEstatica(new ArrayList<>(Arrays.asList(hecho1,hecho2,hecho3)));
    FuenteEstatica fuenteEstatica2 = fuenteEstatica(new ArrayList<>(Arrays.asList(hecho4,hecho5,hecho6)));
    FuenteMetaMapa fuenteMetaMapa = fuenteMetaMapa(List.of(hecho1,hecho2));

    //entityManager().getTransaction().begin();

    entityManager().getTransaction().commit();
    Absoluta algoritmo = new Absoluta();

    List<Hecho> hechosConsensuados = algoritmo.hechosConsensuados(hechosColeccion,filtrosCategoria);

    Assertions.assertEquals(3, FuentesRepository.getInstancia().getFuentes().size());

    assertEquals(0, hechosConsensuados.size());
  }

  @Test
  public void consensuaMayoritariamente(){
    FuenteEstatica fuenteEstatica1 = fuenteEstatica(new ArrayList<>(Arrays.asList(hecho1,hecho2,hecho3)));
    FuenteEstatica fuenteEstatica2 = fuenteEstatica(new ArrayList<>(Arrays.asList(hecho1,hecho5,hecho6)));
    FuenteMetaMapa fuenteMetaMapa = fuenteMetaMapa(List.of(hecho6,hecho4));

    MayoriaSimple algoritmo = new MayoriaSimple();

    List<Hecho> hechosConsensuados = algoritmo.hechosConsensuados(hechosColeccion,List.of());


    Assertions.assertEquals(3, FuentesRepository.getInstancia().getFuentes().size());
    Assertions.assertEquals(2,algoritmo.cuantasVecesAparece(hecho1, FuentesRepository.getInstancia().obtenerHechosDeFuentes(new ArrayList<>())));

    assertTrue(algoritmo.cuantasVecesAparece(hecho1, FuentesRepository.getInstancia().obtenerHechosDeFuentes(new ArrayList<>())) >= (FuentesRepository.getInstancia().getFuentes().size() / 2));
    assertTrue(algoritmo.cuantasVecesAparece(hecho6, FuentesRepository.getInstancia().obtenerHechosDeFuentes(new ArrayList<>())) >= (FuentesRepository.getInstancia().getFuentes().size() / 2));

    assertTrue(algoritmo.estaConsensuado(hecho1, FuentesRepository.getInstancia().obtenerHechosDeFuentes(new ArrayList<>())));
    assertTrue(algoritmo.estaConsensuado(hecho6, FuentesRepository.getInstancia().obtenerHechosDeFuentes(new ArrayList<>())));

    assertEquals(2, hechosConsensuados.size());

    assertTrue(hechosConsensuados.contains(hecho1));
    assertTrue(hechosConsensuados.contains(hecho6));
  }


  @Test
  public void NoConsensuaMayoritariamente(){
    FuenteEstatica fuenteEstatica1 = fuenteEstatica(new ArrayList<>(Arrays.asList(hecho1,hecho2,hecho3)));
    FuenteEstatica fuenteEstatica2 = fuenteEstatica(new ArrayList<>(Arrays.asList(hecho1,hecho5,hecho6)));
    FuenteEstatica fuenteEstatica3 = fuenteEstatica(List.of());
    FuenteEstatica fuenteEstatica4 = fuenteEstatica(List.of());
    FuenteMetaMapa fuenteMetaMapa = fuenteMetaMapa(List.of(hecho6,hecho4));

    MayoriaSimple algoritmo = new MayoriaSimple();

    List<Hecho> hechosConsensuados = algoritmo.hechosConsensuados(hechosColeccion,List.of());


    Assertions.assertEquals(5, FuentesRepository.getInstancia().getFuentes().size());
    assertEquals(0, hechosConsensuados.size());
  }


}
