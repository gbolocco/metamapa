package ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia;

public enum Provincia {
  BUENOS_AIRES("Buenos Aires"),
  CABA("Ciudad Autónoma de Buenos Aires"),
  CATAMARCA("Catamarca"),
  CHACO("Chaco"),
  CHUBUT("Chubut"),
  CORDOBA("Córdoba"),
  CORRIENTES("Corrientes"),
  ENTRE_RIOS("Entre Ríos"),
  FORMOSA("Formosa"),
  JUJUY("Jujuy"),
  LA_PAMPA("La Pampa"),
  LA_RIOJA("La Rioja"),
  MENDOZA("Mendoza"),
  MISIONES("Misiones"),
  NEUQUEN("Neuquén"),
  RIO_NEGRO("Río Negro"),
  SALTA("Salta"),
  SAN_JUAN("San Juan"),
  SAN_LUIS("San Luis"),
  SANTA_CRUZ("Santa Cruz"),
  SANTA_FE("Santa Fe"),
  SANTIAGO_DEL_ESTERO("Santiago del Estero"),
  TIERRA_DEL_FUEGO("Tierra del Fuego, Antártida e Islas del Atlántico Sur"),
  TUCUMAN("Tucumán");

  private final String nombre;

  Provincia(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public static Provincia fromString(String valor) {
    if (valor == null) {
      throw new IllegalArgumentException("El valor no puede ser null");
    }

    String normalizado = valor.trim().toUpperCase();

    try {
      return Provincia.valueOf(normalizado);
    } catch (IllegalArgumentException e) {

    }

    for (Provincia p : Provincia.values()) {
      if (p.getNombre().equalsIgnoreCase(valor.trim())) {
        return p;
      }
    }
    throw new IllegalArgumentException("Provincia desconocida: " + valor);
  }
}
