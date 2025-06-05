package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuenteMetaMapa implements Fuente {
  private final FuenteMetaMapaAdapter adapter;
  private String url;
  public FuenteMetaMapa(FuenteMetaMapaAdapter adapter, String url) {
    this.url=url;
    this.adapter = adapter;
  }

  public List<Hecho> obtenerHechos(List<Filtro> filtros) throws IOException {

    return adapter.obtenerHechos(FiltroUtils.convertirFiltrosAMap(filtros));
  }


  public List<Hecho> obtenerHechosDeUnaColeccion(String id,List<Filtro> filtros) throws IOException {

    return adapter.obtenerHechosDeUnaColeccion(id,FiltroUtils.convertirFiltrosAMap(filtros));
  }

  public void crearSolicitudEliminacion(SolicitudEliminacion solicitud) throws IOException {
    adapter.crearSolicitudEliminacion(solicitud);
  }

}
