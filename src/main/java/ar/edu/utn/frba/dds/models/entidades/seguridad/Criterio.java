package ar.edu.utn.frba.dds.models.entidades.seguridad;

import java.io.IOException;

public interface Criterio {
  void validar(String contrasenia) throws IOException;
}
