package ar.edu.utn.frba.dds.models.entidades.serviciosapi;

import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.Provincia;
import java.io.IOException;

public class LocalizadorProvincia extends LocalizacionAsignada {

  @Override
  public Localizacion buscarLocalizacion(String localizacionValue) throws IOException {
    ServicioGeoref servicioGeoref = new ServicioGeoref();
    Provincia provinciaObjetivo = servicioGeoref.listadoProvincias().
        provincias.stream().filter(provincia -> provincia.getNombre().equals(localizacionValue)).toList().get(0);

   return new Localizacion(provinciaObjetivo.getNombre());
  }

}
