package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.multimedia.TipoContenido;
import ar.edu.utn.frba.dds.servicios.ServicioHechos;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.ArrayList;

import java.time.LocalDateTime;
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
    
    String success = ctx.queryParam("success");
    if ("solicitud_creada".equals(success)) {
      model.put("successMessage", "Solicitud creada exitosamente");
    }
    
    String error = ctx.queryParam("error");
    if (error != null) {
      model.put("errorMessage", "Error al crear solicitud: " + error);
    }
    
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
      entityManager().clear();

      ctx.redirect("/hechos");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(400).result("Error al crear el hecho");
    }
  }

  public void mostrar(Context ctx) {
    try {
      String idParam = ctx.queryParam("id");
      String hechoJson = ctx.queryParam("hecho");

      if (hechoJson == null || hechoJson.isEmpty()) {
        // Si no hay hechoJson pero sí hay id, buscar el hecho en la BD
        if (idParam != null && !idParam.equals("null")) {
          try {
            Long id = Long.parseLong(idParam);
            var hechoFromDB = servicioHechos.buscar(id);
            if (hechoFromDB != null) {
              // Usar el hecho de la BD directamente
              Map<String, Object> model = new HashMap<>();
              model.put("hecho", hechoFromDB);
              model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
              model.put("rol", ctx.sessionAttribute("rol"));
              model.put("user_name", ctx.sessionAttribute("user_name"));
              model.put("user_id", ctx.sessionAttribute("user_id"));
              
              // Manejar mensajes de sesión
              String successMessage = ctx.sessionAttribute("successMessage");
              if (successMessage != null) {
                model.put("successMessage", successMessage);
                ctx.sessionAttribute("successMessage", null);
              }
              
              String errorMessage = ctx.sessionAttribute("errorMessage");
              if (errorMessage != null) {
                model.put("errorMessage", errorMessage);
                ctx.sessionAttribute("errorMessage", null);
              }
              
              ctx.render("hecho.hbs", model);
              return;
            }
          } catch (NumberFormatException e) {
            System.err.println("⚠️ id inválido: " + idParam);
          }
        }
        ctx.status(400).result("Falta el parámetro 'hecho' o 'id' válido");
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
          var hechoFromDB = servicioHechos.buscar(id);
          if (hechoFromDB != null) {
            hecho.setContenidoMultimedia(hechoFromDB.getContenidoMultimedia());
          }
        }
        
        if (hecho.getContenidoMultimedia() == null) {
          hecho.setContenidoMultimedia(new ArrayList<>());
        }

      } catch (NumberFormatException e) {
        System.err.println("⚠️ id inválido: " + idParam);
      }

      Map<String, Object> model = new HashMap<>();
      model.put("hecho", hecho);
      model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
      model.put("rol", ctx.sessionAttribute("rol"));
      model.put("user_name", ctx.sessionAttribute("user_name"));
      model.put("user_id", ctx.sessionAttribute("user_id"));
      
      // Manejar mensajes de sesión
      String successMessage = ctx.sessionAttribute("successMessage");
      if (successMessage != null) {
        model.put("successMessage", successMessage);
        ctx.sessionAttribute("successMessage", null); // Limpiar mensaje
      }
      
      String errorMessage = ctx.sessionAttribute("errorMessage");
      if (errorMessage != null) {
        model.put("errorMessage", errorMessage);
        ctx.sessionAttribute("errorMessage", null); // Limpiar mensaje
      }
      
      // También manejar parámetros de URL (fallback)
      String success = ctx.queryParam("success");
      if ("solicitud_creada".equals(success)) {
        model.put("successMessage", "Solicitud creada exitosamente");
      }
      
      String error = ctx.queryParam("error");
      if (error != null) {
        model.put("errorMessage", "Error al crear solicitud: " + error);
      }

      ctx.render("hecho.hbs", model);

    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error interno del servidor: " + e.getMessage());
    }
  }



}

