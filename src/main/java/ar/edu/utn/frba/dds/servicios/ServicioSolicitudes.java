package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryDB;

import java.util.List;

public class ServicioSolicitudes {
  
  private SolicitudesRepositoryDB repositorioSolicitudes;
  
  public ServicioSolicitudes(SolicitudesRepositoryDB repositorioSolicitudes) {
    this.repositorioSolicitudes = repositorioSolicitudes;
  }
  
  public List<Solicitud> obtenerSolicitudes() {
    return repositorioSolicitudes.pendientes();
  }
  
  //public Solicitud buscarSolicitud(Long id) {
    //return repositorioSolicitudes.buscar(id);
  //}
}