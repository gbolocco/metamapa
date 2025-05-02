package ar.edu.utn.frba.dds;
import java.util.ArrayList;
import java.util.List;


public class Administrador {

  private String Usuario;
  private String Password;
  public Administrador(String Usuario, String Password) {
    this.Usuario = Usuario;
    this.Password = Password;

  }

  public void crearColeccion(){
    ColectionManager.crearColeccion();
  }
}
