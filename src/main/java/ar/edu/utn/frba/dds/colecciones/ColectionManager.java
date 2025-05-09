package ar.edu.utn.frba.dds.colecciones;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hecho.Hecho;
import ar.edu.utn.frba.dds.lectores.Fuente;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColectionManager {

  private List<Coleccion> colecciones = new ArrayList<>();
  private List<Hecho> hechosEliminados = new ArrayList<>();
  private List<SolicitudEliminacion> solicitudesEliminacionHechos = new ArrayList<>();
  private static ColectionManager instance = new ColectionManager();

  private ColectionManager() {
  }

  //getters
  public static ColectionManager getInstance() {
    return instance;
  }

  public List<Coleccion> getColecciones() {
    return new ArrayList<>(this.colecciones);
  }

  public List<Hecho> getHechosEliminados() {
    return new ArrayList<>(this.hechosEliminados);
  }

  public List<SolicitudEliminacion> getSolicitudesEliminacionHechos() {
    return new ArrayList<>(this.solicitudesEliminacionHechos);
  }

  // Método para crear una nueva colección
  public void crearColeccion(
      String nombre,
      String descripcion,
      List<Filtro> filtros,
      Fuente fuente
  ) {
    this.getColecciones().add(new Coleccion(nombre, descripcion, filtros, fuente));
  }

  // Obtener colección por nombre
  public Coleccion getColeccion(String nombre) {
    return this.getColecciones().stream()
        .filter(coleccion -> Objects.equals(nombre, coleccion.nombre()))
        .findFirst()
        .orElse(null); // Podrías lanzar una excepción aquí si es necesario
  }

  // Solicitar eliminación de un hecho
  public void solicitarEliminacionHecho(String nombreHecho, String justificacion) {
    Hecho hecho = buscarHechoPorNombre(nombreHecho);
    if (hecho == null) {
      throw new IllegalArgumentException("No existe un hecho con el nombre: " + nombreHecho);
    }
    SolicitudEliminacion nuevaSolicitud = new SolicitudEliminacion(hecho, justificacion);
    this.getSolicitudesEliminacionHechos().add(nuevaSolicitud);
  }

  // Buscar hecho por nombre
  public Hecho buscarHechoPorNombre(String nombreHecho) {
    return this.getColecciones().stream()
        .flatMap(coleccion -> coleccion.getHechos().stream())
        .filter(hecho -> Objects.equals(hecho.getTitulo(), nombreHecho))
        .findFirst()
        .orElse(null);
  }

  // Aceptar la solicitud de eliminación
  public void fueAceptada(SolicitudEliminacion solicitudEliminacion) {
    Hecho hechoEliminado = solicitudEliminacion.getHecho();
    hechoEliminado.eliminarHecho();
    this.getSolicitudesEliminacionHechos().remove(solicitudEliminacion);
    this.getHechosEliminados().add(hechoEliminado);
    System.out.println("Hecho eliminado: " + solicitudEliminacion
        .getHecho().getTitulo());
  }

  // Rechazar la solicitud de eliminación
  public void fueRechazada(SolicitudEliminacion solicitudEliminacion) {
    this.getSolicitudesEliminacionHechos().remove(solicitudEliminacion);
    System.out.println("Solicitud de eliminación rechazada: "
        + solicitudEliminacion.getHecho().getTitulo());
  }

  // Visualizar hechos eliminados
  public void visualizarHechosEliminados() {
    System.out.println("=== Hechos Eliminados ===");
    this.getHechosEliminados().forEach(Hecho::imprimirHecho);
    System.out.println("=========================");
  }
}
