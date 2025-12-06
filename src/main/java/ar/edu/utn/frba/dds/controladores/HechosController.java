package ar.edu.utn.frba.dds.controladores;

import ar.edu.utn.frba.dds.compartido.DataFormatter;
import ar.edu.utn.frba.dds.dominio.hechos.*;
import ar.edu.utn.frba.dds.dominio.multimedia.ContenidoMultimedia;
import ar.edu.utn.frba.dds.dominio.multimedia.TipoContenido;
import ar.edu.utn.frba.dds.servicios.ServicioHechos;
import ar.edu.utn.frba.dds.servicios.ServicioMultimedia;
import ar.edu.utn.frba.dds.servicios.ServicioUsuarios;
import ar.edu.utn.frba.dds.modelo.Usuario;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import java.time.LocalDateTime;

public class HechosController implements WithSimplePersistenceUnit {
  private ServicioHechos servicioHechos;
  private ServicioUsuarios servicioUsuarios;
  private ServicioMultimedia servicioMultimedia;
  private final ObjectMapper mapper = new ObjectMapper();
  private DataFormatter formateador = new DataFormatter();

  public HechosController(ServicioHechos servicioHechos, ServicioUsuarios servicioUsuarios,
      ServicioMultimedia servicioMultimedia) {
    this.servicioHechos = servicioHechos;
    this.servicioUsuarios = servicioUsuarios;
    this.servicioMultimedia = servicioMultimedia;
  }

  public void listar(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("title", "Solicitudes");
    model.put("content", "View requests here...");
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("hechos", servicioHechos.mostrarHechos());

    servicioHechos.mostrarHechos().stream().forEach(hecho -> {
      if (hecho.getOrigenHecho() != null) {
        model.put("origenHechoFormateado", formateador.formatearOrigen(hecho.getOrigenHecho()));
      }

      if (hecho.getFechaAcontecimiento() != null) {
        model.put("fechaAcontecimientoFormateada", formateador.formatearFecha(hecho.getFechaAcontecimiento()));
      }

      if (hecho.getFechaDeCarga() != null) {
        model.put("fechaDeCargaFormateada", formateador.formatearFecha(hecho.getFechaDeCarga()));
      }
    });

    String success = ctx.queryParam("success");
    if ("hecho_creado".equals(success)) {
      model.put("successMessage", "Hecho creado exitosamente");
    }

    String error = ctx.queryParam("error");
    if (error != null) {
      model.put("errorMessage", "Error al crear solicitud: " + error);
    }

    ctx.render("hechos.hbs", model);
  }

  public void mostrarFormulario(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
    model.put("rol", ctx.sessionAttribute("rol"));
    model.put("user_name", ctx.sessionAttribute("user_name"));
    model.put("user_id", ctx.sessionAttribute("user_id"));
    ctx.render("hechos-form.hbs", model);
  }

  public void servirMultimedia(Context ctx) {
    try {
      // 1. Obtener el ID de la ruta (configurado en Routes.java como {id})
      Long id = Long.parseLong(ctx.pathParam("id"));

      // 2. Buscar la entidad en la base de datos
      ContenidoMultimedia multimedia = servicioMultimedia.buscarPorId(id);

      if (multimedia == null || multimedia.getDatosArchivo() == null) {
        ctx.status(404).result("Contenido multimedia no encontrado o vacío.");
        return;
      }

      byte[] datosArchivo = multimedia.getDatosArchivo();
      String tipoMime = multimedia.getTipoMime();

      // 3. Configurar la respuesta HTTP
      ctx.res().setContentLength(datosArchivo.length);

      // CRUCIAL: Establecer el Content-Type. Si no está, usa octet-stream.
      ctx.res().setContentType(tipoMime != null ? tipoMime : "application/octet-stream");

      // 4. Escribir los bytes directamente en el flujo de salida
      ctx.res().getOutputStream().write(datosArchivo);

      // Finalizar la solicitud sin llamar a ctx.result() ni ctx.render()

    } catch (NumberFormatException e) {
      ctx.status(400).result("ID de multimedia inválido.");
    } catch (Exception e) {
      // 🛑 REVISA TUS LOGS: Si esto se dispara, aquí está el error de conexión/data.
      e.printStackTrace();
      ctx.status(500).result("Error interno al servir el archivo: " + e.getMessage());
    }
  }

