package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.compartido.servicios.agregador.ServicioDeAgregacion;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.util.List;

public abstract class AlgoritmoConsenso {

  protected ServicioDeAgregacion servicioDeAgregacion = new ServicioDeAgregacion();

  public void setServicioDeAgregacion(ServicioDeAgregacion servicioDeAgregacion) {
    this.servicioDeAgregacion = servicioDeAgregacion;
  }

  public abstract Boolean estaConsensuado(Hecho hecho, List<Hecho> hechosCache);

  public List<Hecho> hechosConsensuados(List<Hecho> hechosColeccion,List<Filtro> criterioDePertenencia) {
    List<Hecho> hechosCache = this.servicioDeAgregacion.getHechos(criterioDePertenencia);
    return hechosColeccion.stream().filter(hecho -> estaConsensuado(hecho, hechosCache)).toList();
  }

  public Integer cuantasVecesAparece(Hecho hecho, List<Hecho> hechosCacheFiltrados) {
    return hechosCacheFiltrados.stream().filter(hechoCache -> this.sonEquivalentes(hecho,hechoCache)).toList().size();
  }

  public boolean sonEquivalentes(Hecho h1, Hecho h2) {
    return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
        && h1.getAtributosClave().equals(h2.getAtributosClave());
  }
}
