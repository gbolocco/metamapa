package ar.edu.utn.frba.dds.colecciones;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hecho.Hecho;
import ar.edu.utn.frba.dds.lectores.Fuente;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColectionManager {

  // Se inicializan los campos estáticos de forma estática
  private static final List<Coleccion> COLECCIONES = new ArrayList<>();
  private static final List<Hecho> HECHOS_ELIMINADOS = new ArrayList<>();
  private static final List<SolicitudEliminacion>
      SOLICITUDES_ELIMINACION_HECHOS = new ArrayList<>();

  // Constructor vacío
  public ColectionManager() {
    // No es necesario inicializar colecciones estáticas aquí
  }

  // Método para crear una nueva colección
  public static void crearColeccion(
      String nombre,
      String descripcion,
      List<Filtro> filtros,
      Fuente fuente
  ) {
    COLECCIONES.add(new Coleccion(nombre, descripcion, filtros, fuente));
  }

  // Obtener colección por nombre
  public static Coleccion getColeccion(String nombre) {
    return COLECCIONES.stream()
        .filter(coleccion -> Objects.equals(nombre, coleccion.nombre()))
        .findFirst()
        .orElse(null); // Podrías lanzar una excepción aquí si es necesario
  }

  // Solicitar eliminación de un hecho
  public static void solicitarEliminacionHecho(String nombreHecho, String justificacion) {
    Hecho hecho = buscarHechoPorNombre(nombreHecho);
    if (hecho == null) {
      throw new IllegalArgumentException("No existe un hecho con el nombre: " + nombreHecho);
    }
    SolicitudEliminacion nuevaSolicitud = new SolicitudEliminacion(hecho, justificacion);
    SOLICITUDES_ELIMINACION_HECHOS.add(nuevaSolicitud);
  }

  // Buscar hecho por nombre
  public static Hecho buscarHechoPorNombre(String nombreHecho) {
    return COLECCIONES.stream()
        .flatMap(coleccion -> coleccion.getHechos().stream())
        .filter(hecho -> Objects.equals(hecho.getTitulo(), nombreHecho))
        .findFirst()
        .orElse(null);
  }

  // Aceptar la solicitud de eliminación
  public static void fueAceptada(SolicitudEliminacion solicitudEliminacion) {
    Hecho hechoEliminado = solicitudEliminacion.getHecho();
    hechoEliminado.eliminarHecho();
    SOLICITUDES_ELIMINACION_HECHOS.remove(solicitudEliminacion);
    HECHOS_ELIMINADOS.add(hechoEliminado);
    System.out.println("Hecho eliminado: " + solicitudEliminacion
        .getHecho().getTitulo());
  }

  // Rechazar la solicitud de eliminación
  public static void fueRechazada(SolicitudEliminacion solicitudEliminacion) {
    SOLICITUDES_ELIMINACION_HECHOS.remove(solicitudEliminacion);
    System.out.println("Solicitud de eliminación rechazada: "
        + solicitudEliminacion.getHecho().getTitulo());
  }

  // Obtener las solicitudes de eliminación
  public static List<SolicitudEliminacion> getSolicitudesEliminacionHechos() {
    return new ArrayList<>(SOLICITUDES_ELIMINACION_HECHOS);
  }

  // Visualizar hechos eliminados
  public void visualizarHechosEliminados() {
    System.out.println("=== Hechos Eliminados ===");
    HECHOS_ELIMINADOS.forEach(Hecho::imprimirHecho);
    System.out.println("=========================");
  }
}
