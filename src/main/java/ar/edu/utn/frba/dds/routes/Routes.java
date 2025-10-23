package ar.edu.utn.frba.dds.routes;

import ar.edu.utn.frba.dds.controladores.ColeccionController;
import ar.edu.utn.frba.dds.controladores.HechosController;
import ar.edu.utn.frba.dds.controladores.LoginController;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

  private final Javalin app;

  // ✅ Constructor real (sin void)
  public Routes(Javalin app) {
    this.app = app;
  }

  // ✅ Método para configurar todas las rutas
  public void configureRoutes(HechosController hechoController,
                              LoginController loginController,
                              ColeccionController coleccionController) {

    app.routes(() -> {

      path("hechos", () -> {
        get(hechoController::listar);
        get("nuevo", hechoController::mostrarFormulario);
        get("mapa", hechoController::mostrarMapa);
        post(hechoController::crear);
        get("{hechoId}", hechoController::mostrar);
      });

      path("login", () -> {
        get(loginController::mostrarLogin);
        post(loginController::login);
      });

      path("colecciones", () -> {
        get(coleccionController::mostrarColecciones);
      });
    });
  }

  public void initialRouting() {
    app.get("/", ctx -> {
      Long userId = ctx.sessionAttribute("user_id");
      if (userId == null) {
        ctx.redirect("/login");
      } else {
        ctx.result("Bienvenido usuario ID " + userId);
      }
    });
  }
}
