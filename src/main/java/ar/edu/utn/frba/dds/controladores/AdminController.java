package ar.edu.utn.frba.dds.controladores;
import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.filtros.CampoDeHecho;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroContieneTexto;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.dominio.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteAgregadora;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepositoryMemory;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryDB;
import ar.edu.utn.frba.dds.modelo.Usuario;
import ar.edu.utn.frba.dds.servicios.ServicioColecciones;
import ar.edu.utn.frba.dds.servicios.ServicioFuentes;
import ar.edu.utn.frba.dds.servicios.ServicioSolicitudes;
import ar.edu.utn.frba.dds.servicios.ServicioUsuarios;
import ar.edu.utn.frba.dds.dominio.solicitudes.Solicitud;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    //model.put("colecciones", colecciones.mostrarColecciones());
    //model.put("solicitudes", solicitudes.mostrarSolicitudes());

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
            // Aseguramos mayúsculas y eliminamos espacios
            campo = CampoDeHecho.valueOf(campoStr.trim().toUpperCase());
          } catch (IllegalArgumentException e) {
            // Si el campo no existe en el enum, lo saltamos
            i++;
            continue;
          }

          switch (tipo) {
            case "texto":
              filtros.add(new FiltroContieneTexto(valor, campo));
              break;

            case "fechaDesde":
              try {
                filtros.add(new FiltroFechaDesde(LocalDateTime.parse(valor, formatter), campo));
              } catch (DateTimeParseException e) {
                System.err.println("Error al parsear fechaDesde: " + valor);
              }
              break;

            case "fechaHasta":
              try {
                filtros.add(new FiltroFechaHasta(LocalDateTime.parse(valor, formatter), campo));
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

      // Debug opcional: ver la cantidad de filtros que llegan
      System.out.println("Número de filtros procesados: " + filtros.size());
      filtros.forEach(f -> System.out.println(f.getClass().getSimpleName()));
      System.out.println("Fuentes seleccionadas IDs: " + idsFuentes);

      Fuente fuente;

      if (idsFuentes.size() > 1) {

        List<Fuente> fuentesInput = new ArrayList<>();

        idsFuentes.forEach(idFuente -> {
          fuentesInput.add(servicioFuentes.buscar(idFuente));
          System.out.println("Fuentes seleccionadas IDs: " + idFuente);
        });

        fuente = new FuenteAgregadora(fuentesInput);
      }else {
        fuente = servicioFuentes.buscar(idsFuentes.get(0));
      }


      servicioFuentes.guardarFuente(fuente);
      servicioColecciones.guardarColeccion(new Coleccion(titulo, descripcion, filtros, fuente, "handle"));


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


  public void mostrarSolicitudes(Context ctx) {
    List<Solicitud> solicitudesRepo = servicioSolicitudes.obtenerSolicitudes();
    System.out.println("DEBUG AdminController: Solicitudes del repo: " + solicitudesRepo.size());
    
    List<Map<String, Object>> todasSolicitudes;
    if (solicitudesRepo.isEmpty()) {
      todasSolicitudes = List.of(
      Map.of("id", 1, "titulo", "Solicitud de Eliminación - Terremoto Duplicado", "fecha", "2025-10-29 14:30", "justificacion", "Solicitud para eliminar registro duplicado del terremoto de magnitud 6.2 en Mendoza.", "solicitante", "Juan Pérez", "tipo", "Eliminación"),
      Map.of("id", 2, "titulo", "Solicitud de Eliminación - Inundación Errónea", "fecha", "2025-10-29 10:15", "justificacion", "Solicitud para eliminar registro de inundación que fue cargado por error.", "solicitante", "María González", "tipo", "Eliminación"),
      Map.of("id", 3, "titulo", "Solicitud de Eliminación - Incendio Desactualizado", "fecha", "2025-10-28 16:45", "justificacion", "Solicitud para eliminar registro de incendio forestal en Córdoba.", "solicitante", "Carlos Rodríguez", "tipo", "Eliminación"),
      Map.of("id", 4, "titulo", "Solicitud de Eliminación - Tornado Falso", "fecha", "2025-10-27 09:20", "justificacion", "Solicitud para eliminar registro de tornado que nunca ocurrió.", "solicitante", "Ana Martínez", "tipo", "Eliminación"),
      Map.of("id", 5, "titulo", "Solicitud de Eliminación - Granizada Duplicada", "fecha", "2025-10-26 15:30", "justificacion", "Solicitud para eliminar registro duplicado de granizada en Buenos Aires.", "solicitante", "Luis García", "tipo", "Eliminación"),
      Map.of("id", 6, "titulo", "Solicitud de Eliminación - Sequía Incorrecta", "fecha", "2025-10-25 11:45", "justificacion", "Solicitud para eliminar registro de sequía con datos incorrectos.", "solicitante", "Elena Fernández", "tipo", "Eliminación"),
      Map.of("id", 7, "titulo", "Solicitud de Eliminación - Avalancha Test", "fecha", "2025-10-24 13:15", "justificacion", "Solicitud para eliminar registro de avalancha creado para pruebas.", "solicitante", "Pedro Sánchez", "tipo", "Eliminación"),
      Map.of("id", 8, "titulo", "Solicitud de Eliminación - Huracán Obsoleto", "fecha", "2025-10-23 08:30", "justificacion", "Solicitud para eliminar registro de huracán con información obsoleta.", "solicitante", "Carmen López", "tipo", "Eliminación"),
      Map.of("id", 9, "titulo", "Solicitud de Eliminación - Tsunami Erróneo", "fecha", "2025-10-22 17:00", "justificacion", "Solicitud para eliminar registro de tsunami que fue una falsa alarma.", "solicitante", "Roberto Díaz", "tipo", "Eliminación"),
      Map.of("id", 10, "titulo", "Solicitud de Eliminación - Erupción Duplicada", "fecha", "2025-10-21 12:20", "justificacion", "Solicitud para eliminar registro duplicado de erupción volcánica.", "solicitante", "Sofía Ruiz", "tipo", "Eliminación"),
      Map.of("id", 11, "titulo", "Solicitud de Eliminación - Tormenta Falsa", "fecha", "2025-10-20 14:45", "justificacion", "Solicitud para eliminar registro de tormenta que no ocurrió.", "solicitante", "Miguel Torres", "tipo", "Eliminación"),
      Map.of("id", 12, "titulo", "Solicitud de Eliminación - Deslizamiento Test", "fecha", "2025-10-19 10:30", "justificacion", "Solicitud para eliminar registro de deslizamiento creado para testing.", "solicitante", "Laura Morales", "tipo", "Eliminación"),
      Map.of("id", 13, "titulo", "Solicitud de Eliminación - Helada Incorrecta", "fecha", "2025-10-18 16:15", "justificacion", "Solicitud para eliminar registro de helada con datos erróneos.", "solicitante", "Diego Vargas", "tipo", "Eliminación"),
      Map.of("id", 14, "titulo", "Solicitud de Eliminación - Viento Duplicado", "fecha", "2025-10-17 09:45", "justificacion", "Solicitud para eliminar registro duplicado de vientos fuertes.", "solicitante", "Patricia Jiménez", "tipo", "Eliminación"),
        Map.of("id", 15, "titulo", "Solicitud de Eliminación - Niebla Obsoleta", "fecha", "2025-10-16 11:00", "justificacion", "Solicitud para eliminar registro de niebla con información desactualizada.", "solicitante", "Andrés Castro", "tipo", "Eliminación")
      );
    } else {
      todasSolicitudes = solicitudesRepo.stream()
        .map(s -> {
          Map<String, Object> solicitudMap = new HashMap<>();
          solicitudMap.put("id", s.getId());
          solicitudMap.put("titulo", "Solicitud de " + s.getTipoSolicitud());
          solicitudMap.put("fecha", s.getFechaSolicitud().toString());
          solicitudMap.put("justificacion", s.getJustificacion() != null ? s.getJustificacion() : "Sin justificación");
          solicitudMap.put("solicitante", "Hecho: " + (s.getRepresentacionDeHecho() != null ? s.getRepresentacionDeHecho().getId() : "N/A"));
          solicitudMap.put("tipo", s.getTipoSolicitud().toString());
          return solicitudMap;
        })
        .collect(Collectors.toList());
    }
    
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
    
    ctx.render("solicitudes.hbs", model);
  }

}