package ar.edu.utn.frba.dds.models.entidades.notificaciones;

public class Notificacion {
  private String titulo;
  private String descripcion;

  public Notificacion(){
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }
}
