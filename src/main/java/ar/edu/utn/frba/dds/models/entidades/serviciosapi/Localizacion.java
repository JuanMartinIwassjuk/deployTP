package ar.edu.utn.frba.dds.models.entidades.serviciosapi;

public class Localizacion {

  String nombre_localizacion;

  public Localizacion(String nombre) {
    this.nombre_localizacion = nombre;
  }

  public void setNombre(String nombre) {
    this.nombre_localizacion = nombre;
  }

  public Localizacion(){

  }

  public String getNombreLocalizacion() {
    return nombre_localizacion;
  }
}
