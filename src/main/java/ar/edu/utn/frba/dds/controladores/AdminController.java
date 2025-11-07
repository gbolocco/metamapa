package ar.edu.utn.frba.dds.controladores;
import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso.TipoConsenso;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteAgregadora;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;

import ar.edu.utn.frba.dds.infraestructura.repositorios.EstadisticasRepository;
import ar.edu.utn.frba.dds.servicios.ServicioColecciones;
import ar.edu.utn.frba.dds.servicios.ServicioFuentes;

import ar.edu.utn.frba.dds.servicios.ServicioSolicitudes;
import ar.edu.utn.frba.dds.servicios.ServicioUsuarios;

import ar.edu.utn.frba.dds.modelo.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.javalin.http.Context;

public class AdminController {

  private ServicioFuentes servicioFuentes;
  private ServicioUsuarios servicioUsuarios;
  private ServicioColecciones servicioColecciones;
  private ServicioSolicitudes servicioSolicitudes;

  public AdminController(ServicioFuentes servicioFuentes, ServicioUsuarios servicioUsuarios, ServicioColecciones servicioColecciones, ServicioSolicitudes servicioSolicitudes) {
    this.servicioFuentes = servicioFuentes;
    this.servicioUsuarios = servicioUsuarios;
    this.servicioColecciones = servicioColecciones;
    this.servicioSolicitudes = servicioSolicitudes;
  }

    public void mostrarDashboard(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Panel Admin");
        model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
        model.put("rol", ctx.sessionAttribute("rol"));
        model.put("user_name", ctx.sessionAttribute("user_name"));
        model.put("user_id", ctx.sessionAttribute("user_id"));

        List<Map<String, Object>> pendientes = EstadisticasRepository.getInstancia().getEstadisticasPendientes()
                .stream()
                .filter(e -> !e.fueCalculada())
                .map(e -> {
                    Map<String, Object> datos = new HashMap<>();
                    datos.put("tipo", e.getClass().getSimpleName());
                    datos.put("categoria", e.getCategoria());
                    datos.put("publica", e.getPublica());
                    return datos;
                })
                .toList();

        model.put("pendientes", pendientes);

        ctx.render("admin/dashboard.hbs", model);
    }



  public void crearColeccion(Context ctx) {
    try {
      String titulo = ctx.formParam("titulo");
      String descripcion = ctx.formParam("descripcion");
      String consensoStr = ctx.formParam("consenso");
      String fuentesSeleccionadasStr = ctx.formParam("fuentesSeleccionadas");


      List<Long> idsFuentes = new ArrayList<>();
      if (fuentesSeleccionadasStr != null && !fuentesSeleccionadasStr.isEmpty()) {
        idsFuentes = Arrays.stream(fuentesSeleccionadasStr.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Long::parseLong)
            .toList();
      }

      List<Filtro> filtros = new ArrayList<>();
      DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

      int i = 0;
      while (ctx.formParam("filtros[" + i + "][tipo]") != null) {
        String tipo = ctx.formParam("filtros[" + i + "][tipo]");
        String campoStr = ctx.formParam("filtros[" + i + "][campo]");
        String valor = ctx.formParam("filtros[" + i + "][valor]");

        if (tipo != null && campoStr != null && valor != null && !valor.isEmpty()) {
          CampoDeHecho campo;

          try {
            campo = CampoDeHecho.valueOf(campoStr.trim().toUpperCase());
          } catch (IllegalArgumentException e) {
            i++;
            continue;
          }

          switch (tipo) {
            case "texto":
              filtros.add(new FiltroContieneTexto(valor, campo));
              break;

            case "fechaDesde":
              try {
                filtros.add(new FiltroFechaDesde(LocalDateTime.parse(valor, formatter), CampoDeHecho.FECHA_ACONTECIMIENTO));
              } catch (DateTimeParseException e) {
                System.err.println("Error al parsear fechaDesde: " + valor);
              }
              break;

            case "fechaHasta":
              try {
                filtros.add(new FiltroFechaHasta(LocalDateTime.parse(valor, formatter), CampoDeHecho.FECHA_ACONTECIMIENTO));
              } catch (DateTimeParseException e) {
                System.err.println("Error al parsear fechaHasta: " + valor);
              }
              break;

            default:
              // Ignorar tipos desconocidos
              break;
          }
        }

        i++;
      }

      assert consensoStr != null;
      TipoConsenso tipoConsenso = switch (consensoStr) {
        case "ABSOLUTA" -> TipoConsenso.ABSOLUTA;
        case "MAYORIA_SIMPLE" -> TipoConsenso.MAYORIA_SIMPLE;
        case "MULTIPLES_MENCIONES" -> TipoConsenso.MULTIPLES_MENCIONES;
        default -> throw new IllegalArgumentException("Tipo de consenso desconocido: " + consensoStr);
      };

      Fuente fuente;

      if (idsFuentes.size() > 1) {

        List<Fuente> fuentesInput = new ArrayList<>();

        idsFuentes.forEach(idFuente -> {
          fuentesInput.add(servicioFuentes.buscar(idFuente));
          System.out.println("Fuentes seleccionadas IDs: " + idFuente);
        });

        fuente = new FuenteAgregadora(fuentesInput);
        servicioFuentes.guardarFuente(fuente);

      }else {
        fuente = servicioFuentes.buscar(idsFuentes.get(0));
      }

      Coleccion coleccion = new Coleccion(titulo, descripcion, filtros, fuente, "handle",tipoConsenso );

      servicioColecciones.guardarColeccion(coleccion);

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
      model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
      ctx.render("admin/usuarios.hbs", model);

    }catch (Exception e){
      e.printStackTrace();
      ctx.status(500).result("Error interno del servidor: " + e.getMessage());
    }

  }

