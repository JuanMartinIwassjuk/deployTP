package ar.edu.utn.frba.dds.models.entidades.seguridad;

import ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions.ContraseniaDelTopPeoresException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CriterioTopPeores implements Criterio {

  @Override
  public void validar(String contrasenia) throws IOException {
    boolean estaPresenteEnTop = this.buscarContraseniaEnTop(contrasenia);
    if (estaPresenteEnTop) {
      throw new ContraseniaDelTopPeoresException("La contraseña es top contraseñas inseguras");
    }
  }

  public boolean buscarContraseniaEnTop(String contrasenia) throws IOException {
    BufferedReader reader =
        Files.newBufferedReader(Path.of("src/main/resources/passwords10k.txt"),
            StandardCharsets.UTF_8);
    try {
      String linea;
      boolean estaPresente = false;
      while ((linea = reader.readLine()) != null) {
        if (linea.equals(contrasenia)) {
          estaPresente = true;
          break;
        }
      }
      return estaPresente;
    } finally {
      reader.close();
    }
  }
}

