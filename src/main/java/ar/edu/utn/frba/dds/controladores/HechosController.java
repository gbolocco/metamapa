package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.multimedia.TipoContenido;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.servicios.ServicioHechos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HechosController implements WithSimplePersistenceUnit {
  private ServicioHechos servicioHechos;
  private final ObjectMapper mapper = new ObjectMapper();

  public HechosController(ServicioHechos servicioHechos) {
    this.servicioHechos = servicioHechos;
  }

  public void listar(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("title", "Solicitudes");
    model.put("content", "View requests here...");
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("hechos", servicioHechos.mostrarHechos());
    ctx.render("hechos.hbs", model);
  }

  public void mostrarFormulario(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    ctx.render("hechos-form.hbs",model);
  }

  public void crear(Context ctx) {
    try {
      String titulo = ctx.formParam("titulo");
      String descripcion = ctx.formParam("descripcion");
      String categoria = ctx.formParam("categoria");
      double lat = Double.parseDouble(ctx.formParam("lat"));
      double lon = Double.parseDouble(ctx.formParam("lon"));
      LocalDateTime fechaOcurrencia = LocalDateTime.parse(ctx.formParam("fechaOcurrencia"));
      String foto = ctx.formParam("foto");
      String video = ctx.formParam("video");

      Hecho hecho = new Hecho(
          titulo,
          descripcion,
          categoria,
          new Ubicacion(lat, lon),
          fechaOcurrencia,
          LocalDateTime.now(),
          OrigenHecho.PROVISTO_POR_CONTRIBUYENTE
      );
      
      hecho.addContenidoMultimedia(foto, TipoContenido.IMAGEN);
      hecho.addContenidoMultimedia(video, TipoContenido.VIDEO);

      //todo: deberia pegarle a un service, ese service al repositorio y despues a la base de datos
      servicioHechos.cargarHecho(hecho);
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
    try {
      System.out.println("➡️ Entrando al método mostrar()");

      String idParam = ctx.queryParam("id");
      String hechoJson = ctx.queryParam("hecho");

      System.out.println("🟢 idParam: " + idParam);
      System.out.println("🟢 hechoJson: " + hechoJson);

      if (hechoJson == null || hechoJson.isEmpty()) {
        ctx.status(400).result("Falta el parámetro 'hecho'");
        return;
      }

      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      Hecho hecho = mapper.readValue(hechoJson, Hecho.class);

      Long id = null;
      try {
        if (idParam != null && !idParam.equals("null")) {
          id = Long.parseLong(idParam);
          hecho.setContenidoMultimedia(servicioHechos.buscar(id).getContenidoMultimedia());
          System.out.println(hecho.getContenidoMultimedia());
        }
          if (hecho.getContenidoMultimedia() == null)
            hecho.setContenidoMultimedia(new ArrayList<>());

      } catch (NumberFormatException e) {
        System.err.println("⚠️ id inválido: " + idParam);
      }

      Map<String, Object> model = new HashMap<>();
      model.put("hecho", hecho);
      model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
      model.put("rol", ctx.sessionAttribute("rol"));
      model.put("user_name", ctx.sessionAttribute("user_name"));
      model.put("user_id", ctx.sessionAttribute("user_id"));

      ctx.render("hecho.hbs", model);

    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error interno del servidor: " + e.getMessage());
    }
  }



}

