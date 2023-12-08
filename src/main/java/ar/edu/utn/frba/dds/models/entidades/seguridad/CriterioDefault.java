package ar.edu.utn.frba.dds.models.entidades.seguridad;

import ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions.ContraseniaDefaultException;
import java.util.Arrays;
import java.util.List;

public class CriterioDefault implements Criterio {

  private final List<String> contraseniasDefault =
      Arrays.asList("admin", "administrador", "root", "1234");

  @Override
  public void validar(String contrasenia) {
    boolean esContraseniaDefault = contraseniasDefault.stream()
        .anyMatch(contra -> contra.equalsIgnoreCase(contrasenia));
    if (esContraseniaDefault) {
      throw new ContraseniaDefaultException("No se pueden usar contraseñas por default");
    }
  }
}
