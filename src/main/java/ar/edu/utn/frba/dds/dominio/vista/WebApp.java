package ar.edu.utn.frba.dds.dominio.vista;

import ar.edu.utn.frba.dds.dominio.controladores.ListaHechosController;
import ar.edu.utn.frba.dds.dominio.controladores.UIListaHechosController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.rendering.JavalinRenderer;

import java.io.IOException;
import java.util.function.Consumer;

public class WebApp {

    public static void main(String[] args) {
      initTemplateEngine();
      var app = Javalin.create(config())
          .get("/", ctx -> ctx.result("Hello World"))
          .start(8080);

      app.get("/api/hechos", new ListaHechosController());
      app.get("/hechos", new UIListaHechosController());
    }

  private static void initTemplateEngine() {
    JavalinRenderer.register(
        (path, model, context) -> { // Función que renderiza el template
          Handlebars handlebars = new Handlebars();
          Template template = null;
          try {
            template = handlebars.compile("templates/" + path.replace(".hbs", ""));
            return template.apply(model);
          } catch (IOException e) {
            //
            e.printStackTrace();
            context.status(HttpStatus.NOT_FOUND);
            return "No se encuentra la página indicada...";
          }
        }, ".hbs" // Extensión del archivo de template
    );
  }

  private static Consumer<JavalinConfig> config() {
      return config -> {
          // --- Configuración de JSON ---
          ObjectMapper mapper = new ObjectMapper();
          mapper.registerModule(new JavaTimeModule());
          mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // fechas ISO-8601
          config.jsonMapper(new JavalinJackson(mapper));

          // --- Configuración de archivos estáticos ---
          config.staticFiles.add(staticFiles -> {
              staticFiles.hostedPath = "/";
              staticFiles.directory = "/public";
          });

      };

    }
}

