package ar.edu.utn.frba.dds.models.entidades.serviciospublicos;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Collections;
import java.util.List;
@Entity
@Table(name = "establecimiento")

public class Establecimiento extends Persistente {

  @Column(name = "nombre")
  private String nombre;
  @Enumerated(EnumType.STRING)
  private TipoEstablecimiento tipo_establecimiento;
  //@ManyToMany(cascade = CascadeType.ALL)
  @ManyToMany
  private List<Servicio> servicios_de_establecimiento;
  //@ManyToOne(cascade = CascadeType.ALL)
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "entidad_id", referencedColumnName = "id")
  private Entidad entidad;


  public Establecimiento(String nombre,
                         List<Servicio> serviciosDeEstablecimiento, TipoEstablecimiento tipoEstablecimiento) {
    this.nombre = nombre;
    this.servicios_de_establecimiento = Collections.unmodifiableList(serviciosDeEstablecimiento);
    this.tipo_establecimiento = tipoEstablecimiento;
  }

  public Establecimiento() {

  }

  public String getNombre() {
    return nombre;
  }


  public List<Servicio> getServiciosDeEstablecimiento() {
    return Collections.unmodifiableList(servicios_de_establecimiento);
  }

  public Entidad getEntidad() {
    return entidad;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setTipoEstablecimiento(TipoEstablecimiento tipoEstablecimiento) {
    this.tipo_establecimiento = tipoEstablecimiento;
  }

  public void setServiciosDeEstablecimiento(List<Servicio> serviciosDeEstablecimiento) {
    this.servicios_de_establecimiento = serviciosDeEstablecimiento;
  }

  public void setEntidad(Entidad entidad) {
    this.entidad = entidad;
  }
}
