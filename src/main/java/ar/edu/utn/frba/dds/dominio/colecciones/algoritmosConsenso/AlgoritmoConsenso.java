package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepositoryMemory;
import java.util.List;

public abstract class AlgoritmoConsenso {

  public abstract Boolean estaConsensuado(Hecho hecho, List<List<Hecho>> hechosCacheFiltrados);

  public List<Hecho> hechosConsensuados(
      List<Hecho> hechosColeccion,
      List<Filtro> criterioDePertenencia) {
    List<List<Hecho>> hechosCache = FuentesRepositoryMemory.getInstancia().obtenerHechosPorFuente(criterioDePertenencia);
    return hechosColeccion.stream().filter(hecho -> estaConsensuado(hecho, hechosCache)).toList();
  }

  public int cuantasVecesAparece(
      Hecho hecho,
      List<List<Hecho>> hechosCacheFiltrados) {
    return (int) hechosCacheFiltrados.stream()
        .filter(listaPorFuente ->
            listaPorFuente.stream().anyMatch(h -> this.sonEquivalentes(hecho, h))).count();
  }

  public boolean sonEquivalentes(Hecho h1, Hecho h2) {
    return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
        && h1.getAtributosClave().equals(h2.getAtributosClave());
  }
}
