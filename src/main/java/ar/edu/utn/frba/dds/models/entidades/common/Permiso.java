package ar.edu.utn.frba.dds.models.entidades.common;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "permiso")

public class Permiso extends Persistente {
  @Column(name = "nombre")
  private String nombre;

  @Column(name = "nombre_interno")
  private String nombre_interno;

  public boolean coincideConNombreInterno(String nombre) {
    return this.nombre_interno.equals(nombre);
  }

  public String getNombre() {
    return nombre;
  }

  public String getNombreInterno() {
    return nombre_interno;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setNombreInterno(String nombreInterno) {
    this.nombre_interno = nombreInterno;
  }
}
