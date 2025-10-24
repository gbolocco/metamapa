package ar.edu.utn.frba.dds.dominio.servidor;

<<<<<<< Updated upstream
import static io.javalin.apibuilder.ApiBuilder.get;

=======
import ar.edu.utn.frba.dds.controladores.AdminController;
import ar.edu.utn.frba.dds.controladores.ColeccionController;
>>>>>>> Stashed changes
import ar.edu.utn.frba.dds.controladores.HechosController;
import ar.edu.utn.frba.dds.controladores.LoginController;
import ar.edu.utn.frba.dds.infraestructura.repositorios.RepositorioUsuarios;
import ar.edu.utn.frba.dds.modelo.Rol;
import ar.edu.utn.frba.dds.routes.Routes;
import ar.edu.utn.frba.dds.servicios.ServicioUsuarios;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.CompositeTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.rendering.FileRenderer;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.javalin.http.staticfiles.Location;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebApp {

  private static final Handlebars handlebars;

  static {
    TemplateLoader loader = new ClassPathTemplateLoader("/templates");
    handlebars = new Handlebars(loader);
  }

  public static void main(String[] args) {
    WebApp app = new WebApp();
    app.start();
  }

  public void start() {
    var app = Javalin.create(this::configureJavalin).start(8080);

  }

  private void configureJavalin(JavalinConfig config) {
    configureStaticFiles(config);
    configureTemplating(config);
    configureRoutes(config);
  }

  private static void configureStaticFiles(JavalinConfig config) {
    config.staticFiles.add(staticFilesConfig -> {
      staticFilesConfig.hostedPath = "/static"; 
      staticFilesConfig.directory = "/public";
      staticFilesConfig.location = Location.CLASSPATH;
    });
  }

  private void configureRoutes(JavalinConfig config){
    var repoUsuarios = new RepositorioUsuarios();
    var servicioUsuarios = new ServicioUsuarios(repoUsuarios);
    HechosController hechos = new HechosController();
    LoginController login = new LoginController(servicioUsuarios);
<<<<<<< Updated upstream
    new Routes().configure(config, hechos, login);
=======
    ColeccionController coleccion = new ColeccionController(servicioColecciones);
    AdminController admin = new AdminController();



    new Routes().configure(config, hechos, login, coleccion, admin);
>>>>>>> Stashed changes
  }

  private void configureTemplating(JavalinConfig config) {
    TemplateLoader mainLoader = new ClassPathTemplateLoader("/templates", ".hbs");
    TemplateLoader partialsLoader = new ClassPathTemplateLoader("/templates/partials", ".hbs");
    Handlebars handlebars = new Handlebars(new CompositeTemplateLoader(mainLoader, partialsLoader));

    handlebars.registerHelper("ifRole", (context, options) -> {
      Map<String, Object> model = (Map<String, Object>) options.context;
      String userRole = (String) model.get("rol");
      String requiredRole = options.param(0, null);
      if (userRole != null && userRole.equals(requiredRole)) {
        return options.fn(context);
      }
      return options.inverse(context);
    });

    FileRenderer handlebarsRenderer = (filePath,
                                  model,
                                  ctx) -> {
      try {
        String templateName = filePath.replace(".hbs", "");
        Template viewTpl   = handlebars.compile(templateName);
        String body        = viewTpl.apply(model);   // model: Map<String, ? extends Object>

        Map<String, Object> m = new HashMap<>(model);

        m.put("body", body);
        Rol rol = ctx.sessionAttribute("rol");
        boolean esAdmin = Rol.ADMIN.equals(rol);
        boolean rutaAdmin = ctx.path().startsWith("/admin");
        m.put("isAdmin", esAdmin && rutaAdmin);
        m.put("bodyClass", (Boolean.TRUE.equals(m.get("isAdmin"))) ? "admin" : "");
        System.out.println("esAdmin: " + esAdmin);
        System.out.println("rutaAdmin: " + rutaAdmin);
        System.out.println("isAdmin: " + m.get("isAdmin"));
        Template layoutTpl = handlebars.compile("layout");
        return layoutTpl.apply(m);
      } catch (IOException e) {
        throw new RuntimeException("Error al renderizar la plantilla Handlebars: " + filePath, e);
      }
    };
    config.fileRenderer(handlebarsRenderer);
  }
}

//  private static Consumer<JavalinConfig> config() {
//    return config -> {
//      ObjectMapper mapper = new ObjectMapper();
//      mapper.registerModule(new JavaTimeModule());
//      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // fechas ISO-8601
//      config.jsonMapper(new JavalinJackson(mapper));
//
//      config.staticFiles.add(staticFiles -> {
//        staticFiles.hostedPath = "/";
//        staticFiles.directory = "/public";
//      });
//      TemplateLoader mainLoader = new ClassPathTemplateLoader("/templates", ".hbs");
//      TemplateLoader partialsLoader = new ClassPathTemplateLoader("/templates/partials", ".hbs");
//      Handlebars handlebars = new Handlebars(new CompositeTemplateLoader(mainLoader, partialsLoader));
//
//      config.fileRenderer((path, model, ctx) -> {
//        try {
//          Template template = handlebars.compile(path.replace(".hbs", ""));
//          Map<String, Object> m = new HashMap<>();
//
//          // Add session/context attributes
//          model.put("isLogged", ctx.attribute("isLogged"));
//          model.put("role", ctx.attribute("role"));
//          model.put("isAdmin", ctx.attribute("isAdmin"));
//
//          return template.apply(m);
//        } catch (IOException e) {
//          ctx.status(HttpStatus.NOT_FOUND);
//          return "No se encuentra la página indicada...";
//        }
//      }, ".hbs");
//    };
//  }
//}