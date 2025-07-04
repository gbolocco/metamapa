package ar.edu.utn.frba.dds.compartido.servicios.agregador;

import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;

import java.util.List;

public class ResultadoConsenso {
  private final int cantidadTotalFuentes;
  private final int cantidadCoincidencias;


  public ResultadoConsenso(int total, int coincidencias) {
    this.cantidadTotalFuentes = total;
    this.cantidadCoincidencias = coincidencias;

  }

  public int getCantidadTotalFuentes() { return cantidadTotalFuentes; }
  public int getCantidadCoincidencias() { return cantidadCoincidencias; }


  @Override
  public String toString() {
    return String.format("Fuentes: %d, Coincidencias: %d", cantidadTotalFuentes, cantidadCoincidencias);
  }
}
