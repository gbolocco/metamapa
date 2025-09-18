package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("fuenteDinamica")
public class FuenteDinamica extends Fuente {
  @Override
  public List<Hecho> obtenerHechos(List<Filtro> criterios) {
    return HechosRepositoryMemory
        .getInstancia()
        .filtrarHechos(criterios, OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
  }

  @Override
  public TipoFuente getTipoFuente() {
    return TipoFuente.FUENTE_DINAMICA;
  }

  @Override
  public void actualizarLista(List<RepresentacionDeHecho> representaciones){
    List<Hecho> hechosDb = HechosRepositoryMemory.getInstancia().mostrarHechos();
    hechosDb = hechosDb.stream()
        .filter(h -> representaciones.stream().anyMatch(r -> HechosRepositoryMemory.sonEquivalentes(h, r)))
        .toList();
    hechosDb.forEach(hecho -> hecho.setEstadoHecho(EstadoHecho.ELIMINADO));
  }
}
