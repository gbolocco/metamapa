package ar.edu.utn.frba.dds.dominio.colecciones.contratos;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import java.util.List;
import java.util.Optional;

public interface ColeccionRepository {
  List<Coleccion> mostrarColecciones();
  void agregarColeccion(Coleccion coleccion);
  Optional<Coleccion> buscarColeccionPor(String titulo);
  Optional<Hecho> buscarHechoPor(String tituloHecho);
}
