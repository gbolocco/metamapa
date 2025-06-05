package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FuenteDemo implements Fuente {
  private Conexion conexion;
  private String url;

  public FuenteDemo(String url,Conexion conexion) {
    this.url=url;
    this.conexion=conexion;
  }

  public void IncorporarNuevosHechosSiLosHay(LocalDate fecha) {
    Map<String, Object> datosHecho;
    while((datosHecho=conexion.siguienteHecho(this.url,fecha))!=null){
      Hecho hecho= this.crearHechoDesdeMap(datosHecho);
      HechosRepositoryMemory.getInstancia().cargarHecho(hecho);
    }
    if(conexion.siguienteHecho(this.url,fecha)==null){
      throw new RuntimeException("No hay nuevos hechos");
    }
  }
  private Hecho crearHechoDesdeMap(Map<String, Object> datosHecho) {
    String titulo = (String) datosHecho.getOrDefault("titulo", "");
    String descripcion = (String) datosHecho.getOrDefault("descripcion", "");
    String categoria = (String) datosHecho.getOrDefault("categoria", "");
    Ubicacion ubicacion = (Ubicacion) datosHecho.get("ubicacion");
    LocalDate fechaAcontecimiento = (LocalDate) datosHecho.get("fechaAcontecimiento");
    LocalDate fechaDeCarga = (LocalDate) datosHecho.getOrDefault("fechaDeCarga", LocalDate.now());

    return new Hecho(
        titulo,
        descripcion,
        categoria,
        ubicacion,
        fechaAcontecimiento,
        fechaDeCarga,
        OrigenHecho.FUENTE_PROXY
    );
  }

  //TODO hay q traerse los hechos filtrados segun el criterio de pertenencia del HechosRepository
  @Override
  public List<Hecho> obtenerHechos(List<Filtro> criterios) {
    return List.of();
  }
}



