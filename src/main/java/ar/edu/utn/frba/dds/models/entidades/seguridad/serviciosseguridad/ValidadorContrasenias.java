package ar.edu.utn.frba.dds.models.entidades.seguridad.serviciosseguridad;

import ar.edu.utn.frba.dds.models.entidades.seguridad.Criterio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ValidadorContrasenias {

  private List<Criterio> criteriosSeguridad = new ArrayList<>();

  public void validarContrasenia(String contrasenia) {
    criteriosSeguridad.forEach(criterio -> {
      try {
        criterio.validar(contrasenia);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void agregarCriterioSeguridad(Criterio criterio) {
    criteriosSeguridad.add(criterio);
  }
}
