package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuenteMetaMapa extends FuenteProxy {
  private final FuenteMetaMapaAdapter adapter;
  private List<Hecho> hechos;
  private String url;
  public FuenteMetaMapa(FuenteMetaMapaAdapter adapter, String url) {
    this.url=url;
    this.adapter = adapter;
    this.hechos = new ArrayList<>();
  }

  public List<Hecho> obtenerHechos(List<Filtro> filtros) throws IOException {
    filtros.

    this.hechos = adapter.obtenerHechos(filtros);
    return this.hechos;
  }

  public List<Hecho> getHechos() {
    return this.hechos;
  }

  public List<Hecho> obtenerHechosDeUnaColeccion(String id,Map<String, String> filtros) throws IOException {
    this.hechos = adapter.obtenerHechosDeUnaColeccion(id,filtros);
    return this.hechos;
  }

  public void crearSolicitudEliminacion(SolicitudEliminacion solicitud) throws IOException {
    adapter.crearSolicitudEliminacion(solicitud);
  }

  @Override
  public List<Hecho> cargarHechos() {
    return getHechos();
  }
}
