package ar.edu.utn.frba.dds.usuarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.filtros.TipoCombinacion;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;


public class AdministradorTest {
  private Coleccion coleccion;
  @BeforeEach
  void setUp() {
    List<Filtro> filtros = List.of(
        new FiltroContieneTexto("Incendio", CampoDeHecho.CATEGORIA)
    );
    System.out.println(filtros);
    LectorCsv lectorMock = mock(LectorCsv.class);
    when(lectorMock.leer(any())).thenReturn(List.of());
    coleccion = new Coleccion(
        "Incendios 2025",
        "Hechos de incendios",
        filtros,
        "ruta.csv",
        TipoCombinacion.AND,
        lectorMock);
  }

  @Test
  void puedeCrearUnaColeccion() {
    assertEquals("Incendios 2025", coleccion.getTitulo());
    assertEquals("Hechos de incendios", coleccion.getDescripcion());
    assertTrue(coleccion.mostrarHechos().isEmpty());
  }

}
