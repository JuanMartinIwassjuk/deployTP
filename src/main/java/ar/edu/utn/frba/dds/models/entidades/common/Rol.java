package ar.edu.utn.frba.dds.models.entidades.common;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rol")

public class Rol extends Persistente {

    @Column(name = "nombre")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoRol tipo;

    @ManyToMany
    private Set<Permiso> permisos;

    public Rol() {
      this.permisos = new HashSet<>();
    }

    public void agregarPermisos(Permiso ... permisos) {
      Collections.addAll(this.permisos, permisos);
    }

    public boolean tenesPermiso(Permiso permiso) {
      return this.permisos.contains(permiso);
    }

    public boolean tenesPermiso(String nombre_interno) {
      return this.permisos.stream().anyMatch(p -> p.coincideConNombreInterno(nombre_interno));
    }

  public String getNombre() {
    return nombre;
  }

  public Set<Permiso> getPermisos() {
    return permisos;
  }

  public TipoRol getTipo() {
    return tipo;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setPermisos(Set<Permiso> permisos) {
    this.permisos = permisos;
  }

  public void setTipo(TipoRol tipo) {
    this.tipo = tipo;
  }


  /*
  @Column(name = "nombreRol")
  private String nombre;

  @ElementCollection()
  @CollectionTable(name = "rol_permiso", joinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
  @Column(name = "permisos")
  private List<Permiso> permisos;


  public Rol() {
  }

  public Rol(String nombre, List<Permiso> permisos) {
    this.nombre = nombre;
    this.permisos = permisos;
  }

  public boolean tenesPermiso(Permiso permiso) {
    return permisos.contains(permiso);
  }

  public String getNombre() {
    return nombre;
  }

  public List<Permiso> getPermisos() {
    return permisos;
  }

  public void setNombre(String administrador) {
    this.nombre = administrador;
  }

 */



}

