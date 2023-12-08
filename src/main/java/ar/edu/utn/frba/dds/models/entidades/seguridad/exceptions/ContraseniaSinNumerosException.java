package ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions;

public class ContraseniaSinNumerosException extends RuntimeException {
  public ContraseniaSinNumerosException(String mensaje) {
    super(mensaje);
  }
}

