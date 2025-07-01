package ar.edu.utn.frba.dds.compartido.servicios.agregador;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;

import java.util.List;

public class ResultadoConsenso {
  private final int cantidadTotalFuentes;
  private final int cantidadCoincidencias;
  private final List<Fuente> fuentesCoincidentes;
  private final List<TipoFuente> tipoFuentesCoincidentes;

  public ResultadoConsenso(int total, int coincidencias, List<Fuente> fuentes,List<TipoFuente> tipoFuentes) {
    this.cantidadTotalFuentes = total;
    this.cantidadCoincidencias = coincidencias;
    this.fuentesCoincidentes = fuentes;
    this.tipoFuentesCoincidentes = tipoFuentes;
  }

  public int getCantidadTotalFuentes() { return cantidadTotalFuentes; }
  public int getCantidadCoincidencias() { return cantidadCoincidencias; }
  public List<Fuente> getFuentesCoincidentes() { return fuentesCoincidentes; }
  public List<TipoFuente> getTipoFuentesCoincidentes() { return tipoFuentesCoincidentes; }

  @Override
  public String toString() {
    return String.format("Fuentes: %d, Coincidencias: %d", cantidadTotalFuentes, cantidadCoincidencias);
  }
}
