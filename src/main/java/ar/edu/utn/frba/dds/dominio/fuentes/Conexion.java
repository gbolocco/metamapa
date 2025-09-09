package ar.edu.utn.frba.dds.dominio.fuentes;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.Map;

public interface Conexion {

  public Map<String, Object> siguienteHecho(String url, LocalDateTime fechaUltimaConsulta);


}
