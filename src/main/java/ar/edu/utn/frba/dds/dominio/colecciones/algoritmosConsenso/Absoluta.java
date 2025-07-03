package ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso;

import ar.edu.utn.frba.dds.compartido.servicios.agregador.ResultadoConsenso;
import ar.edu.utn.frba.dds.compartido.servicios.agregador.ServicioDeAgregacion;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Absoluta extends AlgoritmoConsenso {

  public Boolean estaConsensuado(Hecho hecho, List<Hecho> hechosCacheFiltrados) {
    return Objects.equals(this.cuantasVecesAparece(hecho, hechosCacheFiltrados ), this.servicioDeAgregacion.getCantFuentes());
  }

}
