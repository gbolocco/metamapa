package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeCargaHecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.modelo.Usuario;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.RepresentacionHechosRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.Date;

public class ServicioSolicitudes {
  
  private SolicitudesRepository repositorioSolicitudes;
  private Solicitud solicitud;
  
  public ServicioSolicitudes(SolicitudesRepository repositorioSolicitudes) {
    this.repositorioSolicitudes = repositorioSolicitudes;
  }
  
  public List<Solicitud> obtenerSolicitudes() {
    return repositorioSolicitudes.pendientes();
  }
  
  public List<Solicitud> obtenerTodasLasSolicitudes() {
    return repositorioSolicitudes.todas();
  }
  
  public List<Solicitud> obtenerSolicitudesPendientesPorTipo(TipoSolicitud tipoSolicitud) {
    return repositorioSolicitudes.pendientesPorTipo(tipoSolicitud);
  }
  
  public List<Solicitud> obtenerSolicitudesPorUsuario(Long usuarioId) {
    return repositorioSolicitudes.buscarPorUsuario(usuarioId);
  }

  public void crearHecho(Optional<Usuario> usuario, Hecho hecho, TipoSolicitud tipoSolicitud) {
    
    var representacionDeHecho = new RepresentacionDeHecho(
        hecho.getTitulo(),
        hecho.getDescripcion(),
        hecho.getCategoria(),
        hecho.getUbicacion(),
        hecho.getFechaAcontecimiento(),
        hecho.getFechaDeCarga(),
        OrigenHecho.PROVISTO_POR_CONTRIBUYENTE
    );
    
    RepresentacionHechosRepository.getInstancia().cargarRepresentacionDeHecho(representacionDeHecho);
    
    switch (tipoSolicitud) {
      case CARGA_HECHO:
        solicitud = new SolicitudDeCargaHecho(representacionDeHecho);
        break;
      case ELIMINACION_HECHO:
        solicitud = new SolicitudEliminacion();
        solicitud.setRepresentacionDeHecho(representacionDeHecho);
        break;
      case MODIFICACION_HECHO:
        solicitud = new SolicitudModificacion();
        solicitud.setRepresentacionDeHecho(representacionDeHecho);
        break;
      default:
        throw new IllegalArgumentException("Tipo de solicitud no soportado: " + tipoSolicitud);
    }
    
    solicitud.setUsuario(usuario.orElse(null));
    solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
    solicitud.setFechaSolicitud(new java.util.Date());
    
    repositorioSolicitudes.agregar(solicitud);
  }

  
  public void crearSolicitud(Usuario usuario, Hecho hecho, TipoSolicitud tipoSolicitud, String justificacion) {
    crearHecho(Optional.ofNullable(usuario), hecho, tipoSolicitud);
    
    if (solicitud != null && justificacion != null) {
      solicitud.setJustificacion(justificacion);
      repositorioSolicitudes.actualizar(solicitud);
    }
  }

  public void confirmarSolicitud(Long solicitudId) {
    var solicitud = repositorioSolicitudes.buscarSolicitudPorId(solicitudId);
    if (solicitud != null) {
      solicitud.aceptar();
    }
  }
  
  public void rechazarSolicitud(Long solicitudId) {
    var solicitud = repositorioSolicitudes.buscarSolicitudPorId(solicitudId);
    if (solicitud != null) {
      solicitud.rechazar();
    }
  }
}