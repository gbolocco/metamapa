package ar.edu.utn.frba.dds.compartido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLogger {

  public static Logger getLogger(Class<?> clazz) {
    return LoggerFactory.getLogger(clazz);
  }
}

/*
Como se usa?
1. Agregar los siguientes imports a la clase
import org.slf4j.Logger;
import ar.edu.utn.frba.dds.AppLogger;

2. Incluir la siguiente linea en la clase que se deseea logear algo
private static final Logger logger = AppLogger.getLogger(<Mi_CLASE>.class);

NIVELES DE LOGS ( logger.<TIPO_DE_LOG>() )
debug --> Información útil para desarrolladores en ambiente de desarrollo/test.
info -->  Mensajes normales, que indican cosas importantes que ocurrieron (sin ser errores).
warn --> Algo inusual o potencialmente problemático, pero no detiene el sistema.
error --> Algo falló o no pudo completarse. Deberías investigarlo o manejarlo.

Se genera una carpeta ./logs
con un archivo metamapa que contiene todo el historial de logs
*/