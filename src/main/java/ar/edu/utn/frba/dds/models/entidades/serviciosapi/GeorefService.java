package ar.edu.utn.frba.dds.models.entidades.serviciosapi;

import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.ListadoMunicipios;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.ListadoDepartamentos;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes.ListadoProvincias;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeorefService {

  @GET("municipios")
  Call<ListadoMunicipios> municipios();

  @GET("municipios")
  Call<ListadoMunicipios> municipios(@Query("provincia") int idProvincia);

  @GET("municipios")
  Call<ListadoMunicipios> municipios(@Query("provincia") int idPro, @Query("campos") String campos);

  @GET("departamentos")
  Call<ListadoDepartamentos> departamentos(@Query("campos") String campos);

  @GET("departamentos")
  Call<ListadoDepartamentos> departamentos();

  @GET("provincia")
  Call<ListadoProvincias> provincias(@Query("campos") String campos);

  @GET("provincias")
  Call<ListadoProvincias> provincias();


}


