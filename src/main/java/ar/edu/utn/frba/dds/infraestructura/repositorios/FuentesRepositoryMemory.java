package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;

// todas las fuentes del nodo
public class FuentesRepositoryMemory {

  private static final FuentesRepositoryMemory instance = new FuentesRepositoryMemory();

  private List<Fuente> fuentes = new ArrayList<>();

  private FuentesRepositoryMemory() {}

  public static FuentesRepositoryMemory getInstancia() { return instance; }

  public List<Fuente> getFuentes() {
    return fuentes;
  }

  public void agregarFuente(Fuente fuente) {
    this.fuentes.add(fuente);
  }

  // metodos para algoritmos de consenso

  // Notar que este metodo retorna una lista de listas (lista con una lista de hechos por fuente)
  public List<List<Hecho>> obtenerHechosPorFuente(List<Filtro> criterios) {
    return this.fuentes.stream()
        .map(fuente -> fuente.obtenerHechos(criterios))
        .toList();
  }

  public int getCantidadFuentes() {
    return  this.fuentes.size();
  }

}
