package ar.edu.utn.frba.dds.models.entidades.servicios;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "servicio")
@DiscriminatorColumn(name = "clasificacion")

public abstract class Servicio extends Persistente {

  @Column(name = "nombre_servicio")
  public String nombre_servicio;

  @Enumerated(EnumType.STRING)
  public TipoServicio tipo_servicio;


  public void setNombreServicio(String nombreServicio) {
    this.nombre_servicio = nombreServicio;
  }

  public void setTipoServicio(TipoServicio tipoServicio) {
    this.tipo_servicio = tipoServicio;
  }

  public TipoServicio getTipoServicio() {
    return tipo_servicio;
  }

  public String getNombreServicio() {
    return nombre_servicio;
  }
}
