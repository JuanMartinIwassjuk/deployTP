package ar.edu.utn.frba.dds.models.entidades.seguridad.serviciosseguridad;

public class HasheadorContrasenias {

  public String hashearContrasenia(String contrasenia) {
    return org.apache.commons.codec.digest.DigestUtils.sha1Hex(contrasenia);
  }
}