  public void mostrarSolicitudes(Context ctx) {
    String tipo = ctx.queryParam("tipo");

    if (tipo != null) {
      switch (tipo) {
        case "carga":
          mostrarSolicitudesCarga(ctx);
          return;
        case "eliminacion":
          mostrarSolicitudesEliminacion(ctx);
          return;
        case "modificacion":
          mostrarSolicitudesModificacion(ctx);
          return;
        case "todas":
          mostrarTodasLasSolicitudes(ctx);
          return;
      }
    }

    mostrarTodasLasSolicitudes(ctx);
  }

  private void mostrarSolicitudesCarga(Context ctx) {
    List<Solicitud> solicitudesRepo = servicioSolicitudes.obtenerSolicitudesPendientesPorTipo(TipoSolicitud.CARGA_HECHO);

    List<Map<String, Object>> solicitudes;
    if (solicitudesRepo.isEmpty()) {
      solicitudes = List.of(
        Map.of("id", 101, "titulo", "Solicitud de Carga - Terremoto Mendoza", "fecha", "2025-10-30 09:15", "justificacion", "Solicitud para cargar nuevo registro de terremoto de magnitud 5.8 en Mendoza detectado por estación sísmica local.", "solicitante", "Instituto Sismológico", "tipo", "Carga"),
        Map.of("id", 102, "titulo", "Solicitud de Carga - Inundación Litoral", "fecha", "2025-10-29 16:30", "justificacion", "Solicitud para cargar registro de inundación en zona del litoral argentino debido a crecida del río Paraná.", "solicitante", "Servicio Meteorológico", "tipo", "Carga")
      );
    } else {
      solicitudes = solicitudesRepo.stream()
        .map(s -> {
          Map<String, Object> solicitudMap = new HashMap<>();
          solicitudMap.put("id", s.getId());
          solicitudMap.put("titulo", "Solicitud de Carga - " + (s.getRepresentacionDeHecho() != null ? s.getRepresentacionDeHecho().getId() : "N/A"));
          solicitudMap.put("fecha", s.getFechaSolicitud().toString());
          solicitudMap.put("justificacion", s.getJustificacion() != null ? s.getJustificacion() : "Sin justificación");
          solicitudMap.put("solicitante", s.getUsuario() != null ? s.getUsuario().getNombre() : null);
          solicitudMap.put("tipo", "Carga");
          return solicitudMap;
        })
        .collect(Collectors.toList());
    }

    renderizarSolicitudes(ctx, solicitudes, "Solicitudes de Carga", "carga");
  }

