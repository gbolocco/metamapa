package ar.edu.utn.frba.dds.usuarios;


import ar.edu.utn.frba.dds.ColectionManager;
import ar.edu.utn.frba.dds.Fuente;
import ar.edu.utn.frba.dds.SolicitudEliminacion;
import ar.edu.utn.frba.dds.TipoArchivo;
import ar.edu.utn.frba.dds.filtros.Filtro;
import java.util.List;

public class Administrador {

  private String Usuario;
  private String Password;
  public Administrador(String Usuario, String Password) {
    this.Usuario = Usuario;
    this.Password = Password;

  }

  public void crearColeccion(String nombre,String descripcion, String pathArchivo, List<Filtro> filtros,TipoArchivo tipoArchivo){

    Fuente fuente = new Fuente(tipoArchivo,pathArchivo);
    ColectionManager.crearColeccion(nombre,descripcion,filtros,fuente);
  }

  public void importarDatosDeFuenteAColeccion(String nombreColeccion){
    ColectionManager.getColeccion(nombreColeccion).cargarHechosDesdeFuente();
  }
  public void verSolicitudesEliminacion(){
    List<SolicitudEliminacion> listaEliminaciones = obtenerSolicitudesEliminacion();
    if (listaEliminaciones.isEmpty()){
      System.out.println("No hay solicitudes de eliminacion pendientes");
      return;
    }
    listaEliminaciones.forEach(solicitudEliminacion -> {
      System.out.println(solicitudEliminacion.getHecho().getTitulo());
      System.out.println();
    });
  }
  public void aceptarSolicitudEliminacion(String nombreHecho){
    List<SolicitudEliminacion> listaEliminaciones = obtenerSolicitudesEliminacion();
    SolicitudEliminacion solicitudEliminacion = listaEliminaciones.stream()
        .filter(solicitud -> solicitud.getHecho().getTitulo().equals(nombreHecho))
        .findFirst()
        .orElse(null);
    if (solicitudEliminacion == null){
      System.out.println("No existe una solicitud de eliminacion con el nombre: "+nombreHecho);
    }
    solicitudEliminacion.aceptar();
  }

  public List<SolicitudEliminacion> obtenerSolicitudesEliminacion(){
    return ColectionManager.getSolicitudesEliminacionHechos();
  }
}