  public void crear(Context ctx) {
    try {
      String titulo = ctx.formParam("titulo");
      String descripcion = ctx.formParam("descripcion");
      String categoria = ctx.formParam("categoria");
      double lat = Double.parseDouble(ctx.formParam("lat"));
      double lon = Double.parseDouble(ctx.formParam("lon"));
      LocalDateTime fechaOcurrencia = LocalDateTime.parse(ctx.formParam("fechaOcurrencia"));
      UploadedFile fotoFile = ctx.uploadedFile("foto");
      UploadedFile videoFile = ctx.uploadedFile("video");

      Hecho hecho = new Hecho(
          titulo,
          descripcion,
          categoria,
          new Ubicacion(lat, lon),
          fechaOcurrencia,
          LocalDateTime.now(),
          OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);

      // Obtener usuario si está logueado y asignarlo al hecho
      Long userId = ctx.sessionAttribute("user_id");
      Usuario usuario = null;
      if (userId != null) {
        usuario = servicioUsuarios.buscarPorId(userId);
        if (usuario != null) {
          hecho.setUsuario(usuario);
        }
      }

      if (fotoFile != null && fotoFile.size() > 0) {
        // Necesitas una forma de obtener los bytes del UploadedFile.
        // El método content().readAllBytes() de InputStream es el más directo.
        byte[] datosFoto = fotoFile.content().readAllBytes();

        ContenidoMultimedia cmFoto = new ContenidoMultimedia();
        cmFoto.setDatosArchivo(datosFoto);
        cmFoto.setTipoMime(fotoFile.contentType());
        cmFoto.setTipoContenido(TipoContenido.IMAGEN);

        hecho.addContenidoMultimedia(cmFoto);
      }

      // 🎬 Lógica para procesar y adjuntar el Video
      if (videoFile != null && videoFile.size() > 0) {
        byte[] datosVideo = videoFile.content().readAllBytes();

        ContenidoMultimedia cmVideo = new ContenidoMultimedia();
        cmVideo.setDatosArchivo(datosVideo);
        cmVideo.setTipoMime(videoFile.contentType());
        cmVideo.setTipoContenido(TipoContenido.VIDEO);

        hecho.addContenidoMultimedia(cmVideo);
      }

      servicioHechos.cargarHecho(hecho);
      entityManager().getTransaction().begin();
      entityManager().flush();
      entityManager().getTransaction().commit();
      entityManager().clear();

      if (userId != null) {
        // Usuario logueado -> ir a mis solicitudes (o lista de hechos)
        // El usuario pidió que se carguen directamente, así que mejor ir a la lista de
        // hechos o mostrar éxito
        ctx.redirect("/hechos?success=hecho_creado");
      } else {
        // Usuario anónimo -> ir a home
        ctx.redirect("/home?success=hecho_creado");
      }
    } catch (Exception e) {
      e.printStackTrace();
      ctx.redirect("/hechos?error=" + e.getMessage());
    }
  }

