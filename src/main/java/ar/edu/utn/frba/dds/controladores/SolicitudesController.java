package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.UsuariosRepository;

import ar.edu.utn.frba.dds.servicios.ServicioSolicitudes;

import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;

public class SolicitudesController {

  private final ServicioSolicitudes servicioSolicitudes;

  public SolicitudesController(ServicioSolicitudes servicioSolicitudes) {
    this.servicioSolicitudes = servicioSolicitudes;
  }

  public void mostrarFormulario(Context ctx) {
    String hechoId = ctx.queryParam("hechoId");
    String tipoQuery = ctx.queryParam("tipo");
    String coleccionId = ctx.queryParam("coleccionId");
    Map<String, Object> model = new HashMap<>();
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("user_id", ctx.sessionAttribute("user_id"));

    if (hechoId != null) {
      model.put("hechoIdPreseleccionado", hechoId);
    }

    if (coleccionId != null) {
      model.put("coleccionId", coleccionId);
    }

    if (tipoQuery != null) {
      try {
        if (!tipoQuery.equalsIgnoreCase("CARGA")) {
          TipoSolicitud tipoSolicitud = TipoSolicitud.valueOf(tipoQuery.toUpperCase() + "_HECHO");
          model.put("tipoPreseleccionado", tipoSolicitud.toString());
          model.put("requiereJustificacion", tipoSolicitud == TipoSolicitud.ELIMINACION_HECHO);
        }
      } catch (IllegalArgumentException e) {
        // Tipo inválido, ignorar
      }
    }
    ctx.render("solicitud-form.hbs", model);
  }

  public void crear(Context ctx) {
    try {
      Long hechoId = Long.parseLong(ctx.formParam("hechoId"));
      Long userId = ctx.sessionAttribute("user_id");
      String tipoSolicitudStr = ctx.formParam("tipoSolicitud");
      String justificacion = ctx.formParam("justificacion");
      Long coleccionId = Long.parseLong(ctx.formParam("coleccionId"));

      TipoSolicitud tipoSolicitud = TipoSolicitud.valueOf(tipoSolicitudStr);
      var usuario = userId != null ? UsuariosRepository.INSTANCE.buscarPorId(userId) : null;

      var hecho = HechosRepository.getInstancia().buscar(hechoId);
      if (hecho == null) {
        String errorUrl = "/colecciones/" + coleccionId + "/hechos?error=" + "Hecho no encontrado.";
        ctx.redirect(errorUrl);
        return;
      }

      servicioSolicitudes.crearSolicitud(usuario, hecho, tipoSolicitud, justificacion);

      ctx.sessionAttribute("successMessage", "Solicitud creada exitosamente");
      String redirectUrl = "/colecciones/" + coleccionId + "/hechos?id=" + hechoId;
      ctx.redirect(redirectUrl);
    } catch (Exception e) {
      e.printStackTrace();

      Long coleccionId = Long.parseLong(ctx.formParam("coleccionId"));
      Long hechoId = Long.parseLong(ctx.formParam("hechoId"));
      ctx.sessionAttribute("errorMessage", "Error al crear solicitud: " + e.getMessage());
      String errorUrl = "/colecciones/" + coleccionId + "/hechos?id=" + hechoId;
      ctx.redirect(errorUrl);
    }
  }

}