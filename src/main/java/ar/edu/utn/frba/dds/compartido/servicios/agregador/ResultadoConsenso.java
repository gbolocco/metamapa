package ar.edu.utn.frba.dds.compartido.servicios.agregador;

import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;

import java.util.List;

public class ResultadoConsenso {
  private final int cantidadTotalFuentes;
  private final int cantidadCoincidencias;
  private final List<TipoFuente> fuentesCoincidentes;

  public ResultadoConsenso(int total, int coincidencias, List<TipoFuente> fuentes) {
    this.cantidadTotalFuentes = total;
    this.cantidadCoincidencias = coincidencias;
    this.fuentesCoincidentes = fuentes;
  }

  public int getCantidadTotalFuentes() { return cantidadTotalFuentes; }
  public int getCantidadCoincidencias() { return cantidadCoincidencias; }
  public List<TipoFuente> getFuentesCoincidentes() { return fuentesCoincidentes; }

  @Override
  public String toString() {
    return String.format("Fuentes: %d, Coincidencias: %d", cantidadTotalFuentes, cantidadCoincidencias);
  }
}
