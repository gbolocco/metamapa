package ar.edu.utn.frba.dds;

public class Ubicacion {
  private Integer latitud;
  private Integer longitud;

  public Ubicacion(Integer latitud, Integer longitud) {
    if(esCoordenadaValida(latitud, longitud)){

    this.latitud = latitud;
    this.longitud = longitud;

    }else throw new Error("Latitud y Longitud Invalido");//TODO crear excepcion Personalizada
  }
  public boolean esCoordenadaValida(double latitud, double longitud) {
    return (latitud >= -90 && latitud <= 90) &&
        (longitud >= -180 && longitud <= 180);
  }
}
