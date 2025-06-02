package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.TipoCombinacion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeCargaHecho;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ar.edu.utn.frba.dds.dominio.filtros.TipoCombinacion;

public class HechosYSolicitudesRepository {

  private HechosYSolicitudesRepository() {
  }

  private static final HechosYSolicitudesRepository instance = new HechosYSolicitudesRepository();

  private final List<Hecho> hechos = new ArrayList<>();
  private final List<Solicitud> solicitudes = new ArrayList<>();

  public static HechosYSolicitudesRepository getInstancia() {
    return instance;
  }

  public void cargarHecho(Hecho hecho) {
    this.hechos.add(hecho);
  }

  public void cargarSolicitud(SolicitudDeCargaHecho solicitud) {
    this.solicitudes.add(solicitud);
  }

  public List<Hecho> mostrarHechos() {
    return new ArrayList<>(this.hechos);
  }

  public void eliminarSolicitud(SolicitudDeCargaHecho unaSolicitud){
    this.solicitudes.remove(unaSolicitud);
  }

  public List<Solicitud> mostrarSolicitudes() {
    return new ArrayList<>(this.solicitudes);
  }

  public void seAceptoUnaSolicitud(SolicitudDeCargaHecho solicitud) {
    this.eliminarSolicitud(solicitud);
    this.cargarHecho(solicitud.getHecho());
  }

  public void seRechazoUnaSolicitud(SolicitudDeCargaHecho solicitud) {
    this.solicitudes.remove(solicitud);
  }

}
