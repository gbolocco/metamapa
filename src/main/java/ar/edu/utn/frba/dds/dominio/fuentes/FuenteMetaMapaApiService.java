package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public interface FuenteMetaMapaApiService {

  @GET("hechos")
  Call<List<Hecho>> getHechos(
      @QueryMap Map<String, String> filtros
  );

  @GET("colecciones/{identificador}/hechos")
  Call<List<Hecho>> getHechosColeccion(
      @Path("identificador") String identificador,
      @QueryMap Map<String, String> filtros
  );

  @POST("solicitudes")
  Call<Void> createSolicitudEliminacion(
      @Body SolicitudEliminacion solicitud
  );
}
