package ar.edu.utn.frba.dds.models.entidades.seguridad;

import ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions.ContraseniaSinMayusculasException;
import ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions.ContraseniaSinNumerosException;

public class CriterioCaracteresEspeciales implements Criterio {
  @Override
  public void validar(String contrasenia) {
    this.contieneNumero(contrasenia);
    this.contieneMayuscula(contrasenia);
  }

  public void contieneNumero(String contrasenia) {
    if (!contrasenia.matches(".*[0-9].*")) {
      throw new ContraseniaSinNumerosException("La contrasenia debe contener al menos un numero ");
    }
  }

  public void contieneMayuscula(String contrasenia) {
    if (!contrasenia.matches(".*[A-Z].*")) {
      throw new ContraseniaSinMayusculasException("La contrasenia debe contener una mayuscula");
    }
  }

}
