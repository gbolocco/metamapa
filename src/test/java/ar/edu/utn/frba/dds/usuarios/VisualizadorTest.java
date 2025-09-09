package ar.edu.utn.frba.dds.usuarios;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.dominio.filtros.TipoCombinacion;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.lectores.Lector;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VisualizadorTest {

  Lector lector;
  Coleccion coleccion;
  Filtro filtroTexto1;
  Filtro filtroTexto2;
  FiltroFechaHasta filtroFechaHasta;


  @BeforeEach
  void setUp() throws IOException {

    lector = new LectorCsv();
    FuenteEstatica fuente = new FuenteEstatica("datos/desastres_naturales_first_8 (1).csv", lector);
    coleccion = new Coleccion(
        "Incendios 2025",
        "Hechos de incendios",
        List.of(),  // Lista vacía
        fuente,
            "A1302"
    );

    filtroTexto1 = new FiltroContieneTexto("Geophysical", CampoDeHecho.CATEGORIA);
    filtroTexto2 = new FiltroContieneTexto("Earthquake", CampoDeHecho.CATEGORIA);
    filtroFechaHasta = new FiltroFechaHasta(
        LocalDate.of(2024, 5, 1),
        CampoDeHecho.FECHA_ACONTECIMIENTO
    );
  }

  /*@Test
  void visualizadorPuedeVerTodosLosHechosDeLaColeccion() {

    coleccion.imprimirColeccion( List.of(), TipoCombinacion.OR);
    assertFalse(coleccion.mostrarHechos().isEmpty());
  }
  */
  /*@Test
  void visualizadorPuedeAplicarUnaListaDeFiltros() {
    coleccion.setHandle("A1303");
    List<Filtro> filtros = List.of(filtroTexto1, filtroTexto2, filtroFechaHasta);

    List<Hecho> coleccionSinFiltrar = coleccion.mostrarHechos();

    List<Hecho> coleccionFiltrada = coleccion.filtrarHechos(filtros, TipoCombinacion.AND);

   assertTrue(coleccionSinFiltrar.size() > coleccionFiltrada.size());

  }*/
}
