package ar.edu.utn.frba.dds.dominio.fuentes;

import ar.edu.utn.frba.dds.dominio.hechos.Hecho;
import ar.edu.utn.frba.dds.dominio.solicitudes.SolicitudEliminacion;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Entity
@Getter
@Setter
public class FuenteMetaMapaAdapter {

  private  String url;
  @Transient
  private Retrofit retrofit;
  @Transient
  private FuenteMetaMapaApiService fuenteMetaMapaApiService;
  @Id
  private Long id;

  public FuenteMetaMapaAdapter(String url) {
    this.url = url;
    this.retrofit = new Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    this.fuenteMetaMapaApiService = retrofit.create(FuenteMetaMapaApiService.class);
  }

  public FuenteMetaMapaAdapter() {

  }

  public List<Hecho> obtenerHechos(Map<String, String> filtros) {
    try {
      Call<List<Hecho>> call = fuenteMetaMapaApiService.getHechos(filtros);
      Response<List<Hecho>> response = call.execute();
      return response.body();
    } catch (IOException e) {
      throw new UncheckedIOException("Error en obtenerHechos", e);
    }
  }

  public List<Hecho> obtenerHechosDeUnaColeccion(String id, Map<String, String> filtros) {

    try {
      Call<List<Hecho>> call = fuenteMetaMapaApiService.getHechosColeccion(id, filtros);
      Response<List<Hecho>> response = call.execute();
      return response.body();
    } catch (IOException e) {
      throw new UncheckedIOException("Error en obtenerHechos de una coleccion", e);
    }

  }

  public void crearSolicitudEliminacion(SolicitudEliminacion solicitud) {
    try {
      Call<Void> call = fuenteMetaMapaApiService.createSolicitudEliminacion(solicitud);
      call.execute();
    } catch (IOException e) {
      throw new UncheckedIOException("Error en crear solicitud de eliminacion", e);
    }

  }


}
