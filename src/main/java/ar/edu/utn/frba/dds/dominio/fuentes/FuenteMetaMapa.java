package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import java.io.IOException;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("metamapa")
public class FuenteMetaMapa extends Fuente {

  @OneToOne()
  private FuenteMetaMapaAdapter adapter;

  public FuenteMetaMapa(FuenteMetaMapaAdapter adapter) {
    this.adapter = adapter;
  }

  public FuenteMetaMapa() {

  }

  @Override
  public List<Hecho> obtenerHechos(List<Filtro> filtros) {
    List<Hecho> hechos = adapter.obtenerHechos(FiltroUtils.convertirfiltrosaMap(filtros));
    hechos.forEach(hecho -> HechosRepositoryMemory.getInstancia().cargarHecho(hecho));
    return hechos;
  }

  @Override
  public TipoFuente getTipoFuente() {
    return TipoFuente.FUENTE_PROXY;
  }


  public List<Hecho> obtenerHechosDeUnaColeccion(String id,
                                                 List<Filtro> filtros) {

    return adapter.obtenerHechosDeUnaColeccion(id, FiltroUtils.convertirfiltrosaMap(filtros));
  }

  public void crearSolicitudEliminacion(SolicitudEliminacion solicitud) {
    adapter.crearSolicitudEliminacion(solicitud);
  }

}
