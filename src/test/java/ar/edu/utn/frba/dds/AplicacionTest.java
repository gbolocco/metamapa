package ar.edu.utn.frba.dds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.filtros.FiltroFecha;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AplicacionTest {

  @Test
  public void navegarHechosPorColeccionSegunFiltros() {
    Fuente fuente = new Fuente(TipoArchivo.CSV,"desastres_naturales_argentina.CSV");
    LectorCSV lector= new LectorCSV();
    List<Hecho> hechos= new ArrayList<>();

    hechos =lector.leerHechosDesdeCSV(fuente.getPathArchivo());
    FiltroContieneTexto filtro1 = new FiltroContieneTexto("La Rioja", CampoDeHecho.TITULO);
    FiltroContieneTexto filtro2 = new FiltroContieneTexto("Vientos huracanados", CampoDeHecho.DESCRIPCION);
    LocalDate fecha1 =  LocalDate.of(2014,1,1);
    LocalDate fecha2 =  LocalDate.of(2015,1,1);
    FiltroFecha filtroFecha= new FiltroFecha(fecha1,fecha2);
    List<Filtro> criterioDePertenencia= new ArrayList<>();

    criterioDePertenencia.add(filtro1);
    criterioDePertenencia.add(filtroFecha);

    Coleccion coleccion= new Coleccion("Vientos","Devastador Vientos",criterioDePertenencia,fuente,hechos);
    coleccion.imprimirHechosFiltrados(criterioDePertenencia);
  }
}
