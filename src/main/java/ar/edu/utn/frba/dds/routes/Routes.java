package ar.edu.utn.frba.dds.routes;

import ar.edu.utn.frba.dds.controladores.*;
import ar.edu.utn.frba.dds.modelo.Rol;
import io.javalin.config.JavalinConfig;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
  public void configure(
    JavalinConfig config,
    HechosController hechos,
    LoginController login,
    ColeccionController coleccionController,
    UserController userController,
    AdminController admin,
    SolicitudesController solicitudesController,
    HomeController homeController,
    EstadisticaController estadisticController
    ) {

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
          ctx.redirect("/login?redirect=" + ctx.path());
          ctx.status(302);
          ctx.result("");
          return;
        }
      });

      get("/", ctx -> ctx.redirect("/home"));
      get("/home", homeController::mostrarHome);

      get("/multimedia/{id}", hechos::servirMultimedia);

      path("/", () -> {
          get("/login",login::mostrarLogin);
          get("/estadisticas" ,estadisticController::mostrarEstadisticas);
          post("/estadisticas",estadisticController::crearEstadistica);
          post("/estadisticas/descargar", estadisticController::descargarSeleccionadas);
          post("/estadisticas/calcular",estadisticController::calcularEstadisticas);
      });

      path("/admin", () -> {
        get("/dashboard",admin::mostrarDashboard);
        get("/coleccion",admin::mostrarFormColeccion);
        post("/coleccion",admin::crearColeccion);
        get("/usuarios",admin::mostrarUsuarios);
        get("/solicitudes", admin::mostrarSolicitudes);
        post("/solicitudes/{id}/confirmar", admin::confirmar);
        post("/solicitudes/{id}/rechazar", admin::rechazar);
        get("/fuentes", admin::mostrarFuentes);
          delete("/fuentes/{id}", admin::eliminarFuente);
          post("/fuentes/nueva", admin::crearFuente);
        });

      path("usuarios",() -> {
        put("/{id}/rol",userController::actualizarRolUsuario);
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
      
      get("/registro", login::mostrarRegistro);
      post("/registro", login::registrar);

      path("/colecciones", () -> {
        get(coleccionController::mostrarColecciones);
        get("/{id}",coleccionController::mostrarColeccion);
        get("/{id}/hechos", hechos::mostrar);
      });

      path("/solicitudes", () -> {
        get(solicitudesController::mostrarFormulario);
        post(solicitudesController::crear);
      });

      post("/logout", login::logout);
      
      before("/mis-solicitudes", ctx -> {
        if (ctx.sessionAttribute("user_id") == null) {
          ctx.redirect("/login?redirect=" + ctx.path());
          return;
        }
      });
      
      before("/mis-hechos", ctx -> {
        if (ctx.sessionAttribute("user_id") == null) {
          ctx.redirect("/login?redirect=" + ctx.path());
          return;
        }
      });
      
      get("/mis-solicitudes", userController::mostrarMisSolicitudes);
      get("/mis-hechos", userController::mostrarMisHechos);
      
      
    });
  }

}
