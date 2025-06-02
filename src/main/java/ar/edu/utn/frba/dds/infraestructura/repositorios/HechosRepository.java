package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudDeCargaHecho;
import java.util.ArrayList;
import java.util.List;

public class HechosRepository {

  private HechosRepository() {
  }

  private static final HechosRepository instance = new HechosRepository();

  private final List<Hecho> hechos = new ArrayList<>();

  public static HechosRepository getInstancia() {
    return instance;
  }

  public void cargarHecho(Hecho hecho) {
    this.hechos.add(hecho);
  }

  public List<Hecho> mostrarHechos() {
    return new ArrayList<>(this.hechos);
  }


}
