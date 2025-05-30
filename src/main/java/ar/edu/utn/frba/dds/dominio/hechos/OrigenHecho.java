package ar.edu.utn.frba.dds.dominio.hechos;

public enum OrigenHecho {
  FUENTE_ESTATICA,
  FUENTE_PROXY,
  PROVISTO_POR_CONTRIBUYENTE;

  private Integer idUsuario;

  public void setIdUsuario(Integer idUsuario) {
    if (this == PROVISTO_POR_CONTRIBUYENTE) {
      this.idUsuario = idUsuario;
    } else {
      throw new UnsupportedOperationException("Unicamente se puede asignar a un usuario el origen PROVISTO_POR_CONTRIBUYENTE");
    }
  }
  public Integer getIDusuario() {
    if (this == PROVISTO_POR_CONTRIBUYENTE) {
      return idUsuario;
    }
    return null;
  }
}

/*
public class EjemploUso {
    public static void main(String[] args) {
        FuenteHecho f1 = FuenteHecho.SISTEMA;
        FuenteHecho f2 = FuenteHecho.USUARIO;

        // Asignar nombre al usuario solo si corresponde
        f2.setNombreUsuario("Juan Pérez");

        System.out.println(f2 + " - Usuario: " + f2.getNombreUsuario());

        // Esto lanzaría una excepción:
        // f1.setNombreUsuario("Error"); // UnsupportedOperationException
    }
}
}*/
