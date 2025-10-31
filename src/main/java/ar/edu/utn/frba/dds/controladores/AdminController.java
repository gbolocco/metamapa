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
import java.util.stream.Collectors;

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
      String fuentesSeleccionadasStr = ctx.formParam("fuentesSeleccionadas");

      List<Long> idsFuentes = new ArrayList<>();
      if (fuentesSeleccionadasStr != null && !fuentesSeleccionadasStr.isEmpty()) {
        idsFuentes = Arrays.stream(fuentesSeleccionadasStr.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Long::parseLong)
            .collect(Collectors.toList());
      }

      List<Filtro> filtros = new ArrayList<>();
      int i = 0;

      // Iterar por índice (i=0, i=1, i=2...) hasta que ya no exista el parámetro 'filtros[i][tipo]'
      while (ctx.formParam("filtros[" + i + "][tipo]") != null) {
        String tipo = ctx.formParam("filtros[" + i + "][tipo]");
        String campoStr = ctx.formParam("filtros[" + i + "][campo]");
        String valor = ctx.formParam("filtros[" + i + "][valor]");

        if (tipo != null && campoStr != null && valor != null) {
          // Asegúrate de que CampoDeHecho tenga los valores correctos (TITULO, DESCRIPCION, FECHAACONTENICIMIENTO)
          // Nota: Aquí se usa FECHAACONTENICIMIENTO porque así lo estás forzando en el JS para filtros de fecha.
          CampoDeHecho campo = CampoDeHecho.valueOf(campoStr.toUpperCase());

          switch (tipo) {
            case "texto":
              filtros.add(new FiltroContieneTexto(valor, campo));
              break;
            case "fechaDesde":
              // Parsear el valor como LocalDateTime
              filtros.add(new FiltroFechaDesde(LocalDateTime.parse(valor), campo));
              break;
            case "fechaHasta":
              // Parsear el valor como LocalDateTime
              filtros.add(new FiltroFechaHasta(LocalDateTime.parse(valor), campo));
              break;
            // Si hay un tipo desconocido, simplemente se ignora y no se agrega
          }
        }
        i++;
      }

      // Debug opcional: ver la cantidad de filtros que llegan
      System.out.println("Número de filtros procesados: " + filtros.size());
      // --- FIN OBTENER FILTROS ---

      // Debug opcional: ver los ids que llegan
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


      // Crear y guardar la colección (se agrega al repo cuando se instancia)
      Coleccion coleccion = new Coleccion(titulo, descripcion, new ArrayList<>(), fuente, "handle");
      ColeccionRepository.getInstancia().agregarColeccion(coleccion);

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