  private void mostrarSolicitudesEliminacion(Context ctx) {
    List<Solicitud> solicitudesRepo = servicioSolicitudes.obtenerSolicitudesPendientesPorTipo(TipoSolicitud.ELIMINACION_HECHO);
    System.out.println("DEBUG AdminController: Solicitudes del repo: " + solicitudesRepo.size());

    List<Map<String, Object>> todasSolicitudes = solicitudesRepo.stream()
        .map(s -> {
          Map<String, Object> solicitudMap = new HashMap<>();
          solicitudMap.put("id", s.getId());
          solicitudMap.put("titulo", "Solicitud de " + s.getTipoSolicitud());
          solicitudMap.put("fecha", s.getFechaSolicitud().toString());
          solicitudMap.put("justificacion", s.getJustificacion() != null ? s.getJustificacion() : "Sin justificación");
          solicitudMap.put("solicitante", s.getUsuario() != null ? s.getUsuario().getNombre() : null);
          solicitudMap.put("tipo", s.getTipoSolicitud().toString());
          return solicitudMap;
        })
        .collect(Collectors.toList());

    String pageParam = ctx.queryParam("page");
    int page = pageParam != null ? Integer.parseInt(pageParam) : 1;
    int pageSize = 10;
    int totalSolicitudes = todasSolicitudes.size();
    int totalPages = (int) Math.ceil((double) totalSolicitudes / pageSize);

    int startIndex = (page - 1) * pageSize;
    int endIndex = Math.min(startIndex + pageSize, totalSolicitudes);

    List<Map<String, Object>> solicitudesPagina = todasSolicitudes.subList(startIndex, endIndex);

    Map<String, Object> model = new HashMap<>();
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    model.put("solicitudes", solicitudesPagina);
    model.put("currentPage", page);
    model.put("totalPages", totalPages);
    model.put("hasPrevious", page > 1);
    model.put("hasNext", page < totalPages);
    model.put("previousPage", page - 1);
    model.put("nextPage", page + 1);

    renderizarSolicitudes(ctx, solicitudesPagina, "Solicitudes de Eliminación", "eliminacion");
  }

  private void mostrarSolicitudesModificacion(Context ctx) {
    List<Solicitud> solicitudesRepo = servicioSolicitudes.obtenerSolicitudesPendientesPorTipo(TipoSolicitud.MODIFICACION_HECHO);

    List<Map<String, Object>> solicitudes;
    if (solicitudesRepo.isEmpty()) {
      solicitudes = List.of(
        Map.of("id", 201, "titulo", "Solicitud de Modificación - Huracán Categoría", "fecha", "2025-10-30 11:45", "justificacion", "Solicitud para modificar la categoría del huracán registrado, se detectó error en la clasificación inicial.", "solicitante", "Centro Meteorológico", "tipo", "Modificación"),
        Map.of("id", 202, "titulo", "Solicitud de Modificación - Coordenadas Incendio", "fecha", "2025-10-29 14:20", "justificacion", "Solicitud para corregir las coordenadas del incendio forestal, se registraron coordenadas incorrectas.", "solicitante", "Bomberos Voluntarios", "tipo", "Modificación")
      );
    } else {
      solicitudes = solicitudesRepo.stream()
        .map(s -> {
          Map<String, Object> solicitudMap = new HashMap<>();
          solicitudMap.put("id", s.getId());
          solicitudMap.put("titulo", "Solicitud de Modificación - " + (s.getRepresentacionDeHecho() != null ? s.getRepresentacionDeHecho().getId() : "N/A"));
          solicitudMap.put("fecha", s.getFechaSolicitud().toString());
          solicitudMap.put("justificacion", s.getJustificacion() != null ? s.getJustificacion() : "Sin justificación");
          solicitudMap.put("solicitante", s.getUsuario() != null ? s.getUsuario().getNombre() : null);
          solicitudMap.put("tipo", "Modificación");
          return solicitudMap;
        })
        .collect(Collectors.toList());
    }

    renderizarSolicitudes(ctx, solicitudes, "Solicitudes de Modificación", "modificacion");
  }

  private void mostrarTodasLasSolicitudes(Context ctx) {
    List<Solicitud> solicitudesRepo = servicioSolicitudes.obtenerTodasLasSolicitudes();

    List<Map<String, Object>> todasSolicitudes = new ArrayList<>();

    todasSolicitudes.addAll(solicitudesRepo.stream()
      .map(s -> {
        Map<String, Object> solicitudMap = new HashMap<>();
        solicitudMap.put("id", s.getId());
        solicitudMap.put("titulo", "Solicitud de " + s.getTipoSolicitud().toString().replace("_", " "));
        solicitudMap.put("fecha", s.getFechaSolicitud().toString());
        solicitudMap.put("solicitante", s.getUsuario() != null ? s.getUsuario().getNombre() : null);
        solicitudMap.put("tipo", s.getTipoSolicitud().toString().replace("_", " "));
        solicitudMap.put("estado", s.getEstadoSolicitud().toString());
        return solicitudMap;
      })
      .collect(Collectors.toList()));

    if (todasSolicitudes.isEmpty()) {
      todasSolicitudes.addAll(List.of(
        Map.of("id", 101, "titulo", "Solicitud de Carga - Terremoto Mendoza", "fecha", "2025-10-30 09:15", "solicitante", "Instituto Sismológico", "tipo", "Carga", "estado", "Pendiente"),
        Map.of("id", 102, "titulo", "Solicitud de Eliminación - Inundación Errónea", "fecha", "2025-10-29 16:30", "solicitante", "Servicio Meteorológico", "tipo", "Eliminación", "estado", "Pendiente")
      ));
    }

    Map<String, Object> model = new HashMap<>();
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    model.put("todasSolicitudes", todasSolicitudes);
    model.put("titulo", "Todas las Solicitudes");
    model.put("mostrarTabla", true);
    model.put("mostrarBotones", true);
    model.put("tipoActivo", "todas");

    ctx.render("solicitudes.hbs", model);
  }