  public void mostrar(Context ctx) {
    try {
      String idParam = ctx.pathParam("id");
      String hechoJson = ctx.queryParam("hecho");

      if (hechoJson == null || hechoJson.isEmpty()) {
        // Prioritize query param 'id' (used in collections view) over path param 'id'
        String queryId = ctx.queryParam("id");
        Long idToSearch = null;

        if (queryId != null && !queryId.isEmpty()) {
          try {
            idToSearch = Long.parseLong(queryId);
          } catch (NumberFormatException e) {
            System.err.println("⚠️ query id inválido: " + queryId);
          }
        } else if (idParam != null && !idParam.equals("null")) {
          try {
            idToSearch = Long.parseLong(idParam);
          } catch (NumberFormatException e) {
            System.err.println("⚠️ path id inválido: " + idParam);
          }
        }

        if (idToSearch != null) {
          var hechoFromDB = servicioHechos.buscar(idToSearch);
          if (hechoFromDB != null) {
            Map<String, Object> model = new HashMap<>();
            model.put("hecho", hechoFromDB);

            if (hechoFromDB.getOrigenHecho() != null) {
              model.put("origenFormateado", formateador.formatearOrigen(hechoFromDB.getOrigenHecho()));
            }

            if (hechoFromDB.getEstadoHecho() != null) {
              model.put("estadoFormateado", formateador.formatearEstado(hechoFromDB.getEstadoHecho()));
            }

            if (hechoFromDB.getFechaAcontecimiento() != null) {
              model.put("fechaFormateada", formateador.formatearFecha(hechoFromDB.getFechaAcontecimiento()));
            }

            model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
            model.put("rol", ctx.sessionAttribute("rol"));
            model.put("user_name", ctx.sessionAttribute("user_name"));
            model.put("user_id", ctx.sessionAttribute("user_id"));

            // Manejar mensajes de sesión
            String successMessage = ctx.sessionAttribute("successMessage");
            if (successMessage != null) {
              model.put("successMessage", successMessage);
              ctx.sessionAttribute("successMessage", null);
            }

            String errorMessage = ctx.sessionAttribute("errorMessage");
            if (errorMessage != null) {
              model.put("errorMessage", errorMessage);
              ctx.sessionAttribute("errorMessage", null);
            }

            ctx.render("hecho.hbs", model);
            return;
          }
        }
        ctx.status(400).result("Falta el parámetro 'hecho' o 'id' válido");
        return;
      }

      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      Hecho hecho = mapper.readValue(hechoJson, Hecho.class);

      Long id = null;
      try {
        if (idParam != null && !idParam.equals("null")) {
          id = Long.parseLong(idParam);
          var hechoFromDB = servicioHechos.buscar(id);
          if (hechoFromDB != null) {
            hecho.setContenidoMultimedia(hechoFromDB.getContenidoMultimedia());
          }
        }

        if (hecho.getContenidoMultimedia() == null) {
          hecho.setContenidoMultimedia(new ArrayList<>());
        }

      } catch (NumberFormatException e) {
        System.err.println("⚠️ id inválido: " + idParam);
      }

      Map<String, Object> model = new HashMap<>();
      model.put("hecho", hecho);

      if (hecho.getFechaAcontecimiento() != null) {
        model.put("fechaFormateada", formateador.formatearFecha(hecho.getFechaAcontecimiento()));
      }

      if (hecho.getOrigenHecho() != null) {
        model.put("origenFormateado", formateador.formatearOrigen(hecho.getOrigenHecho()));
      }

      if (hecho.getEstadoHecho() != null) {
        model.put("estadoFormateado", formateador.formatearEstado(hecho.getEstadoHecho()));
      }

      model.put("loggedIn", ctx.sessionAttribute("loggedIn"));
      model.put("rol", ctx.sessionAttribute("rol"));
      model.put("user_name", ctx.sessionAttribute("user_name"));
      model.put("user_id", ctx.sessionAttribute("user_id"));

      // Manejar mensajes de sesión
      String successMessage = ctx.sessionAttribute("successMessage");
      if (successMessage != null) {
        model.put("successMessage", successMessage);
        ctx.sessionAttribute("successMessage", null); // Limpiar mensaje
      }

      String errorMessage = ctx.sessionAttribute("errorMessage");
      if (errorMessage != null) {
        model.put("errorMessage", errorMessage);
        ctx.sessionAttribute("errorMessage", null); // Limpiar mensaje
      }

      // También manejar parámetros de URL (fallback)
      String success = ctx.queryParam("success");
      if ("hecho_creado".equals(success)) {
        model.put("successMessage", "Hecho creado exitosamente");
      }

      String error = ctx.queryParam("error");
      if (error != null) {
        model.put("errorMessage", "Error al crear solicitud: " + error);
      }

      ctx.render("hecho.hbs", model);

    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error interno del servidor: " + e.getMessage());
    }
  }

}
