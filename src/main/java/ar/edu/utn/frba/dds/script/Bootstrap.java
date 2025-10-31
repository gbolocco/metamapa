package ar.edu.utn.frba.dds.script;

import ar.edu.utn.frba.dds.dominio.colecciones.Coleccion;
import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.dominio.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.hechos.OrigenHecho;
import ar.edu.utn.frba.dds.dominio.hechos.RepresentacionDeHecho;
import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import ar.edu.utn.frba.dds.dominio.lectores.LectorCsv;
import ar.edu.utn.frba.dds.dominio.multimedia.TipoContenido;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.infraestructura.repositorios.ColeccionRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.FuentesRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.HechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.RepresentacionHechosRepository;
import ar.edu.utn.frba.dds.infraestructura.repositorios.SolicitudesRepositoryDB;
import ar.edu.utn.frba.dds.modelo.Rol;
import ar.edu.utn.frba.dds.modelo.Usuario;
import ar.edu.utn.frba.dds.infraestructura.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Bootstrap implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    new Bootstrap().init();
  }

  public void init() {
    withTransaction(() -> {

      // Solo crear usuarios si no existen
      try {
        RepositorioUsuarios.INSTANCE.buscarPorNombre("feli");
      } catch (Exception e) {
        var usuarios = Arrays.asList(
            new Usuario("feli", "feli", Rol.USER),
            new Usuario("dani", "dani", Rol.USER),
            new Usuario("umi", "umi", Rol.ADMIN)
        );
        usuarios.forEach((usuario) -> RepositorioUsuarios.INSTANCE.registrar(usuario));
      }
      var hechos = Arrays.asList(
          new Hecho("Prueba1", "Prueba1", "Prueba1", new Ubicacion(30.2,30.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.PROVISTO_POR_CONTRIBUYENTE),
          new Hecho("Prueba2", "Prueba2", "Prueba2", new Ubicacion(20.2,10.2), LocalDateTime.now(), LocalDateTime.now(), OrigenHecho.FUENTE_PROXY)
      );
      hechos.forEach(hecho -> hecho.addContenidoMultimedia("https://media.istockphoto.com/id/155666671/es/" +
          "vector/ilustraci%C3%B3n-vectorial-de-red-house-icon.jpg?s=612x612&w=0&k=20&c=3IHzI5tgnVZQuE_4ZdJDIDyMGd44qWuketKv5EOvawQ=", TipoContenido.IMAGEN));
      hechos.forEach(h-> h.addContenidoMultimedia("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", TipoContenido.VIDEO));
      hechos.forEach((hecho) -> HechosRepository.getInstancia().cargarHecho(hecho));

      var fuente = new FuenteDinamica();
      entityManager().persist(fuente);

      var colecciones = Arrays.asList(
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba","prueba",new ArrayList<>(), fuente,"prueba"),
          new Coleccion("prueba2","prueba2",new ArrayList<>(), fuente,"prueba2"),
          new Coleccion("prueba2","prueba2",new ArrayList<>(), fuente,"prueba2"),
          new Coleccion("prueba2","prueba2",new ArrayList<>(), fuente,"prueba2")
      );

      colecciones.forEach(c -> entityManager().persist(c));

      // Crear solicitudes de eliminación
      var representacion1 = new RepresentacionDeHecho();
      representacion1.setTitulo("Terremoto en Mendoza");
      representacion1.setDescripcion("Terremoto de magnitud 6.2");
      RepresentacionHechosRepository.getInstancia().cargarRepresentacionDeHecho(representacion1);
      
      var representacion2 = new RepresentacionDeHecho();
      representacion2.setTitulo("Inundación en Buenos Aires");
      representacion2.setDescripcion("Inundación por lluvias torrenciales");
      RepresentacionHechosRepository.getInstancia().cargarRepresentacionDeHecho(representacion2);
      
      // Verificar si ya existen solicitudes para evitar duplicados
      var countQuery = entityManager().createQuery("SELECT COUNT(s) FROM Solicitud s", Long.class);
      Long count = countQuery.getSingleResult();
      
      if (count == 0) {
        var solicitud1 = new SolicitudEliminacion();
        solicitud1.setRepresentacionDeHecho(representacion1);
        solicitud1.setJustificacion("Registro duplicado, ya existe el mismo evento con ID diferente");
        solicitud1.setTipoSolicitud(ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud.ELIMINACION_HECHO);
        solicitud1.setEstadoSolicitud(ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud.PENDIENTE);
        solicitud1.setFechaSolicitud(new java.util.Date());
        
        var solicitud2 = new SolicitudEliminacion();
        solicitud2.setRepresentacionDeHecho(representacion2);
        solicitud2.setJustificacion("Información incorrecta, el evento no ocurrió en la fecha indicada");
        solicitud2.setTipoSolicitud(ar.edu.utn.frba.dds.dominio.solicitudes.TipoSolicitud.ELIMINACION_HECHO);
        solicitud2.setEstadoSolicitud(ar.edu.utn.frba.dds.dominio.solicitudes.EstadoSolicitud.PENDIENTE);
        solicitud2.setFechaSolicitud(new java.util.Date());
        
        entityManager().persist(solicitud1);
        entityManager().persist(solicitud2);
      }
    });

  }

}
