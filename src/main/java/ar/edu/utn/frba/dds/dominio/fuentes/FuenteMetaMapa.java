package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import java.io.IOException;
import java.util.List;


public class FuenteMetaMapa implements Fuente {
  private final FuenteMetaMapaAdapter adapter;

  public FuenteMetaMapa(FuenteMetaMapaAdapter adapter) {
    this.adapter = adapter;
  }

  public List<Hecho> obtenerHechos(List<Filtro> filtros) {

    return adapter.obtenerHechos(FiltroUtils.convertirfiltrosaMap(filtros));
  }


  public List<Hecho> obtenerHechosDeUnaColeccion(String id,
                                                 List<Filtro> filtros) {

    return adapter.obtenerHechosDeUnaColeccion(id, FiltroUtils.convertirfiltrosaMap(filtros));
  }

  public void crearSolicitudEliminacion(SolicitudEliminacion solicitud) {
    adapter.crearSolicitudEliminacion(solicitud);
  }

}
