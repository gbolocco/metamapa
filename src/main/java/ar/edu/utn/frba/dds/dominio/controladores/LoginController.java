package ar.edu.utn.frba.dds.dominio.controladores;

import ar.edu.utn.frba.dds.infraestructura.repositorios.RepositorioUsuarios;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LoginController implements Handler {
  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    try {
      var usuario = RepositorioUsuarios.INSTANCE.buscar(
          ctx.formParam("nombre"),
          ctx.formParam("password")
      );

      ctx.sessionAttribute("user_id", usuario.getId());
      ctx.redirect("/");

    } catch (Exception e) {
      Map<String, Object> model = new HashMap<>();
      //e.printStackTrace();

      ctx.redirect("/login?error=true");
    }
  }
}
