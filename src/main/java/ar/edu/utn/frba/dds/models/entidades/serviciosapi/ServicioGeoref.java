package ar.edu.utn.frba.dds.models.entidades.serviciosapi;

import java.io.IOException;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.ListadoMunicipios;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.ListadoDepartamentos;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.ListadoProvincias;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServicioGeoref {

  private ServicioGeoref instance = null;
  private static final String urlAPI = "https://apis.datos.gob.ar/georef/api/";
  private Retrofit retrofit;



  public ServicioGeoref() {
    this.retrofit = new Retrofit.Builder().baseUrl(urlAPI)
            .addConverterFactory(GsonConverterFactory.create()).build();
  }

  public ServicioGeoref instance() {
    if (instance == null) {
      instance = new ServicioGeoref();
    }
    return instance;
  }

  public ListadoProvincias listadoProvincias() throws IOException {
    GeorefService georefService = this.retrofit.create(GeorefService.class);
    Call<ListadoProvincias> requestProvinciasArg = georefService.provincias();
    Response<ListadoProvincias> responseProvinciasArgs = requestProvinciasArg.execute();
    return responseProvinciasArgs.body();
  }

  public ListadoMunicipios listadoMunicipios() throws IOException {
    GeorefService georefService = this.retrofit.create(GeorefService.class);
    Call<ListadoMunicipios> requestMunicipiosArg = georefService.municipios();
    Response<ListadoMunicipios> responseMunicipiosArg = requestMunicipiosArg.execute();
    return responseMunicipiosArg.body();
  }

  public ListadoDepartamentos listadoDepartamentos() throws IOException {
    GeorefService georefService = this.retrofit.create(GeorefService.class);
    Call<ListadoDepartamentos> requestDepartamentosArg = georefService.departamentos();
    Response<ListadoDepartamentos> responseDepartamentosArg = requestDepartamentosArg.execute();
    return responseDepartamentosArg.body();
  }

  public ListadoMunicipios listadoMunicipiosDeProvincia(int id) throws IOException {
    GeorefService georefService = this.retrofit.create(GeorefService.class);
    Call<ListadoMunicipios> requestMunicipiosArg = georefService.municipios(id);
    Response<ListadoMunicipios> responseMunicipiosArg = requestMunicipiosArg.execute();
    return responseMunicipiosArg.body();
  }

}
