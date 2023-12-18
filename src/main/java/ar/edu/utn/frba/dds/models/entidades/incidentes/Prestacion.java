package ar.edu.utn.frba.dds.models.entidades.incidentes;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.entidades.servicios.EstadoServicio;
import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Establecimiento;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "prestacion")
public class Prestacion extends Persistente {

  //@ManyToOne(cascade = CascadeType.ALL)
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "servicio_id", referencedColumnName = "id")
  Servicio servicio;

  //@ManyToOne(cascade = CascadeType.ALL)
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "establecimiento_id", referencedColumnName = "id")
  Establecimiento establecimiento;

  @Enumerated
  EstadoServicio estado_servicio;


  //@OneToMany(cascade = CascadeType.ALL)
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "prestacion_id", referencedColumnName = "id")
  List<Incidente> incidentes;

  //@ManyToMany(cascade = CascadeType.ALL)
  @ManyToMany(cascade = CascadeType.ALL)
  List<Comunidad> comunidades_interesadas;



  public Prestacion(Servicio servicio, Establecimiento establecimiento) {
    this.servicio = servicio;
    this.establecimiento = establecimiento;
    this.estado_servicio = EstadoServicio.DISPONIBLE;
  }

  public Prestacion() {

  }

  public void agregarIncidente(Incidente incidente){
    if(this.verificarIncidente(incidente)){
      this.incidentes.add(incidente);
    }

  }

  public Establecimiento getEstablecimiento() {
    return establecimiento;
  }

  public void deshabilitar() {
    this.estado_servicio = EstadoServicio.NO_DISPONIBLE;
  }

  public void habilitar() {
    this.estado_servicio = EstadoServicio.DISPONIBLE;
  }

  public EstadoServicio getEstadoServicio() {
    return estado_servicio;
  }

  public Boolean verificarIncidente(Incidente incidente){
    return this.incidentes.stream().filter(i -> i.getEstaAbierto() && i.getPrestacion().getEstablecimiento() == incidente.getPrestacion().getEstablecimiento() && i.getPrestacion().getEstablecimiento().getEntidad() == incidente.getPrestacion().getEstablecimiento().getEntidad() && i.getFechaApertura().getDayOfYear() == incidente.getFechaApertura().getDayOfYear()) == null; // si es null el filter es q se trata de un incidente distinto al que ya reportaron
  }


  public void setServicio(Servicio servicio) {
    this.servicio = servicio;
  }

  public void setEstablecimiento(Establecimiento establecimiento) {
    this.establecimiento = establecimiento;
  }

  public void setEstadoServicio(EstadoServicio estadoServicio) {
    this.estado_servicio = estadoServicio;
  }

  public void setIncidentes(List<Incidente> incidentes) {
    this.incidentes = incidentes;
  }


  public void agregarComunidadInteresada(Comunidad comunidad){
    comunidades_interesadas.add(comunidad);
  }

  public Servicio getServicio() {
    return servicio;
  }

  public void setComunidadesInteresadas(List<Comunidad> comunidadesInteresadas) {
    this.comunidades_interesadas = comunidadesInteresadas;
  }

  public List<Comunidad> getComunidadesInteresadas() {
    return comunidades_interesadas;
  }
}



