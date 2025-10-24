package ar.edu.utn.frba.dds.routes;

import ar.edu.utn.frba.dds.controladores.ColeccionController;
import ar.edu.utn.frba.dds.controladores.HechosController;
import ar.edu.utn.frba.dds.controladores.LoginController;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {



  public void configure(JavalinConfig config, HechosController hechos, LoginController login, ColeccionController coleccionController) {

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

      path("/colecciones", () -> {
        get(coleccionController::mostrarColecciones);
      });

      config.router.apiBuilder(() -> {
        post("/logout", login::logout);
      });
    });
  }

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

//  public void setApp(Javalin app) {
//    this.app = app;
//  }
}
