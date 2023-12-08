package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.ListadoDepartamentos;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.ListadoMunicipios;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.ListadoProvincias;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.ServicioGeoref;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServiceGeorefTest {

  private ServicioGeoref servicioGeoref;

  @BeforeEach
  public void init() {
    servicioGeoref = new ServicioGeoref();

  }

  @Test
  public void lasProvinciasDelSistema() throws IOException {

    ListadoProvincias listadoProvincias = servicioGeoref.listadoProvincias();
    Assertions.assertEquals(24, listadoProvincias.provincias.size());
  }
  @Test
  public void losMunicipiosDelSistema() throws IOException {

    ListadoMunicipios listadoMunicipios = servicioGeoref.listadoMunicipios();
    Assertions.assertEquals(10, listadoMunicipios.municipios.size());
  }
  @Test
  public void lasMunicipiosDeUnaProvincia() throws IOException {

    ListadoProvincias listadoProvincias = servicioGeoref.listadoProvincias();
    ListadoMunicipios listadoMunicipios = servicioGeoref.listadoMunicipiosDeProvincia(listadoProvincias.provincias.get(0).getId());
    Assertions.assertEquals(10, listadoMunicipios.municipios.size());
  }

  @Test
  public void losDepartamentosDelSistema() throws IOException {
    ListadoDepartamentos listadoDepartamentos = servicioGeoref.listadoDepartamentos();
    Assertions.assertEquals(10, listadoDepartamentos.departamentos.size());
  }

}
