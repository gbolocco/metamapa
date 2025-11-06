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
import ar.edu.utn.frba.dds.servicios.ServicioUsuarios;
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

  public AdminController(ServicioFuentes servicioFuentes, ServicioUsuarios servicioUsuarios, ServicioColecciones servicioColecciones) {
    this.servicioFuentes = servicioFuentes;
    this.servicioUsuarios = servicioUsuarios;
    this.servicioColecciones = servicioColecciones;
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
      model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
      ctx.render("admin/usuarios.hbs", model);

    }catch (Exception e){
      e.printStackTrace();
      ctx.status(500).result("Error interno del servidor: " + e.getMessage());
    }

  }

  public void mostrarFuentes(Context ctx) {
    Map<String, Object> model = new HashMap<>();

    List<Fuente> fuentes = servicioFuentes.getFuentes();

    // Convertimos las fuentes a mapas con todas las propiedades necesarias
    List<Map<String, Object>> fuentesDTO = fuentes.stream()
        .map(f -> {
          Map<String, Object> map = new HashMap<>();
          map.put("id", f.getId());
          map.put("tipo_fuente", f.getTipoFuente());  // Se ejecuta el método acá
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


}