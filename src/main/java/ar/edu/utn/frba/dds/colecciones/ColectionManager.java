package ar.edu.utn.frba.dds.colecciones;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hecho.Hecho;
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



  public List<SolicitudEliminacion> getSolicitudesEliminacionHechos() {
    return new ArrayList<>(this.solicitudesEliminacionHechos);
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
}
