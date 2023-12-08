package ar.edu.utn.frba.dds.models.entidades.serviciosapi;

import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.Departamento;
import java.io.IOException;

public class LocalizadorDepartamento extends LocalizacionAsignada {

  @Override
  public Localizacion buscarLocalizacion(String localizacionValue) throws IOException {
    ServicioGeoref servicioGeoref = new ServicioGeoref();
    Departamento departamentoObjetivo = servicioGeoref.listadoDepartamentos().
        departamentos.stream().filter(departamento -> departamento.getNombre().equals(localizacionValue)).toList().get(0);

    return new Localizacion(departamentoObjetivo.getNombre());
  }
}
