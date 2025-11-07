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
import ar.edu.utn.frba.dds.dominio.multimedia.TipoContenido;
import ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepository;
import ar.edu.utn.frba.dds.modelo.Rol;
import ar.edu.utn.frba.dds.modelo.Usuario;
import ar.edu.utn.frba.dds.infraestructura.repositorios.UsuariosRepository;
import ar.edu.utn.frba.dds.servicios.ServicioSolicitudes;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Bootstrap implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    new Bootstrap().init();
  }

  public void init() {
    var fuente = new FuenteDinamica();
    entityManager().persist(fuente);

    var fuenteEstatica = new FuenteEstatica("./datos/desastres_naturales_processed.csv",new LectorCsv());

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
      var hechos = Arrays.asList(
          new Hecho("Prueba1", "Prueba1", "Prueba1", new Ubicacion(30.2,30.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
          new Hecho("Prueba2", "Prueba2", "Prueba2", new Ubicacion(20.2,10.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.FUENTE_PROXY)
      );
      hechos.forEach(hecho -> hecho.addContenidoMultimedia("https://media.istockphoto.com/id/155666671/es/" +
          "vector/ilustraci%C3%B3n-vectorial-de-red-house-icon.jpg?s=612x612&w=0&k=20&c=3IHzI5tgnVZQuE_4ZdJDIDyMGd44qWuketKv5EOvawQ=", TipoContenido.IMAGEN));
      hechos.forEach(h-> h.addContenidoMultimedia("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", TipoContenido.VIDEO));
      hechos.forEach((hecho) -> HechosRepository.getInstancia().cargarHecho(hecho));



      var colecciones = Arrays.asList(
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba", TipoConsenso.ABSOLUTA),
          new Coleccion("prueba2","prueba2",new ArrayList<>(), fuente,"prueba2", TipoConsenso.ABSOLUTA),
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
