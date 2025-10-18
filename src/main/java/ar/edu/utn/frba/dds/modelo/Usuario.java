package ar.edu.utn.frba.dds.modelo;

import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Usuario {
  @Id
  @GeneratedValue
  private Long id;
  private String hashPassorwd;
  private String nombre;

  public Usuario() {

  }

  public Usuario(String nombre, String password) {
    super();
    this.nombre = nombre;
    this.hashPassorwd = DigestUtils.sha256Hex(password);
  }

}
