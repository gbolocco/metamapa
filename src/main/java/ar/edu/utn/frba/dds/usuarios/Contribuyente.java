package ar.edu.utn.frba.dds.usuarios;

import ar.edu.utn.frba.dds.colecciones.Coleccion;
import ar.edu.utn.frba.dds.colecciones.ColectionManager;
import ar.edu.utn.frba.dds.hecho.Hecho;
import ar.edu.utn.frba.dds.filtros.Filtro;
import java.util.List;

public class Contribuyente {
  private String nombre;
  private String apellido;
  private Integer edad;

  public Contribuyente(String nombre, String apellido, Integer edad) {
    if(nombre ==null || apellido==null || edad==null){
      throw new IllegalArgumentException("falta completar algun campo");
    }
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
  }

  private boolean esMayorDeEdad(){
    return this.edad != null && this.edad >=18 ;
  }
  public void navegarHechosPorColeccionSegunFiltros(Coleccion coleccion, List<Filtro> filtros) {
    coleccion.visualizarHechos(filtros);
  }
  public void solicitarEliminacionHecho(String nombreHecho, String justificacion) {
    Hecho hecho = ColectionManager.buscarHechoPorNombreDentroDeColecciones(nombreHecho);
    ColectionManager.solicitudEliminacionHecho(nombreHecho, justificacion);
    System.out.println("[-] Solicitud de eliminacion pendiente de confirmacion del Hecho: "+ hecho.getTitulo());

  }

}

