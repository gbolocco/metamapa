package ar.edu.utn.frba.dds.dominio.servidor;

import ar.edu.utn.frba.dds.controladores.HechosController;
import ar.edu.utn.frba.dds.controladores.LoginController;
import ar.edu.utn.frba.dds.routes.Routes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.CompositeTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.rendering.JavalinRenderer;

import java.io.IOException;
import java.util.function.Consumer;

import static io.javalin.apibuilder.ApiBuilder.*;

public class WebApp {

    public static void main(String[] args) {
      initTemplateEngine();
      var app = Javalin.create(config())
          .start(8080);

      HechosController hechoController = new HechosController();
      LoginController loginController = new LoginController();

      Routes.instance.setApp(app);
      Routes.instance.configureRoutes(hechoController, loginController);
      Routes.instance.initialRouting();
  }

  private static void initTemplateEngine() {
    JavalinRenderer.register(
        (path, model, context) -> {
          try {
            TemplateLoader mainLoader = new ClassPathTemplateLoader("/templates", ".hbs");
            TemplateLoader partialsLoader = new ClassPathTemplateLoader("/templates/partials", ".hbs");
            Handlebars handlebars = new Handlebars(new CompositeTemplateLoader(mainLoader, partialsLoader));

            Template template = handlebars.compile(path.replace(".hbs", ""));

            return template.apply(model);

          } catch (IOException e) {
            context.status(HttpStatus.NOT_FOUND);
            return "No se encuentra la página indicada...";
          }
        },
        ".hbs" // extensión de las vistas
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

