package ar.edu.utn.frba.dds.script;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.modelo.Usuario;
import ar.edu.utn.frba.dds.infraestructura.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Bootstrap implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    new Bootstrap().init();
  }

  public void init() {
    withTransaction(() -> {
      var usuarios = Arrays.asList(
          new Usuario("feli", "feli"),
          new Usuario("dani", "dani"),
          new Usuario("umi", "umi")
      );
      usuarios.forEach((usuario) -> RepositorioUsuarios.INSTANCE.registrar(usuario));
      var hechos = Arrays.asList(
          new Hecho("Prueba1", "Prueba1", "Prueba1", new Ubicacion(30.2,30.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
          new Hecho("Prueba2", "Prueba2", "Prueba2", new Ubicacion(20.2,10.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.FUENTE_PROXY)
      );
      hechos.forEach((hecho) -> HechosRepository.getInstancia().cargarHecho(hecho));

    });

  }

}
