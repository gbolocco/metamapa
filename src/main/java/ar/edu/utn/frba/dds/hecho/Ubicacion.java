package ar.edu.utn.frba.dds.hecho;

public class Ubicacion {
  private Double latitud;
  private Double longitud;

  public Ubicacion(Double latitud, Double longitud) {
    if(esCoordenadaValida(latitud, longitud)){

    this.latitud = latitud;
    this.longitud = longitud;

    }else throw new Error("Latitud y Longitud Invalido");//TODO crear excepcion Personalizada
  }
  public boolean esCoordenadaValida(double latitud, double longitud) {
    return (latitud >= -90 && latitud <= 90) &&
        (longitud >= -180 && longitud <= 180);
  }

  public Double getLatitud() {
    return latitud;
  }

  public Double getLongitud() {
    return longitud;
  }
}
