package ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions;

public class ContraseniaSinMayusculasException extends RuntimeException {
  public ContraseniaSinMayusculasException(String mensaje) {
    super(mensaje);
  }
}
