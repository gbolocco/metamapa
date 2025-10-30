package ar.edu.utn.frba.dds.routes;

import ar.edu.utn.frba.dds.controladores.AdminController;
import ar.edu.utn.frba.dds.controladores.ColeccionController;
import ar.edu.utn.frba.dds.controladores.HechosController;
import ar.edu.utn.frba.dds.controladores.LoginController;
import ar.edu.utn.frba.dds.modelo.Rol;
import io.javalin.config.JavalinConfig;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
  public void configure(
    JavalinConfig config,
    HechosController hechos,
    LoginController login,
    ColeccionController coleccionController,
    AdminController admin) {

    config.router.apiBuilder(() -> {

      before(ctx -> {
        ctx.attribute("user_id", ctx.sessionAttribute("user_id"));
        ctx.attribute("user_name", ctx.sessionAttribute("nombre"));
        ctx.attribute("loggedIn", ctx.sessionAttribute("loggedIn"));
        ctx.attribute("rol", ctx.sessionAttribute("rol"));
      });

      before("/admin/*", ctx -> {
        Rol rol = ctx.sessionAttribute("rol");
        if (rol == null || rol != Rol.ADMIN) {
          ctx.redirect("/login");
          ctx.status(302);
          ctx.result("");
          return;
        }
      });

      path("/", () -> {
        get("/login",login::mostrarLogin);
      });

      path("/admin", () -> {
        get("/dashboard",admin::mostrarDashboard);
        });
      path("/hechos", () -> {
        get(hechos::listar);
        get("/nuevo", hechos::mostrarFormulario);

        post(hechos::crear);

      });

      path("/login", () -> {
        get(login::mostrarLogin);
        post(login::login);
      });

      path("/colecciones", () -> {
        get(coleccionController::mostrarColecciones);
        get("/{id}",coleccionController::mostrarColeccion);
        post("/{id}/hecho", hechos::mostrar);
      });

      post("/logout", login::logout);
    });
  }

}
