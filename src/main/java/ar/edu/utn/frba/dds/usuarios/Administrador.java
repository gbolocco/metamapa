package ar.edu.utn.frba.dds.usuarios;

import ar.edu.utn.frba.dds.colecciones.ColectionManager;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.lectores.Fuente;
import ar.edu.utn.frba.dds.lectores.TipoArchivo;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import java.util.List;

public class Administrador {

  //private String usuario;
  //private String password;

  public Administrador(String usuario, String password) {
    //this.usuario = usuario;
    //this.password = password;
  }

  public void crearColeccion(
      String nombre,
      String descripcion,
      String pathArchivo,
      List<Filtro> filtros,
      TipoArchivo tipoArchivo
  ) {

    Fuente fuente = new Fuente(tipoArchivo, pathArchivo);
    ColectionManager.getInstance().crearColeccion(nombre, descripcion, filtros, fuente);
  }

  public void importarDatosDeFuente(String nombreColeccion) {
    ColectionManager.getInstance().getColeccion(nombreColeccion).cargarHechosDesdeFuente();
  }

  public void verSolicitudesEliminacion() {
    List<SolicitudEliminacion> listaEliminaciones = obtenerSolicitudesEliminacion();
    if (listaEliminaciones.isEmpty()) {
      System.out.println("No hay solicitudes de eliminacion pendientes");
      return;
    }

    listaEliminaciones.forEach(solicitudEliminacion -> {
      System.out.println(solicitudEliminacion.getHecho().getTitulo());
      System.out.println();
    });
  }

  public void aceptarSolicitudEliminacion(String nombreHecho) {
    List<SolicitudEliminacion> listaSolicitudesEliminacion = obtenerSolicitudesEliminacion();
    SolicitudEliminacion solicitudEliminacion = listaSolicitudesEliminacion.stream()
        .filter(solicitud -> solicitud.getHecho().getTitulo().equals(nombreHecho))
        .findFirst()
        .orElse(null);
    if (solicitudEliminacion == null) {
      System.out.println("No existe una solicitud de eliminacion con el nombre: " + nombreHecho);
      return;
    }
    solicitudEliminacion.aceptar();
  }

  public List<SolicitudEliminacion> obtenerSolicitudesEliminacion() {
    return ColectionManager.getInstance().getSolicitudesEliminacionHechos();
  }
}
