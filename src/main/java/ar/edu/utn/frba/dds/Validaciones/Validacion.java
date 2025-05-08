package ar.edu.utn.frba.dds.Validaciones;

import ar.edu.utn.frba.dds.hecho.Excepciones.CoordenadaInvalidaException;
import java.util.List;

public class Validacion {
  public static void validarStringNoVacio(String valor, String nombreCampo) {
    if (valor == null || valor.trim().isEmpty()) {
      throw new IllegalArgumentException("El campo '" + nombreCampo + "' no puede ser nulo ni estar vacío.");
    }
  }

  public static void validarNoNulo(Object objeto, String nombreCampo) {
    if (objeto == null) {
      throw new IllegalArgumentException("El campo '" + nombreCampo + "' no puede ser nulo.");
    }
  }

  public static <T> void validarListaNoNulaNiConElementosNulos(List<T> lista, String nombreCampo) {
    if (lista == null) {
      throw new IllegalArgumentException("La lista '" + nombreCampo + "' no puede ser nula.");
    }
    if (lista.contains(null)) {
      throw new IllegalArgumentException("La lista '" + nombreCampo + "' no puede contener elementos nulos.");
    }
  }

  public static void validarLongitudMaxima(String valor, int max, String nombreCampo) {
    if (valor != null && valor.length() > max) {
      throw new IllegalArgumentException("El campo '" + nombreCampo + "' no puede superar los " + max + " caracteres.");
    }
  }

  public static void validarCoordenadas(Double latitud, Double longitud) {
    validarNoNulo(latitud, "latitud");
    validarNoNulo(longitud, "longitud");
    if (latitud < -90 || latitud > 90) {
      throw new CoordenadaInvalidaException("La latitud debe estar entre -90 y 90 grados.");
    }
    if (longitud < -180 || longitud > 180) {
      throw new CoordenadaInvalidaException("La longitud debe estar entre -180 y 180 grados.");
    }
  }
}