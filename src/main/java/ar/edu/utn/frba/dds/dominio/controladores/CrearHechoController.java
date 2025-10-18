package ar.edu.utn.frba.dds.dominio.controladores;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepositoryMemory;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class CrearHechoController implements Handler, WithSimplePersistenceUnit {

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    String titulo = ctx.formParam("titulo");
    String descripcion = ctx.formParam("descripcion");
    String categoria = ctx.formParam("categoria");
    double lat = Double.parseDouble(ctx.formParam("lat"));
    double lon = Double.parseDouble(ctx.formParam("lon"));
    LocalDateTime fechaOcurrencia = LocalDateTime.parse(ctx.formParam("fechaOcurrencia"));

    Hecho hecho = new Hecho(
        titulo,
        descripcion,
        categoria,
        new Ubicacion(lat, lon),
        fechaOcurrencia,
        LocalDateTime.now(),
        OrigenHecho.PROVISTO_POR_CONTRIBUYENTE
    );

    HechosRepositoryMemory.getInstancia().cargarHecho(hecho);
    //DISCUTIR SI DEJAR ACA O EN cargarHecho()
    entityManager().getTransaction().begin();
    entityManager().flush();
    entityManager().getTransaction().commit();
  }
}
