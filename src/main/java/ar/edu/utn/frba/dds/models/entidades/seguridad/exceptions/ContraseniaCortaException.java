package ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions;

public class ContraseniaCortaException extends RuntimeException {
  public ContraseniaCortaException(String mensaje) {
    super(mensaje);
  }
}
