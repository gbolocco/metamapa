package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

public abstract class AlgoritmoConsenso {


  public abstract Boolean estaConsensuado(Hecho hecho, List<List<Hecho>> hechosCacheFiltrados);

  public List<Hecho> hechosConsensuados(
      List<Hecho> hechosColeccion,
      List<Filtro> criterioDePertenencia) {
    List<List<Hecho>> hechosCache = FuentesRepository
        .getInstancia().obtenerHechosDeFuentes(criterioDePertenencia);
    return hechosColeccion.stream().filter(hecho -> estaConsensuado(hecho, hechosCache)).toList();
  }

  public int cuantasVecesAparece(
      Hecho hecho,
      List<List<Hecho>> hechosCacheFiltrados) {
    return (int) hechosCacheFiltrados.stream()
        .filter(listaPorFuente ->
            listaPorFuente.stream().anyMatch(h -> this.sonEquivalentes(hecho, h))).count();
  }

  public  boolean sonEquivalentes(Hecho h1, Hecho h2) {
    return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
        && h1.getDescripcion().equals(h2.getDescripcion())
        && h1.getCategoria().equals(h2.getCategoria())
        && h1.getUbicacion().getLatitud().equals(h2.getUbicacion().getLatitud())
        && h1.getUbicacion().getLongitud().equals(h2.getUbicacion().getLongitud())
        && h1.getFechaAcontecimiento().equals(h2.getFechaAcontecimiento());
  }

}
