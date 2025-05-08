package ar.edu.utn.frba.dds.colecciones;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hecho.Hecho;
import ar.edu.utn.frba.dds.lectores.Fuente;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
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

  public static void crearColeccion(
      String nombre,
      String descripcion,
      List<Filtro> filtros,
      Fuente fuente
  ) {
    colecciones.add(new Coleccion(nombre, descripcion, filtros, fuente));
  }

  public static Coleccion getColeccion(String nombre) {
    return colecciones.stream()
        .filter(coleccion -> Objects.equals(nombre, coleccion.nombre()))
        .findFirst()
        .orElse(null); // lanzar excepción en el caso de que no este
  }

  public static void solicitarEliminacionHecho(String nombreHecho, String justificacion) {
    if (buscarHechoPorNombre(nombreHecho) == null) {
      throw new IllegalArgumentException("No existe un hecho con el nombre: " + nombreHecho);
    } else {
      SolicitudEliminacion nuevaSolicitud = new SolicitudEliminacion(
          buscarHechoPorNombre(nombreHecho),
          justificacion
      );
      solicitudesEliminacionHechos.add(nuevaSolicitud);
    }
  }

  public static Hecho buscarHechoPorNombre(String nombreHecho) {
    return colecciones.stream()
        .flatMap(coleccion -> coleccion.getHechos().stream())
        .filter(hecho -> Objects.equals(hecho.getTitulo(), nombreHecho))
        .findFirst()
        .orElse(null);
  }

  public static void fueAceptada(SolicitudEliminacion solicitudEliminacion) {
    Hecho hechoEliminado = solicitudEliminacion.getHecho();
    hechoEliminado.eliminarHecho();
    solicitudesEliminacionHechos.remove(solicitudEliminacion);
    hechosEliminados.add(hechoEliminado);
    System.out.println("Hecho eliminado: " + solicitudEliminacion.getHecho().getTitulo());
  }

  public static void fueRechazada(SolicitudEliminacion solicitudEliminacion) {
    solicitudesEliminacionHechos.remove(solicitudEliminacion);
    System.out.println("Solicitud de eliminacion rechazada: "
        + solicitudEliminacion.getHecho().getTitulo());
  }

  public static List<SolicitudEliminacion> getSolicitudesEliminacionHechos() {
    return solicitudesEliminacionHechos;
  }

  public void visualizarHechosEliminados() {
    System.out.println("=== Hechos Eliminados ===");
    hechosEliminados.forEach(Hecho::imprimirHecho);
    System.out.println("=========================");
  }
}
