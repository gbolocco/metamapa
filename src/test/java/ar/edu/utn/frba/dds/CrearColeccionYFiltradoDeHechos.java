package ar.edu.utn.frba.dds;

import static ar.edu.utn.frba.dds.TipoArchivo.CSV;
import ar.edu.utn.frba.dds.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.filtros.FiltroFecha;
import ar.edu.utn.frba.dds.usuarios.Administrador;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CrearColeccionYFiltradoDeHechos {

  //Creamos usuario administrador y el colection manager
  Administrador administrador = new Administrador("admin","admin");
  ColectionManager colectionManager= new ColectionManager();

  Fuente fuente = new Fuente(CSV,"desastres_naturales_argentina.csv");

  // Creamos una serie de filtros para el criterio de pertenencia y/o filtros
  FiltroContieneTexto filtroTexto1 = new FiltroContieneTexto("Buenos Aires", CampoDeHecho.TITULO);
  FiltroContieneTexto filtroTexto2 = new FiltroContieneTexto("nevada", CampoDeHecho.DESCRIPCION);
  LocalDate fecha1 =  LocalDate.of(2014,1,1);
  LocalDate fecha2 =  LocalDate.of(2014,1,30);
  FiltroFecha filtroFecha= new FiltroFecha(fecha1,fecha2);

  @Test
  void crearUnaColeccion(){

    //Agregamos filtros al criterio de pertenencia
    List<Filtro> criterioDePertenencia= new ArrayList<>();
    criterioDePertenencia.add(filtroFecha);

    //Admin crea una coleccion en base al criterio de pertenencia
    administrador.crearColeccion("Hechos de enero","hechos ocurridos en el mes de enero",fuente.getPathArchivo(),criterioDePertenencia,CSV);
    //Se importan los hechos desde la fuente
    administrador.importarDatosDeFuenteAColeccion("Hechos de enero");

    //Los mostramos por pantalla
    Coleccion coleccion= colectionManager.getColeccion("Hechos de enero");
    coleccion.visualizarHechos(null);
  }

  @Test
  void filtradoDeHechos(){

    // Misma coleccion anterior
    List<Filtro> criterioDePertenencia= new ArrayList<>();
    criterioDePertenencia.add(filtroFecha);
    administrador.crearColeccion("Hechos de enero","hechos ocurridos en el mes de enero",fuente.getPathArchivo(),criterioDePertenencia,CSV);
    administrador.importarDatosDeFuenteAColeccion("Hechos de enero");
    Coleccion coleccion= colectionManager.getColeccion("Hechos de enero");

    //Creamos lista de filtros y las mostramos por pantalla
    List<Filtro> filtros = new ArrayList<>();
    filtros.add(filtroTexto1);
    //filtros.add(filtroTexto2);

    coleccion.visualizarHechos(filtros);
  }
}
