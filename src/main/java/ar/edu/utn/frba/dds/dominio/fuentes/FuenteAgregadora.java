package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.compartido.servicios.agregador.ServicioDeAgregacion;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import java.util.List;

public class FuenteAgregadora implements Fuente {
  private ServicioDeAgregacion servicioDeAgregacion;

  public FuenteAgregadora(ServicioDeAgregacion servicioDeAgregacion) {
    this.servicioDeAgregacion = servicioDeAgregacion;
  }
  public List<Hecho> obtenerHechos(List<Filtro> criterios) {
    return servicioDeAgregacion.combinarHechosDesdeTodasLasFuentes( criterios);
  }

  @Override
  public TipoFuente getTipoFuente() {
    return TipoFuente.FUENTE_AGREGADORA;
  }
}
