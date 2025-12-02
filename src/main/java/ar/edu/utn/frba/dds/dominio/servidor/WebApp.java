package ar.edu.utn.frba.dds.dominio.servidor;

import ar.edu.utn.frba.dds.controladores.*;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.UsuariosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepository;
import ar.edu.utn.frba.dds.modelo.Rol;
import ar.edu.utn.frba.dds.routes.Routes;
import ar.edu.utn.frba.dds.servicios.ServicioColecciones;
import ar.edu.utn.frba.dds.servicios.ServicioFuentes;
import ar.edu.utn.frba.dds.servicios.ServicioHechos;
import ar.edu.utn.frba.dds.servicios.ServicioSolicitudes;
import ar.edu.utn.frba.dds.servicios.ServicioUsuarios;
import com.github.jknack.handlebars.Helper;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WebApp {


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
    var repoUsuarios = UsuariosRepository.INSTANCE;
    var repoColecciones = ColeccionRepository.getInstancia();
    var repoFuentes = FuentesRepository.getInstancia();
    var repoHechos = HechosRepository.getInstancia();
    var repoSolicitudes = new SolicitudesRepository();

    var servicioUsuarios = new ServicioUsuarios(repoUsuarios);
    var servicioColecciones = new ServicioColecciones(repoColecciones);
    var servicioFuente = new ServicioFuentes(repoFuentes);
    var servicioHechos = new ServicioHechos(repoHechos);
    var servicioSolicitudes = new ServicioSolicitudes(repoSolicitudes);

    HechosController hechos = new HechosController(servicioHechos, servicioSolicitudes, servicioUsuarios);

    LoginController login = new LoginController(servicioUsuarios);
    ColeccionController coleccion = new ColeccionController(servicioColecciones);
    AdminController admin = new AdminController(servicioFuente, servicioUsuarios, servicioColecciones, servicioSolicitudes);
    UserController user = new UserController(servicioUsuarios, servicioSolicitudes);
    SolicitudesController solicitudesController = new SolicitudesController(servicioSolicitudes);
    HomeController home = new HomeController();
    EstadisticaController estadisticaController = new EstadisticaController();

    new Routes().configure(config, hechos, login, coleccion, user, admin, solicitudesController,home,estadisticaController);
  }

  private void configureTemplating(JavalinConfig config) {
    TemplateLoader mainLoader = new ClassPathTemplateLoader("/templates", ".hbs");
    TemplateLoader partialsLoader = new ClassPathTemplateLoader("/templates/partials", ".hbs");
    Handlebars handlebars = new Handlebars(new CompositeTemplateLoader(mainLoader, partialsLoader));

    handlebars.registerHelper("resta", (Helper<Integer>) (context, options ) -> {
      // context es el primer argumento (paginaActual)
      // options.param(0) es el segundo argumento (el número a restar, '1')
      int valorArestar = options.param(0, 0); // Usar valor por defecto por seguridad
      return context - valorArestar;
    });

    handlebars.registerHelper("suma", (Helper<Integer>) (context, options) -> {
      int valorAsumar = options.param(0, 0); // Usar valor por defecto por seguridad
      return context + valorAsumar;
    });

    handlebars.registerHelper("ifRole", (context, options) -> {
      Map<String, Object> model = (Map<String, Object>) options.context;
      String userRole = (String) model.get("rol");
      String requiredRole = options.param(0, null);
      if (userRole != null && userRole.equals(requiredRole)) {
        return options.fn(context);
      }
      return options.inverse(context);
    });

    handlebars.registerHelper("eq", (context, options) -> {
      Object param1 = options.param(0, null);
      Object param2 = options.param(1, null);
      if (param1 != null && param1.equals(param2)) {
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