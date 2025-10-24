package ar.edu.utn.frba.dds.controladores;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryDB;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;

public class AdminController implements WithSimplePersistenceUnit {
  private ColeccionRepository colecciones = ColeccionRepository.getInstancia();
  private SolicitudesRepositoryDB solicitudes = SolicitudesRepositoryDB.getInstancia();

  public void mostrarDashboard(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("title", "Panel Admin");
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_id", ctx.sessionAttribute("user_id"));

    //model.put("colecciones", colecciones.mostrarColecciones());
    //model.put("solicitudes", solicitudes.mostrarSolicitudes());

    ctx.render("admin/dashboard.hbs", model);
  }

}