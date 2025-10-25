package ar.edu.utn.frba.dds.script;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.modelo.Rol;
import ar.edu.utn.frba.dds.modelo.Usuario;
import ar.edu.utn.frba.dds.infraestructura.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Bootstrap implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    new Bootstrap().init();
  }

  public void init() {
    withTransaction(() -> {

      var usuarios = Arrays.asList(
          new Usuario("feli", "feli", Rol.USER),
          new Usuario("dani", "dani", Rol.USER),
          new Usuario("umi", "umi", Rol.ADMIN)
      );
      usuarios.forEach((usuario) -> RepositorioUsuarios.INSTANCE.registrar(usuario));
      var hechos = Arrays.asList(
          new Hecho("Prueba1", "Prueba1", "Prueba1", new Ubicacion(30.2,30.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
          new Hecho("Prueba2", "Prueba2", "Prueba2", new Ubicacion(20.2,10.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.FUENTE_PROXY)
      );
      hechos.forEach(hecho -> hecho.addContenidoMultimedia("https://media.istockphoto.com/id/155666671/es/" +
          "vector/ilustraci%C3%B3n-vectorial-de-red-house-icon.jpg?s=612x612&w=0&k=20&c=3IHzI5tgnVZQuE_4ZdJDIDyMGd44qWuketKv5EOvawQ="));
      hechos.forEach((hecho) -> HechosRepository.getInstancia().cargarHecho(hecho));
      var fuente = new FuenteDinamica();
      var colecciones = Arrays.asList(
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba2","prueba2",new ArrayList<>(), fuente,"prueba2"),
          new Coleccion("prueba2","prueba2",new ArrayList<>(), fuente,"prueba2"),
          new Coleccion("prueba2","prueba2",new ArrayList<>(), fuente,"prueba2")
      );
      colecciones.forEach(c -> ColeccionRepository.getInstancia().agregarColeccion(c));

    });

  }

}
