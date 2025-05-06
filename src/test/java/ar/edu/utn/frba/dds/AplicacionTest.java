package ar.edu.utn.frba.dds;
import static ar.edu.utn.frba.dds.TipoArchivo.CSV;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

public class AplicacionTest {

  @Test
  public void navegarHechosPorColeccionSegunFiltros() {
    Administrador administrador = new Administrador("admin","admin");
    ColectionManager colectionManager= new ColectionManager();
    Fuente fuente = new Fuente(CSV,"desastres_naturales_argentina.csv");
    Contribuyente contribuyente = new Contribuyente("Juan","Luengo",20);
    List<Filtro> filtros = new ArrayList<>();


    FiltroContieneTexto filtro1 = new FiltroContieneTexto("La Rioja", CampoDeHecho.TITULO);
    FiltroContieneTexto filtro2 = new FiltroContieneTexto("Vientos huracanados", CampoDeHecho.DESCRIPCION);
    LocalDate fecha1 =  LocalDate.of(2014,1,1);
    LocalDate fecha2 =  LocalDate.of(2014,3,3);
    FiltroFecha filtroFecha= new FiltroFecha(fecha1,fecha2);
    List<Filtro> criterioDePertenencia= new ArrayList<>();

    criterioDePertenencia.add(filtro1);
    criterioDePertenencia.add(filtroFecha);
    //filtros.add(filtro1);
    administrador.crearColeccion("Vientos","Devastador Vientos",fuente.getPathArchivo(),criterioDePertenencia,CSV);
    administrador.importarDatosDeFuenteAColeccion("Vientos");
    Coleccion coleccion= colectionManager.getColeccion("Vientos");

    coleccion.imprimirHechosSegunFiltros(filtros);

    contribuyente.solicitarEliminacionHecho("Equipos de emergencia atienden Precipitación de material volcánico en La Rioja, La Rioja","abc");
    administrador.verSolicitudesEliminacion();
    administrador.aceptarSolicitudEliminacion("Equipos de emergencia atienden Precipitación de material volcánico en La Rioja, La Rioja");
    administrador.verSolicitudesEliminacion();

    coleccion.imprimirHechosSegunFiltros(filtros);


  }

  @Test
  public void testValidarJustificacionSolicitud() { // para probar validaciones
    Hecho hecho = new Hecho("Prueba", "Prueba", "Prueba", new Ubicacion(1.1,1.1),LocalDate.now(),LocalDate.now(),OriginHecho.FUENTE);

    String justificacionLarga = new String(new char[501]).replace('\0', 'a');

    assertThrows(IllegalArgumentException.class, () -> {
      new SolicitudEliminacion(hecho, justificacionLarga); // justificacion larga
    });
    assertThrows(IllegalArgumentException.class, () -> {
      new SolicitudEliminacion(hecho, null); // sin justificacion
    });
  }
}