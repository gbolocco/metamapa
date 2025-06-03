package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FuenteDemo extends FuenteProxy {
  private Conexion conexion;
  private List<Hecho> hechos;
  private String url;

  public FuenteDemo(String url,Conexion conexion) {
    this.url=url;
    this.hechos = new ArrayList<>();
  }
  public void IncorporarNuevosHechosSiLosHay(LocalDate fecha) {
    if(conexion.siguienteHecho(this.url,fecha)==null){
      throw new RuntimeException("No hay nuevos hechos");
    }else{
      Hecho hecho= (Hecho) conexion.siguienteHecho(this.url,fecha);
      this.hechos.add(hecho);
    }
  }

  public List<Hecho> getHechos() {
    return hechos;
  }

  @Override
  public List<Hecho> cargarHechos() {
    return this.getHechos();
  }

}
