package ar.edu.utn.frba.dds.routes;

<<<<<<< Updated upstream
=======
import ar.edu.utn.frba.dds.controladores.AdminController;
import ar.edu.utn.frba.dds.controladores.ColeccionController;
>>>>>>> Stashed changes
import ar.edu.utn.frba.dds.controladores.HechosController;
import ar.edu.utn.frba.dds.controladores.LoginController;
import ar.edu.utn.frba.dds.modelo.Rol;
import io.javalin.config.JavalinConfig;

import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Routes {
  public static final Routes instance = new Routes();

<<<<<<< Updated upstream

  public void configure(JavalinConfig config, HechosController hechos, LoginController login) {
=======
  public void configure(
    JavalinConfig config,
    HechosController hechos,
    LoginController login,
    ColeccionController coleccionController,
    AdminController admin) {
>>>>>>> Stashed changes

    config.router.apiBuilder(() -> {
      before(ctx -> {
        // Session configuration for API routes
        if (ctx.sessionAttribute("user_id") == null) ctx.sessionAttribute("user_id", null);
        if (ctx.sessionAttribute("loggedIn") == null) ctx.sessionAttribute("loggedIn", false);
        if (ctx.sessionAttribute("rol") == null) ctx.sessionAttribute("rol", null);
        ctx.attribute("user_id", ctx.sessionAttribute("user_id"));
        ctx.attribute("loggedIn", ctx.sessionAttribute("loggedIn"));
        ctx.attribute("rol", ctx.sessionAttribute("rol"));
        System.out.println("API before - user_id: " + ctx.attribute("user_id") +
            ", loggedIn: " + ctx.attribute("loggedIn") +
            ", rol: " + ctx.attribute("rol"));
      });
<<<<<<< Updated upstream
=======

      before("/admin/*", ctx -> {
        Rol rol = ctx.sessionAttribute("rol");  // enum en sesión
        if (rol == null || rol != Rol.ADMIN) {
          ctx.redirect("/login");
          ctx.status(302);
          ctx.result("");
          return;
        }
      });

      path("/admin", () -> {
        get("/dashboard",admin::mostrarDashboard);
        });

>>>>>>> Stashed changes
      path("/hechos", () -> {
        get(hechos::listar);
        get("/nuevo", hechos::mostrarFormulario);
        get("/mapa", hechos::mostrarMapa);
        post(hechos::crear);
        get("/{hechoId}", hechos::mostrar);
      });

      path("/login", () -> {
        get(login::mostrarLogin);
        post(login::login);
      });

<<<<<<< Updated upstream
=======
      path("/colecciones", () -> {
        get(coleccionController::mostrarColecciones);
      });

>>>>>>> Stashed changes
      config.router.apiBuilder(() -> {
        post("/logout", login::logout);
      });
    });
  }
<<<<<<< Updated upstream

//  public void initialRouting(){
//    app.get("/", ctx -> {
//      Long userId = ctx.sessionAttribute("user_id");
//      if (userId == null) {
//        ctx.redirect("/login");
//      } else {
//        ctx.result("Bienvenido usuario ID " + userId);
//      }
//    });
//  }

  public Routes getInstance() {
    return instance;
  }

//  public void setApp(Javalin app) {
//    this.app = app;
//  }
=======
>>>>>>> Stashed changes
}
