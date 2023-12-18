package ar.edu.utn.frba.dds.models.entidades.comunidades;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Prestacion;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "comunidad")
public class Comunidad extends Persistente {
  @Column(name = "nombre")
  private String nombre;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Miembro> miembros;

  @OneToMany(cascade = CascadeType.MERGE)
  @JoinColumn(name = "incidente_id", referencedColumnName = "id")
  private List<Incidente> incidentes_miembros;

  @OneToMany(cascade = CascadeType.MERGE)
  @JoinColumn(name = "admin_id", referencedColumnName = "id")
  private List<Miembro> administradores;

  @ManyToMany(cascade = CascadeType.MERGE)
  private List<Prestacion> prestaciones_de_interes;


  public Comunidad(List<Miembro> interesados) {
    this.miembros = interesados;
  }

  public Comunidad() {

  }

  public void informarSobreIncidente(Incidente incidente){
    this.miembros.forEach(miembro -> miembro.serNotificado(incidente));
  }


  public List<Incidente> incidentesAbiertos(){
    return this.incidentes_miembros.stream().filter(Incidente::getEstaAbierto).toList();
  }

  public List<Incidente> incidentesCerrados(){
    return this.incidentes_miembros.stream().filter(incidente ->!incidente.getEstaAbierto()).toList();
  }


  public void agregarIncidente(Incidente incidente){
    incidentes_miembros.add(incidente);
    //incidente.setComunidad(this);
  }

  public int cantidadDeMiembros(){
    return this.miembros.size();
  }

  public Boolean contieneAlMiembro(Miembro miembro){
    return miembros.contains(miembro);
  }


  public List<Incidente> getIncidentesMiembros() {
    return incidentes_miembros;
  }

  public List<Miembro> getMiembros() {
    return miembros;
  }


  public void setMiembros(List<Miembro> miembros) {
    this.miembros = miembros;
  }

  public void setIncidentesMiembros(List<Incidente> incidentesMiembros) {
    this.incidentes_miembros = incidentesMiembros;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public void agregarPrestacion(Prestacion prestacion){
    prestaciones_de_interes.add(prestacion);
  }

  public void setPrestacionesDeInteres(List<Prestacion> prestacionesDeInteres) {
    this.prestaciones_de_interes = prestacionesDeInteres;
  }

  public int getCantidadDeMiembros(){
    return this.miembros.size();
  }

  public int getCantidadDeIncidentes(){
    return this.incidentes_miembros.size();
  }

  public List<Prestacion> getPrestacionesDeInteres() {
    return prestaciones_de_interes;
  }

  public void setAdministradores(List<Miembro> administradores) {
    this.administradores = administradores;
  }

  public int getCantidadPrestacionesInteres(){
    return prestaciones_de_interes.size();
  }

  public List<Miembro> getAdministradores() {
    return administradores;
  }
}

