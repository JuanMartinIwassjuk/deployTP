package ar.edu.utn.frba.dds.models.entidades.incidentes;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.Localizacion;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidente")
public class Incidente extends Persistente {

  @Column(name = "titulo")
  private String titulo;

  @Column(name = "detalle")
  private String detalle;

  @Column(name = "fecha_apertura", columnDefinition = "DATE")
  private LocalDateTime fecha_apertura;

  @Column(name = "fecha_cierre", columnDefinition = "DATE")
  private LocalDateTime fecha_cierre;

  //@ManyToOne(cascade = CascadeType.ALL)
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "prestacion_id", referencedColumnName = "id")
  private Prestacion prestacion;

  @Column(name = "estado_incidente")
  private Boolean esta_abierto;

  //@ManyToOne(cascade = CascadeType.ALL)
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "comunidad_id", referencedColumnName = "id")
  private Comunidad comunidad;

  @Column(name = "cantidad_afectados")
  private int cantidad_afectados;

  //@Embedded
  //@Column(name = "localizacion")
  //private Localizacion localizacion;




  public Incidente(String titulo, String detalle, LocalDateTime fechaApertura, Prestacion prestacion,
                   Comunidad comunidad,Boolean estaAbierto) {
    this.titulo = titulo;
    this.detalle = detalle;
    this.fecha_apertura = fechaApertura;
    this.prestacion = prestacion;
    this.comunidad = comunidad;
    this.esta_abierto = estaAbierto;
    this.cantidad_afectados = comunidad.getMiembros().stream().
        filter(Miembro::esAfectado).toList().size();
    this.prestacion.agregarIncidente(this);
  }

  public Incidente(String titulo, String detalle){
    this.titulo = titulo;
    this.detalle = detalle;
  }

  public Incidente() {

  }

  //public void setLocalizacion(Localizacion localizacion) {
  //this.localizacion = localizacion;
  //}

  public void setFechaCierre(LocalDateTime fechaCierre) {
    this.fecha_cierre = fechaCierre;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public void setDetalle(String detalle) {
    this.detalle = detalle;
  }

  public void cerrarIncidente() {
    this.esta_abierto = false;
  }

  public LocalDateTime getFechaApertura() {
    return fecha_apertura;
  }

  public String getTitulo() {
    return titulo;
  }
  public String getDetalle() {
    return detalle;
  }

  public LocalDateTime getFechaCierre() {
    return fecha_cierre;
  }

  public Boolean getEstaAbierto() {
    return esta_abierto;
  }

  public Prestacion getPrestacion() {
    return prestacion;
  }

  public Boolean estaEnLas24Horas(){
    LocalDateTime fechaLimite = this.getFechaApertura().plusHours(24);
    return LocalDateTime.now().isBefore(fechaLimite);
  }

  public Comunidad getComunidad() {
    return comunidad;
  }

  public int cantidadAfectados() {
    return this.cantidad_afectados;
  }

  public int getCantidad_afectados() {
    return cantidad_afectados;
  }

  public void setFechaApertura(LocalDateTime fechaApertura) {
    this.fecha_apertura = fechaApertura;
  }

  public void setPrestacion(Prestacion prestacion) {
    this.prestacion = prestacion;
  }

  public void setEstaAbierto(Boolean estaAbierto) {
    this.esta_abierto = estaAbierto;
  }

  public void setComunidad(Comunidad comunidad) {
    this.comunidad = comunidad;
  }

  public void setCantidadAfectados(int cantidadAfectados) {
    this.cantidad_afectados = cantidadAfectados;
  }

  public Localizacion getLocalizacion() {
    return this.getPrestacion().getEstablecimiento().getEntidad().getLocalizacionAsignada();
  }

}
