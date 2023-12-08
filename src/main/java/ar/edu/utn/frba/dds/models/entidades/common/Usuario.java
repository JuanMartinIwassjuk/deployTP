package ar.edu.utn.frba.dds.models.entidades.common;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.Localizacion;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")

public class Usuario extends Persistente {
  @Column(name = "nombre_usuario")
  private String usuario;

  @Column(name = "contrasenia")
  private String contrasenia;

  @Column(name = "numero_celular")
  private String celular;

  @Column(name = "direccion_mail")
  private String mail;

  @ManyToOne
  private Rol rol;


  public Usuario(String usuario, String contrasenia, String mail, String celular, Rol rol) {
    this.usuario = usuario;
    this.contrasenia = contrasenia;
    this.celular = celular;
    this.mail = mail;
    this.rol = rol;
  }

  public Usuario() {
  }

  public String getUsuario() {
    return usuario;
  }

  public String getContrasenia() {
    return contrasenia;
  }


  public String getCelular() {
    return celular;
  }

  public String getMail() {
    return mail;
  }

  public Rol getRol() {
    return rol;
  }


  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public void setContrasenia(String contrasenia) {
    this.contrasenia = contrasenia;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }
}



