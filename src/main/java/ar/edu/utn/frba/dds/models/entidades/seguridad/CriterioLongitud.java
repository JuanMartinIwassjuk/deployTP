package ar.edu.utn.frba.dds.models.entidades.seguridad;

import ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions.ContraseniaCortaException;

public class CriterioLongitud implements Criterio {

  @Override
  public void validar(String contrasenia) {
    this.longitudAceptable(contrasenia);
  }

  public void longitudAceptable(String contrasenia) {
    if (!(contrasenia.length() >= 8)) {
      throw new ContraseniaCortaException("La contrasenia tener 8 o mas caracteres ");
    }
  }
}
