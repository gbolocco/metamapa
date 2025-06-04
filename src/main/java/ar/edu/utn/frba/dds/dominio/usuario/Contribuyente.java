package ar.edu.utn.frba.dds.dominio.usuario;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeCargaHecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryMemory;
import java.time.LocalDate;

public class Contribuyente {

  private String nombre;
  private Integer edad;

  public Contribuyente(String nombre, Integer edad) {
    if (edad <= 18){
      throw new IllegalArgumentException("El usuario debe ser mayor de edad");
    }
    this.nombre = nombre;
    this.edad = edad;
  }
  public Hecho crearHecho(String titulo, String descripcion, String categoria, Ubicacion ubicacion, LocalDate fechaAcontecimiento, LocalDate fechaDeCarga) {

    // TODO validad creacion de hechos, que campos son obligatorios y cuales no? se poria aplicar un patron builder, borrador de un hecho

    OrigenHecho origenContribuyente = OrigenHecho.PROVISTO_POR_CONTRIBUYENTE;
    origenContribuyente.setContribuyenteHecho(this);

    return new Hecho(titulo, descripcion, categoria, ubicacion, fechaAcontecimiento, fechaDeCarga, origenContribuyente);
  }

  public SolicitudDeCargaHecho generarSolicitudDeCreacion(Hecho hecho){

    SolicitudDeCargaHecho solicitud = new SolicitudDeCargaHecho(hecho);
    SolicitudesRepositoryMemory.getInstancia().agregar(solicitud);
    return solicitud;
  }

  public SolicitudModificacion generarSolicitudDeModificacion(Hecho hechoParaModificar, Hecho hechoActualizado){

    //oriigen y contribuyente del hecho actualizado tiene asociado el contribuyente?

    return new SolicitudModificacion(hechoParaModificar,hechoActualizado,this);

    //[(CAMPO, Modificacion),...]
  }
}