  private void renderizarSolicitudes(Context ctx, List<Map<String, Object>> solicitudes, String titulo, String tipoActivo) {
    String pageParam = ctx.queryParam("page");
    int page = pageParam != null ? Integer.parseInt(pageParam) : 1;
    int pageSize = 10;
    int totalSolicitudes = solicitudes.size();
    int totalPages = (int) Math.ceil((double) totalSolicitudes / pageSize);

    int startIndex = (page - 1) * pageSize;
    int endIndex = Math.min(startIndex + pageSize, totalSolicitudes);

    List<Map<String, Object>> solicitudesPagina = solicitudes.subList(startIndex, endIndex);

    Map<String, Object> model = new HashMap<>();
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    model.put("solicitudes", solicitudesPagina);
    model.put("currentPage", page);
    model.put("totalPages", totalPages);
    model.put("hasPrevious", page > 1);
    model.put("hasNext", page < totalPages);
    model.put("previousPage", page - 1);
    model.put("nextPage", page + 1);
    model.put("titulo", titulo);
    model.put("mostrarBotones", true);
    model.put("tipoActivo", tipoActivo);

    ctx.render("solicitudes.hbs", model);
  }

  public void confirmar(Context ctx) {
    try {
      Long solicitudId = Long.parseLong(ctx.pathParam("id"));
      servicioSolicitudes.confirmarSolicitud(solicitudId);
      ctx.status(200).result("Solicitud confirmada exitosamente");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error al confirmar la solicitud");
    }
  }

  public void rechazar(Context ctx) {
    try {
      Long solicitudId = Long.parseLong(ctx.pathParam("id"));
      servicioSolicitudes.rechazarSolicitud(solicitudId);
      ctx.status(200).result("Solicitud rechazada exitosamente");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error al rechazar la solicitud");
    }
  }

  public void mostrarFuentes(Context ctx) {
    Map<String, Object> model = new HashMap<>();

    List<Fuente> fuentes = servicioFuentes.getFuentes();

    for (Fuente fuente : fuentes) {
      System.out.println(fuente.getId());
    }

    // Convertimos las fuentes a mapas con todas las propiedades necesarias
    List<Map<String, Object>> fuentesDTO = fuentes.stream()
        .map(f -> {
          Map<String, Object> map = new HashMap<>();
          map.put("id", f.getId());
          map.put("tipo_fuente", f.getTipoFuente());
          map.put("url",f.getUrl());
          return map;
        })
        .collect(Collectors.toList());

    // Atributos de sesión
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));

    // Pasamos la lista procesada
    model.put("fuentes", fuentesDTO);


    ctx.render("admin/fuentes.hbs", model);
  }


  public void eliminarFuente(Context ctx) {
    try {
      Long idFuente = Long.valueOf(ctx.pathParam("id"));
      servicioFuentes.eliminar(idFuente);
      ctx.status(200);
    }catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error al eliminar la fuente");
    }
  }


  public void crearFuente(Context ctx) {
    try{
      Map<String, Object> body = ctx.bodyAsClass(Map.class);

      String url = body.get("url") != null ? String.valueOf(body.get("url")) : null;
      String tipo = String.valueOf(body.get("tipo_fuente"));
      String componentes = body.get("componentes") != null ? String.valueOf(body.get("componentes")) : null;

      Fuente fuenteCreada = servicioFuentes.crearFuente(url,tipo,componentes);

      Map<String, Object> response = new HashMap<>();
      response.put("id", fuenteCreada.getId());
      response.put("tipo_fuente", fuenteCreada.getTipoFuente().name());
      response.put("url", fuenteCreada.getUrl());

      ctx.json(response);
      ctx.status(200);
    }catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error al crear la fuente");
    }
  }


}