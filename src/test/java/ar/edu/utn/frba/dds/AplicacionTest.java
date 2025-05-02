package ar.edu.utn.frba.dds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroContieneTexto;
import org.junit.jupiter.api.Test;

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

    List<Filtro> criterioDePertenencia= new ArrayList<>();

    criterioDePertenencia.add(filtro1);
    criterioDePertenencia.add(filtro2);


    Coleccion coleccion= new Coleccion("Vientos","Devastador Vientos",criterioDePertenencia,fuente,hechos);
    coleccion.imprimirHechosFiltrados(criterioDePertenencia);
  }
}
