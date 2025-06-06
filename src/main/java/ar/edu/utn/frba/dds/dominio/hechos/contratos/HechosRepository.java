package ar.edu.utn.frba.dds.dominio.hechos.contratos;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import java.util.List;

public interface HechosRepository {

  void cargarHecho(Hecho hecho);

  List<Hecho> mostrarHechos();

  List<Hecho> filtrarHechos(List<Filtro> filtros, OrigenHecho origen);

  void modificarHecho(Hecho hechoaModificar, Hecho hechoModificado);
}
