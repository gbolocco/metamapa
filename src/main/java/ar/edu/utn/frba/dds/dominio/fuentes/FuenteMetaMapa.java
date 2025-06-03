package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuenteMetaMapa extends FuenteProxy {
  private final FuenteMetaMapaAdapter adapter;
  private List<Hecho> hechos;

  public FuenteMetaMapa(FuenteMetaMapaAdapter adapter, String url) {
    super(url);
    this.adapter = adapter;
    this.hechos = new ArrayList<>();
  }

  public List<Hecho> obtenerHechos(Map<String, String> filtros) throws IOException {
    this.hechos = adapter.obtenerHechos(filtros);
    return this.hechos;
  }

  public List<Hecho> getHechos() {
    return this.hechos;
  }

  public List<Hecho> obtenerHechosColeccion(Map<String, String> filtros) throws IOException {
    this.hechos = adapter.obtenerHechos(filtros);
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
