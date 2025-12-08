package ar.edu.utn.frba.dds.compartido;

import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.dominio.hechos.EstadoHecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataFormatter {

  public String formatearFecha(LocalDateTime fecha) {
    if (fecha == null) {
      return "Fecha Desconocida";
    }
    // Definimos el patrón: día-mes-año
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    return fecha.format(formatter);
  }

  public String formatearOrigen(OrigenHecho origen) {
    if (origen == null) {
      return "Desconocido";
    }
    switch (origen) {
      case PROVISTO_POR_CONTRIBUYENTE:
        return "Provisto por Contribuyente";
      case FUENTE_PROXY:
        return "Fuente Proxy";
      case FUENTE_ESTATICA:
        return "Fuente Estática";
      default:
        return origen.name();
    }
  }

  public String formatearEstado(EstadoHecho estado) {
    if (estado == null) {
      return "Desconocido";
    }
    switch (estado) {

      case VISUALIZABLE:
        return "Visualizable";
      case PENDIENTE_DE_APROBACION:
        return "Pendiente de aprobacion";
      case ELIMINADO:
        return "Eliminado";
      default:
        return estado.name();
    }
  }
    public String formatearFuente(TipoFuente fuente) {
      if (fuente == null) {
        return "Desconocido";
      }
      switch (fuente) {
        case FUENTE_DINAMICA:
          return "Fuente Dinámica";
        case FUENTE_PROXY:
          return "Fuente Proxy";
        case FUENTE_ESTATICA:
          return "Fuente Estática";
        case FUENTE_AGREGADORA:
          return "Fuente Agregadora";
        default:
          return fuente.name();
      }
  }
}
