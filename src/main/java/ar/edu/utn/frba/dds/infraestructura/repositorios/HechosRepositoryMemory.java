package ar.edu.utn.frba.dds.infraestructura.repositorios;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.contratos.HechosRepository;
import java.util.ArrayList;
import java.util.List;

public class HechosRepositoryMemory implements HechosRepository {

  private HechosRepositoryMemory() {
  }

  private static final HechosRepositoryMemory instance = new HechosRepositoryMemory();

  private final List<Hecho> hechos = new ArrayList<>();

  public static HechosRepositoryMemory getInstancia() {
    return instance;
  }

  public void cargarHecho(Hecho hecho) {
    this.hechos.add(hecho);
  }

  public List<Hecho> mostrarHechos() {
    return hechos;
  }

  public List<Hecho> filtrarHechos(List<Filtro> filtros, OrigenHecho origenHecho) {
    return hechos.stream().filter(
        hecho -> filtros
            .stream()
            .allMatch(filtro -> filtro.cumpleFiltro(hecho) && hecho.getOrigenHecho().equals(origenHecho)))
        .toList();
  }

  public void modificarHecho(Hecho hechoaModificar, Hecho hechoModificado) {
    if (!hechos.contains(hechoaModificar)) {
      throw new IllegalArgumentException("El hecho no existe en la fuenta dinamica");
    }
    this.hechos.set(hechos.indexOf(hechoaModificar), hechoModificado);
  }

}
