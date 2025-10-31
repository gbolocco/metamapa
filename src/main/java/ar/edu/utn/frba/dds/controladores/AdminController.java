package ar.edu.utn.frba.dds.controladores;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryDB;
import ar.edu.utn.frba.dds.modelo.Usuario;
import ar.edu.utn.frba.dds.servicios.ServicioFuentes;
import ar.edu.utn.frba.dds.servicios.ServicioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController implements WithSimplePersistenceUnit {

  private ServicioFuentes servicioFuentes;
  private ServicioUsuarios servicioUsuarios;

  public AdminController(ServicioFuentes servicioFuentes, ServicioUsuarios servicioUsuarios) {
    this.servicioFuentes = servicioFuentes;
    this.servicioUsuarios = servicioUsuarios;
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

  public void crearColeccion(Context ctx) {
    try {
      String titulo = ctx.formParam("titulo");
      String descripcion = ctx.formParam("descripcion");
      String consensoStr = ctx.formParam("consenso");
      List<Long> fuentesSeleccionadas = ctx.formParam("fuentesSeleccionadas");

      //TipoConsenso consenso = TipoConsenso.valueOf(consensoStr);

      List<Long> idsFuentes = new ArrayList<>();
      if (fuentesSeleccionadas != null && !fuentesSeleccionadas.isEmpty()) {
        //idsFuentes = Arrays.asList(fuentesSeleccionadas.split(","));
      }

      // --- Obtener filtros dinámicos ---
      List<Filtro> filtros = new ArrayList<>();

      // Buscar parámetros tipo filtros[0][tipo], filtros[0][campo], filtros[0][valor], etc.
      ctx.formParamMap().forEach((key, values) -> {
        if (key.startsWith("filtros[")) {
          String tipo = ctx.formParam(key.replaceAll("\\[.*", "[tipo]"));
          String campoStr = ctx.formParam(key.replaceAll("\\[.*", "[campo]"));
          String valor = ctx.formParam(key.replaceAll("\\[.*", "[valor]"));

          if (tipo != null && campoStr != null && valor != null) {
            CampoDeHecho campo = CampoDeHecho.valueOf(campoStr.toUpperCase());

            switch (tipo) {
              case "texto":
                filtros.add(new FiltroContieneTexto(valor, campo));
                break;
              case "fechaDesde":
                filtros.add(new FiltroFechaDesde(LocalDateTime.parse(valor), campo));
                break;
              case "fechaHasta":
                filtros.add(new FiltroFechaHasta(LocalDateTime.parse(valor), campo));
                break;
            }
          }
        }
      });

      // Crear la colección
      //Coleccion coleccion = new Coleccion();


      // Guardar (ejemplo usando repo o servicio)
      //ColeccionRepository repo = new ColeccionRepository();
      //repo.guardar(coleccion);

      ctx.redirect("/admin/dashboard");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error al crear la colección: " + e.getMessage());
    }
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

  public void mostrarUsuarios(Context ctx) {

    try{
      List<Usuario> usuarios = servicioUsuarios.getUsuarios();
      Map<String, Object> model = new HashMap<>();
      model.put("rol", ctx.sessionAttribute("rol"));
      model.put("user_name", ctx.sessionAttribute("user_name"));
      model.put("user_id", ctx.sessionAttribute("user_id"));
      model.put("usuarios", usuarios);
      ctx.render("admin/usuarios.hbs", model);
    }catch (Exception e){
      e.printStackTrace();
      ctx.status(500).result("Error interno del servidor: " + e.getMessage());
    }

  }

}