package ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia;

import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;

public class CalculadorProvincia {

  private ServicioCalculadorProvincia servicioCalculador;

  public CalculadorProvincia(ServicioCalculadorProvincia servicio) {
    this.servicioCalculador = servicio;
  }

  public Provincia calcularProvincia(Ubicacion ubicacion) {
    String provincia = servicioCalculador.calcularProvincia(ubicacion);
    return Provincia.valueOf(provincia);
  }


}
