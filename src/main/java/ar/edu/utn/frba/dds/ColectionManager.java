package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.filtros.Filtro;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColectionManager {
  static List<Coleccion> colecciones;
  static List<Hecho> hechosEliminados;
  static List<SolicitudEliminacion> solicitudesEliminacionHechos;

  public ColectionManager() {
    this.colecciones = new ArrayList<>();
    this.hechosEliminados = new ArrayList<>();
    this.solicitudesEliminacionHechos = new ArrayList<>();
  }
  public static void crearColeccion(String nombre,String descripcion,List<Filtro> filtros,Fuente fuente){
    colecciones.add(new Coleccion(nombre,descripcion,filtros,fuente));
  }
  public static Coleccion getColeccion(String nombre) {
    return colecciones.stream()
        .filter(coleccion -> Objects.equals(nombre, coleccion.nombre()))
        .findFirst()
        .orElse(null); // lanzar excepción en el caso de que no este
  }
  public static void solicitudEliminacionHecho(String nombreHecho, String justificacion){
    if (buscarHechoPorNombreDentroDeColecciones(nombreHecho)==null){
      throw new IllegalArgumentException("No existe un hecho con el nombre: "+nombreHecho);
    }else {
      SolicitudEliminacion nuevaSolicitud = new SolicitudEliminacion(buscarHechoPorNombreDentroDeColecciones(nombreHecho), justificacion);
      solicitudesEliminacionHechos.add(nuevaSolicitud);
    }

  }
  public static Hecho buscarHechoPorNombreDentroDeColecciones(String nombreHecho) {
    for (Coleccion coleccion : colecciones) {
      for (Hecho hecho : coleccion.getHechos()) {
        if (Objects.equals(hecho.getTitulo(), nombreHecho)) {
          return hecho;
        }
      }
    }
    return null;
  }
  public static void fueAceptada(SolicitudEliminacion solicitudEliminacion) {
    solicitudesEliminacionHechos.remove(solicitudEliminacion);
    hechosEliminados.add(solicitudEliminacion.getHecho());
    System.out.println("Hecho eliminado: " + solicitudEliminacion.getHecho().getTitulo());
  }
  public static void fueRechazada(SolicitudEliminacion solicitudEliminacion) {
    solicitudesEliminacionHechos.remove(solicitudEliminacion);
    System.out.println("Solicitud de eliminacion rechazada: " + solicitudEliminacion.getHecho().getTitulo());
  }
  public static List<SolicitudEliminacion> getSolicitudesEliminacionHechos() {
    return solicitudesEliminacionHechos;
  }
}
