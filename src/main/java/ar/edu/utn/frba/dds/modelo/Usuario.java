package ar.edu.utn.frba.dds.modelo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Usuario {
  @Id
  @GeneratedValue
  private Long id;
  private String nombre;
  private String hashPassword;

  @Enumerated(EnumType.STRING)
  private Rol rol = Rol.USER;

  public Usuario() {

  }

  public Usuario(String nombre, String password, Rol rol) {
    super();
    this.nombre = nombre;
    this.hashPassword = DigestUtils.sha256Hex(password);
    this.rol = rol;
  }

}
