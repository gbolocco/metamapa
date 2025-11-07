package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.RepresentacionHechosRepository;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("metamapa")
public class FuenteMetaMapa extends Fuente {

  @Transient
  private FuenteMetaMapaAdapter adapter;

  public FuenteMetaMapa(FuenteMetaMapaAdapter adapter) {
    this.adapter = adapter;
    this.hechos = new ArrayList<>();
  }

  public FuenteMetaMapa() {

  }

  @Override
  public List<Hecho> obtenerHechos(List<Filtro> filtros) {
    List<Hecho> hechos = adapter.obtenerHechos(FiltroUtils.convertirfiltrosaMap(filtros));
    List<RepresentacionDeHecho> representaciones = RepresentacionHechosRepository
        .getInstancia().getRepHechosEliminados();

    return hechos.stream()
        .filter(h -> representaciones
            .stream()
            .noneMatch(r -> HechosRepository.sonEquivalentes(h, r)))
        .toList();
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
