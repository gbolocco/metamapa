//package ar.edu.utn.frba.dds.usuarios;
//
////import ar.edu.utn.frba.dds.colecciones.ColectionManager;
//import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
//import java.util.List;
//
//public class Administrador {
//
//  public void verSolicitudesEliminacion() {
//    List<SolicitudEliminacion> listaEliminaciones = obtenerSolicitudesEliminacion();
//    if (listaEliminaciones.isEmpty()) {
//      System.out.println("No hay solicitudes de eliminacion pendientes");
//      return;
//    }
//
//    listaEliminaciones.forEach(solicitudEliminacion -> {
//      System.out.println(solicitudEliminacion.getHecho().getTitulo());
//      System.out.println();
//    });
//  }
//
//  public void aceptarSolicitudEliminacion(String nombreHecho) {
//    List<SolicitudEliminacion> listaSolicitudesEliminacion = obtenerSolicitudesEliminacion();
//    SolicitudEliminacion solicitudEliminacion = listaSolicitudesEliminacion.stream()
//        .filter(solicitud -> solicitud.getHecho().getTitulo().equals(nombreHecho))
//        .findFirst()
//        .orElse(null);
//    if (solicitudEliminacion == null) {
//      System.out.println("No existe una solicitud de eliminacion con el nombre: " + nombreHecho);
//      return;
//    }
////    solicitudEliminacion.aceptar();
//  }
////
////  public List<SolicitudEliminacion> obtenerSolicitudesEliminacion() {
//////    return ColectionManager.getInstance().getSolicitudesEliminacionHechos();
////  }
//}
