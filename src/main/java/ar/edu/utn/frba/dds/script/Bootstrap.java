package ar.edu.utn.frba.dds.script;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso.Absoluta;
import ar.edu.utn.frba.dds.dominio.colecciones.algoritmosConsenso.TipoConsenso;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import ar.edu.utn.frba.dds.dominio.multimedia.ContenidoMultimedia;
import ar.edu.utn.frba.dds.dominio.multimedia.TipoContenido;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepository;
import ar.edu.utn.frba.dds.modelo.Rol;
import ar.edu.utn.frba.dds.modelo.Usuario;
import ar.edu.utn.frba.dds.infraestructura.repositorios.UsuariosRepository;
import ar.edu.utn.frba.dds.servicios.ServicioSolicitudes;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDateTime;
import java.util.List;

public class Bootstrap implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    new Bootstrap().init();
  }

  public void init() {
    var fuente = new FuenteDinamica();
    entityManager().persist(fuente);

    Path pathImagen = Paths.get("./multimedia-examples/WhatsApp Image 2024-05-06 at 12.04.51 PM.jpeg");
    Path pathVideo = Paths.get("./multimedia-examples/VID-20170211-WA0014.mp4");
    var fuenteEstatica = new FuenteEstatica("./datos/desastres_naturales_processed.csv",new LectorCsv());


      List<Hecho> hechos = List.of(
              new Hecho("Derrame de petróleo en el río","Se detectó un derrame de petróleo cerca del puerto de Campana.","Contaminación",new Ubicacion(-34.169,-58.959),LocalDateTime.of(2025,11,1,8,30),LocalDateTime.of(2025,11,3,14,45),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
              new Hecho("Corte de luz prolongado","Vecinos de La Plata reportaron falta de suministro eléctrico por más de 10 horas.","Infraestructura",new Ubicacion(-34.921,-57.954),LocalDateTime.of(2025,10,29,19,15),LocalDateTime.of(2025,10,30,6,50),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
              new Hecho("Basural clandestino","Se encontró un depósito ilegal de residuos cerca del Riachuelo.","Contaminación",new Ubicacion(-34.674,-58.392),LocalDateTime.of(2025,11,4,10,20),LocalDateTime.of(2025,11,5,9,10),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
              new Hecho("Hundimiento de calzada","Parte de la avenida Córdoba presenta hundimientos que dificultan el tránsito.","Infraestructura",new Ubicacion(-34.601,-58.384),LocalDateTime.of(2025,11,2,7,0),LocalDateTime.of(2025,11,6,13,30),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
              new Hecho("Brote de dengue","Se confirmaron nuevos casos de dengue en la ciudad de Resistencia.","Salud",new Ubicacion(-27.451,-58.986),LocalDateTime.of(2025,10,25,9,45),LocalDateTime.of(2025,11,1,18,0),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
              new Hecho("Robo a mano armada","Comerciantes de Córdoba denuncian una serie de robos en locales del centro.","Seguridad",new Ubicacion(-31.420,-64.188),LocalDateTime.of(2025,11,5,21,10),LocalDateTime.of(2025,11,6,23,50),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
              new Hecho("Contaminación por humo","Incendios rurales generan humo que afecta la calidad del aire en Rosario.","Contaminación",new Ubicacion(-32.958,-60.639),LocalDateTime.of(2025,11,3,15,30),LocalDateTime.of(2025,11,7,11,0),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
              new Hecho("Caída de poste eléctrico","Un poste cayó sobre la vereda en Mendoza capital, bloqueando el paso.","Infraestructura",new Ubicacion(-32.890,-68.845),LocalDateTime.of(2025,10,31,17,40),LocalDateTime.of(2025,11,2,8,15),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
              new Hecho("Asalto en transporte público","Un pasajero fue asaltado en un colectivo de la línea 60 en Vicente López.","Seguridad",new Ubicacion(-34.5601, -58.4624),LocalDateTime.of(2025,11,5,22,5),LocalDateTime.of(2025,11,6,0,30),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
              new Hecho("Clínica clausurada por falta de higiene","El Ministerio de Salud clausuró una clínica en Tucumán por incumplir normas sanitarias.","Salud",new Ubicacion(-26.808,-65.217),LocalDateTime.of(2025,11,4,10,0),LocalDateTime.of(2025,11,6,12,45),OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
              new Hecho("Prueba1", "Prueba1", "Prueba1", new Ubicacion(30.2,30.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
              new Hecho("Prueba2", "Prueba2", "Prueba2", new Ubicacion(20.2,10.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.FUENTE_PROXY)
      );



      withTransaction(() -> {
      var usuarioFeli = UsuariosRepository.INSTANCE.buscarPorNombre("feli");
      var usuarioDani = UsuariosRepository.INSTANCE.buscarPorNombre("dani");
      
      if (usuarioFeli.isEmpty()) {
        var usuarios = Arrays.asList(
            new Usuario("feli", "feli", Rol.USER),
            new Usuario("dani", "dani", Rol.USER),
            new Usuario("umi", "umi", Rol.ADMIN)
        );
        usuarios.forEach((usuario) -> {
          UsuariosRepository.INSTANCE.registrar(usuario);
        });
        
        usuarioFeli = UsuariosRepository.INSTANCE.buscarPorNombre("feli");
        usuarioDani = UsuariosRepository.INSTANCE.buscarPorNombre("dani");
      }
      try {
        byte[] datosImagenReal = Files.readAllBytes(pathImagen);
        byte[] datosVideoReal = Files.readAllBytes(pathVideo);

        hechos.forEach(hecho -> {
          ContenidoMultimedia cmImagen = new ContenidoMultimedia();
          cmImagen.setDatosArchivo(datosImagenReal); // <-- Datos reales
          cmImagen.setTipoMime("image/jpeg");
          cmImagen.setTipoContenido(TipoContenido.IMAGEN);
          hecho.addContenidoMultimedia(cmImagen);

          ContenidoMultimedia cmVideo = new ContenidoMultimedia();
          cmVideo.setDatosArchivo(datosVideoReal); // Asignamos el byte array
          cmVideo.setTipoMime("video/mp4");
          cmVideo.setTipoContenido(TipoContenido.VIDEO);
          hecho.addContenidoMultimedia(cmVideo);
        });

        hechos.forEach(hecho -> entityManager().persist(hecho));

      } catch (IOException e) {
        System.err.println("Error al leer el archivo de prueba: " + e.getMessage());
      }

      var colecciones = Arrays.asList(
          new Coleccion(
              "Grandes Batallas Medievales",
              "Análisis detallado de las estrategias y consecuencias de los conflictos bélicos más importantes de la Edad Media.",
              new ArrayList<>(),
              fuente,
              "760",
              TipoConsenso.MULTIPLES_MENCIONES
          ),
          new Coleccion(
              "Historia del Transporte Público en Buenos Aires (1880-1950)",
              "Compilación de hechos clave sobre tranvías, subtes y colectivos que moldearon la ciudad.",
              new ArrayList<>(),
              fuente,
              "65",
              TipoConsenso.MAYORIA_SIMPLE
          ),
          new Coleccion(
              "Revolución Industrial: Impacto Social",
              "Hechos sobre las condiciones laborales, migración rural y el cambio de paradigma social en el siglo XIX.",
              new ArrayList<>(),
              fuente,
              "78",
              TipoConsenso.ABSOLUTA
          ),
          new Coleccion(
              "Pioneros de la Computación",
              "Breve recorrido por las figuras que sentaron las bases de la informática moderna, desde Turing hasta Wozniak.",
              new ArrayList<>(),
              fuente,
              "2",
              TipoConsenso.MAYORIA_SIMPLE
          ),
          new Coleccion(
              "Arquitectura Racionalista en La Plata",
              "Ejemplos y datos sobre edificios emblemáticos de esta corriente arquitectónica en la capital bonaerense.",
              new ArrayList<>(),
              fuente,
              "23",
              TipoConsenso.MAYORIA_SIMPLE
          ),
          new Coleccion(
              "Eventos Astronómicos Inusuales",
              "Hechos documentados sobre supernovas, cometas y lluvias de meteoros con alta visibilidad.",
              new ArrayList<>(),
              fuente,
              "453",
              TipoConsenso.ABSOLUTA
          ),
          new Coleccion(
              "Derechos Civiles en América Latina",
              "Cronología de las leyes y movimientos que impulsaron la igualdad y los derechos humanos en la región.",
              new ArrayList<>(),
              fuente,
              "54",
              TipoConsenso.MULTIPLES_MENCIONES
          ),
          new Coleccion(
              "Coleccion con fuente estatica",
              "Descripción corta para un test de paginación.",
              new ArrayList<>(),
              fuenteEstatica,
              "023",
              TipoConsenso.MAYORIA_SIMPLE
          ),
          new Coleccion(
              "Colección de Prueba 9",
              "Ejemplo con un handle más largo.",
              new ArrayList<>(),
              fuente,
              "01",
              TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba2","prueba2",new ArrayList<>(), fuente,"prueba2", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba2","prueba2",new ArrayList<>(), fuente,"prueba2", TipoConsenso.ABSOLUTA)
      );

      colecciones.forEach(c -> entityManager().persist(c));
      var countQuery = entityManager().createQuery("SELECT COUNT(s) FROM Solicitud s", Long.class);
      Long count = countQuery.getSingleResult();
      
      if (count == 0) {
        var servicioSolicitudes = new ServicioSolicitudes(new SolicitudesRepository());
        
        var hecho1 = new Hecho("Terremoto en Mendoza", "Terremoto de magnitud 6.2", "Desastre Natural", 
                               new Ubicacion(-32.8895, -68.8458), LocalDateTime.now(), LocalDateTime.now(), 
                               OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
        
        var hecho2 = new Hecho("Inundación en Buenos Aires", "Inundación por lluvias torrenciales", "Desastre Natural", 
                               new Ubicacion(-34.6118, -58.3960), LocalDateTime.now(), LocalDateTime.now(), 
                               OrigenHecho.PROVISTO_POR_CONTRIBUYENTE);
        
        servicioSolicitudes.crearSolicitud(usuarioFeli.get(), hecho1, TipoSolicitud.ELIMINACION_HECHO, "Solicitud de eliminación de prueba");
        servicioSolicitudes.crearSolicitud(usuarioDani.get(), hecho2, TipoSolicitud.ELIMINACION_HECHO, "Solicitud de eliminación de prueba");
        servicioSolicitudes.crearSolicitud(usuarioDani.get(), hecho1, TipoSolicitud.ELIMINACION_HECHO, "Solicitud de eliminación de prueba");
      }
    });

  }

}
