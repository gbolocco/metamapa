package ar.edu.utn.frba.dds.colecciones;

import ar.edu.utn.frba.dds.hecho.Hecho;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColeccionesRepository {
  private static final ColeccionesRepository instance = new ColeccionesRepository();
  private List<Coleccion> colecciones = new ArrayList<>();
  private List<Hecho> hechosEliminados = new ArrayList<>();
  private ColeccionesRepository(){}
  public static ColeccionesRepository getInstancia(){
    return instance;
  }
  public List<Coleccion> getColecciones() {
    return new ArrayList<>(this.colecciones);
  }
  public List<Hecho> getHechosEliminados() {
    return new ArrayList<>(this.hechosEliminados);
  }

  public void agregarColeccion(Coleccion coleccion) {
    this.colecciones.add(coleccion);
  }

  public Coleccion buscarColeccionPor(String nombre) {
    return this.getColecciones().stream()
        .filter(coleccion -> Objects.equals(nombre, coleccion.nombre()))
        .findFirst()
        .orElse(null); // Podrías lanzar una excepción aquí si es necesario
  }

  public Hecho buscarHechoPorNombre(String nombreHecho) {
    return this.getColecciones().stream()
        .flatMap(coleccion -> coleccion.getHechos().stream())
        .filter(hecho -> Objects.equals(hecho.getTitulo(), nombreHecho))
        .findFirst()
        .orElse(null);
  }
}
