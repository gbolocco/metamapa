package ar.edu.utn.frba.dds.usuarios;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFecha;
import ar.edu.utn.frba.dds.dominio.filtros.TipoCombinacion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.Lector;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VisualizadorTest {

  Lector lector;
  Coleccion coleccion;
  Filtro filtroTexto1;
  Filtro filtroTexto2;
  FiltroFecha filtroFecha;


  @BeforeEach
  void setUp() {

    lector = new LectorCsv();

    coleccion = new Coleccion(
        "Incendios 2025",
        "Hechos de incendios",
        List.of(),  // Lista vacía
        "datos/desastres_naturales_processed.csv",
        TipoCombinacion.AND,
        new LectorCsv()
    );

    coleccion.cargarHechosDesdeFuente();

    filtroTexto1 = new FiltroContieneTexto("Geophysical", CampoDeHecho.CATEGORIA);
    filtroTexto2 = new FiltroContieneTexto("Earthquake", CampoDeHecho.CATEGORIA);
    filtroFecha = new FiltroFecha(
        LocalDate.of(2024, 5, 1),
        LocalDate.of(2024, 5, 20),
        CampoDeHecho.FECHA_ACONTECIMIENTO
    );

  }

  @Test
  void visualizadorPuedeVerTodosLosHechosDeLaColeccion() {
    coleccion.imprimirColeccion( List.of(), TipoCombinacion.OR);
    assertFalse(coleccion.mostrarHechos().isEmpty());
  }

  @Test
  void visualizadorPuedeAplicarUnaListaDeFiltros() {

    List<Filtro> filtros = List.of(filtroTexto1, filtroTexto2, filtroFecha);

    List<Hecho> coleccionSinFiltrar = coleccion.mostrarHechos();

    List<Hecho> coleccionFiltrada = coleccion.filtrarHechos(filtros, TipoCombinacion.AND);

   assertTrue(coleccionSinFiltrar.size() > coleccionFiltrada.size());

  }
}
