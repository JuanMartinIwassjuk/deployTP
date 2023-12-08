package ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes;

public class Departamento {

  private int id;
  public String nombre;
  private Provincia provincia;

  public String getNombre() {
    return this.nombre;
  }
}
