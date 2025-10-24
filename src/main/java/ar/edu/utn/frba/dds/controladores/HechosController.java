package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HechosController implements WithSimplePersistenceUnit {
  private HechosRepository repo = HechosRepository.getInstancia();
  private Map<String, Object> model;

  public void listar(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("title", "Solicitudes");
    model.put("content", "View requests here...");
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    model.put("hechos", repo.mostrarHechos());
    ctx.render("hechos.hbs", model);
  }

  public void mostrarFormulario(Context ctx) {
    ctx.render("hechos-form.hbs");
  }

  public void mostrarMapa(Context ctx) throws JsonProcessingException {
    Collection<Hecho> hechos = repo.mostrarHechos();

    Map<String, Object> model = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    String hechosJson = mapper.writeValueAsString(hechos);

    model.put("hechosJson", hechosJson);
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    ctx.render("mapa.hbs", model);
  }

  public void crear(Context ctx) {
    try {
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

      //todo: deberia pegarle a un service, ese service al repositorio y despues a la base de datos
      repo.cargarHecho(hecho);
      //DISCUTIR SI DEJAR ACA O EN cargarHecho()
      entityManager().getTransaction().begin();
      entityManager().flush();
      entityManager().getTransaction().commit();


      ctx.redirect("/hechos");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(400).result("Error al crear el hecho");
    }
  }

  public void mostrar(Context ctx) {
    Long hechoId = Long.parseLong(ctx.pathParam("hechoId"));

    if (repo.existe(hechoId)) {
      ctx.json(repo.buscar(hechoId));
    } else {
      ctx.status(HttpStatus.NOT_FOUND);
      ctx.result("Producto no encontrado");
    }
  }
}
