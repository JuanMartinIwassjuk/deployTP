package ar.edu.utn.frba.dds.models.entidades.serviciosapi;

import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.Municipio;
import java.io.IOException;

public class LocalizadorMunicipio extends LocalizacionAsignada {

  @Override
  public Localizacion buscarLocalizacion(String localizacionValue) throws IOException {
    ServicioGeoref servicioGeoref = new ServicioGeoref();
    Municipio municipioObjetivo = servicioGeoref.listadoMunicipios().
        municipios.stream().filter(municipio -> municipio.getNombre().equals(localizacionValue)).toList().get(0);

    return new Localizacion(municipioObjetivo.getNombre());
  }
}



