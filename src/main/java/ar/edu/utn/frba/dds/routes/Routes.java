package ar.edu.utn.frba.dds.routes;

import ar.edu.utn.frba.dds.controladores.HechosController;
import ar.edu.utn.frba.dds.controladores.LoginController;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Routes {
  private Javalin app;
  public static final Routes instance = new Routes();


  public void Routes(Javalin app) {
    this.app = app;
  }

  public void configureRoutes(HechosController hechoController, LoginController loginController){
    app.routes(() -> {
      path("hechos", () -> {
        get(hechoController::listar);
        get("nuevo", hechoController::mostrarFormulario);
        get("mapa", hechoController::mostrarMapa);
        post(hechoController::crear);
        get("{hechoId}", hechoController::mostrar);
      });

      // Login y usuarios
      path("login", () -> {
        get(loginController::mostrarLogin);
        post(loginController::login);
      });
    });
  }

  public void initialRouting(){
    app.get("/", ctx -> {
      Long userId = ctx.sessionAttribute("user_id");
      if (userId == null) {
        ctx.redirect("/login");
      } else {
        ctx.result("Bienvenido usuario ID " + userId);
      }
    });
  }

  public Routes getInstance() {
    return instance;
  }

  public void setApp(Javalin app) {
    this.app = app;
  }
}
