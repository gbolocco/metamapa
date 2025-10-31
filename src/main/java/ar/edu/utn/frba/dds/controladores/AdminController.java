package ar.edu.utn.frba.dds.controladores;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryDB;
import ar.edu.utn.frba.dds.servicios.ServicioFuentes;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController implements WithSimplePersistenceUnit {

  private ServicioFuentes servicioFuentes;

  public AdminController(ServicioFuentes servicioFuentes) {
    this.servicioFuentes = servicioFuentes;
  }
  public void mostrarDashboard(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("title", "Panel Admin");
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("user_id", ctx.sessionAttribute("user_id"));

    //model.put("colecciones", colecciones.mostrarColecciones());
    //model.put("solicitudes", solicitudes.mostrarSolicitudes());

    ctx.render("admin/dashboard.hbs", model);
  }
  public void mostrarFormColeccion(Context ctx) {
    List<Fuente> fuentes = servicioFuentes.getFuentes();

    Map<String, Object> model = new HashMap<>();
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    model.put("fuentes", fuentes);

    ctx.render("admin/coleccion.hbs", model);
  }

}