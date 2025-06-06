package ar.edu.utn.frba.dds.dominio.fuentes;

import java.time.LocalDate;
import java.util.Map;

public interface Conexion {

  public Map<String, Object> siguienteHecho(String url, LocalDate fechaUltimaConsulta);


}
