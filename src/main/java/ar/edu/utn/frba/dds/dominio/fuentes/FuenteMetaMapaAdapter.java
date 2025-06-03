package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FuenteMetaMapaAdapter {
  private String url;
  private Retrofit retrofit;
  private FuenteMetaMapaApiService fuenteMetaMapaApiService;

  private FuenteMetaMapaAdapter(String url) {
    this.url = url;
    this.retrofit = new Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    this.fuenteMetaMapaApiService = retrofit.create(FuenteMetaMapaApiService.class);
  }

  public List<Hecho> obtenerHechos(Map<String, String> filtros) throws IOException {
    Call<List<Hecho>> call = fuenteMetaMapaApiService.getHechos(filtros);
    Response<List<Hecho>> response = call.execute();
    return response.body();
  }

  public List<Hecho> obtenerHechosColeccion(String id, Map<String, String> filtros)
      throws IOException {
    Call<List<Hecho>> call = fuenteMetaMapaApiService.getHechosColeccion(id, filtros);
    Response<List<Hecho>> response = call.execute();
    return response.body();
  }

  public void crearSolicitudEliminacion(SolicitudEliminacion solicitud) throws IOException {
    Call<Void> call = fuenteMetaMapaApiService.createSolicitudEliminacion(solicitud);
    Response<Void> response = call.execute();

  }


}
