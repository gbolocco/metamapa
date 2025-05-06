package ar.edu.utn.frba.dds;

import static ar.edu.utn.frba.dds.TipoArchivo.CSV;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.filtros.FiltroFecha;
import ar.edu.utn.frba.dds.usuarios.Administrador;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SolicitudesDeEliminacion {

  Administrador administrador = new Administrador("admin","admin");
  ColectionManager colectionManager= new ColectionManager();

  Fuente fuente = new Fuente(CSV,"desastres_naturales_argentina.csv");

  FiltroContieneTexto filtroTexto1 = new FiltroContieneTexto("Buenos Aires", CampoDeHecho.TITULO);
  FiltroContieneTexto filtroTexto2 = new FiltroContieneTexto("nevada", CampoDeHecho.DESCRIPCION);
  LocalDate fecha1 =  LocalDate.of(2014,1,1);
  LocalDate fecha2 =  LocalDate.of(2014,3,30);
  FiltroFecha filtroFecha= new FiltroFecha(fecha1,fecha2);



  @Test
  void solicitarEliminacionDeUnHecho(){

    //Creamos una coleccion
    List<Filtro> criterioDePertenencia= new ArrayList<>();
    criterioDePertenencia.add(filtroTexto2);
    criterioDePertenencia.add(filtroFecha);
    administrador.crearColeccion("Hechos de enero","hechos ocurridos en el mes de enero",fuente.getPathArchivo(),criterioDePertenencia,CSV);
    administrador.importarDatosDeFuenteAColeccion("Hechos de enero");
    Coleccion coleccion = colectionManager.getColeccion("Hechos de enero");

    //Coleccion antes de eliminar un hecho
    coleccion.visualizarHechos(null);

    //Creamos un usuario contribuyente
    Contribuyente contribuyente = new Contribuyente("Juan","Luengo",20);

    contribuyente.solicitarEliminacionHecho("Nevada histórica deja múltiples daños en Colón","Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa.");
    administrador.verSolicitudesEliminacion();
    administrador.aceptarSolicitudEliminacion("Nevada histórica deja múltiples daños en Colón");
    administrador.verSolicitudesEliminacion();

    //Verificamos que el hecho se encuentra en la lista de hechos eliminados
    colectionManager.visualizarHechosEliminados();

    //Verificamos que el hecho no se muestra cuando invocamos a la coleccion
    coleccion.visualizarHechos(null);
  }

}
