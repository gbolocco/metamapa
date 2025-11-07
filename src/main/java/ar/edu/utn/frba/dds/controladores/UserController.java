package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.modelo.Rol;
import ar.edu.utn.frba.dds.modelo.Usuario;
import ar.edu.utn.frba.dds.servicios.ServicioUsuarios;
import ar.edu.utn.frba.dds.servicios.ServicioSolicitudes;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import io.javalin.http.Context;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {
  private ServicioUsuarios servicioUsuarios;
  private ServicioSolicitudes servicioSolicitudes;

  public UserController(ServicioUsuarios servicioUsuarios, ServicioSolicitudes servicioSolicitudes) {
    this.servicioUsuarios = servicioUsuarios;
    this.servicioSolicitudes = servicioSolicitudes;
  }

  public void actualizarRolUsuario(Context ctx) {
    try{
      Long id = Long.parseLong(ctx.pathParam("id"));
      System.out.println(id);
      Usuario usuario = servicioUsuarios.buscarPorId(id);

      RolUpdateRequest requestBody = ctx.bodyAsClass(RolUpdateRequest.class);
      String rol = requestBody.getRol();

      System.out.println("ID: " + id + ", Nuevo Rol: " + rol);


      if (rol == null) {
        return;
      }else if (rol.equals("ADMIN")) {
        usuario.setRol(Rol.ADMIN);
      }else if (rol.equals("USER")) {
        usuario.setRol(Rol.USER);
      }
      servicioUsuarios.actualizarUsuario(usuario);
      ctx.status(200);
    }catch(Exception e){
      e.printStackTrace();
      System.out.println("Error al actualizar rol");
      ctx.status(500).result("Error interno del servidor: " + e.getMessage());
    }
  }

  public void mostrarMisSolicitudes(Context ctx) {
    try {
      Long userId = ctx.sessionAttribute("user_id");
      if (userId == null) {
        ctx.redirect("/login");
        return;
      }
      
      List<Solicitud> solicitudesUsuario = servicioSolicitudes.obtenerSolicitudesPorUsuario(userId);
      
      List<java.util.Map<String, Object>> solicitudes = solicitudesUsuario.stream()
        .map(s -> {
          java.util.Map<String, Object> solicitudMap = new java.util.HashMap<>();
          solicitudMap.put("id", s.getId());
          solicitudMap.put("tipo", s.getTipoSolicitud().toString().replace("_HECHO", ""));
          solicitudMap.put("fecha", s.getFechaSolicitud().toString());
          solicitudMap.put("estado", s.getEstadoSolicitud().toString());
          solicitudMap.put("justificacion", s.getJustificacion() != null ? s.getJustificacion() : "Sin justificación");
          return solicitudMap;
        })
        .collect(Collectors.toList());
      
      java.util.Map<String, Object> model = new java.util.HashMap<>();
      model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
      model.put("rol", ctx.sessionAttribute("rol"));
      model.put("user_name", ctx.sessionAttribute("user_name"));
      model.put("user_id", userId);
      model.put("solicitudes", solicitudes);
      
      String success = ctx.queryParam("success");
      if ("solicitud_creada".equals(success)) {
        model.put("successMessage", "Solicitud generada exitosamente");
      }
      
      ctx.render("mis-solicitudes.hbs", model);
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error interno del servidor: " + e.getMessage());
    }
  }

  public void mostrarMisHechos(Context ctx) {
    try {
      Long userId = ctx.sessionAttribute("user_id");
      if (userId == null) {
        ctx.redirect("/login");
        return;
      }
      
      Usuario usuario = servicioUsuarios.buscarPorId(userId);
      List<Hecho> hechosUsuario = HechosRepository.getInstancia().buscarPorUsuario(usuario);
      
      List<java.util.Map<String, Object>> hechos = hechosUsuario.stream()
        .map(h -> {
          java.util.Map<String, Object> hechoMap = new java.util.HashMap<>();
          hechoMap.put("id", h.getId());
          hechoMap.put("titulo", h.getTitulo());
          hechoMap.put("descripcion", h.getDescripcion());
          hechoMap.put("categoria", h.getCategoria());
          hechoMap.put("fechaAcontecimiento", h.getFechaAcontecimiento().toString());
          hechoMap.put("fechaCarga", h.getFechaDeCarga().toString());
          hechoMap.put("estado", h.getEstadoHecho().toString());
          return hechoMap;
        })
        .collect(Collectors.toList());
      
      java.util.Map<String, Object> model = new java.util.HashMap<>();
      model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
      model.put("rol", ctx.sessionAttribute("rol"));
      model.put("user_name", ctx.sessionAttribute("user_name"));
      model.put("user_id", userId);
      model.put("hechos", hechos);
      
      ctx.render("mis-hechos.hbs", model);
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error interno del servidor: " + e.getMessage());
    }
  }

  public static class RolUpdateRequest {
    private String rol;

    public RolUpdateRequest() {}

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
  }
}
