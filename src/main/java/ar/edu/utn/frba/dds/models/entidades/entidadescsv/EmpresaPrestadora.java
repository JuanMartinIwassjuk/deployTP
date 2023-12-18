package ar.edu.utn.frba.dds.models.entidades.entidadescsv;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Entidad;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "empresa_prestadora")
public class EmpresaPrestadora extends Persistente {

  @Column(name = "nombre_empresa")
  private String nombre_empresa;


  //@OneToMany(cascade = CascadeType.ALL)
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "empresa_prestadora_id", referencedColumnName = "id")
  private List<Entidad> entidades;

  //@OneToOne(cascade = CascadeType.ALL)
  //@OneToOne(cascade = CascadeType.MERGE)
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "miembro_id", referencedColumnName = "id")
  private Miembro usuario_designado;


  public EmpresaPrestadora(String nombreEmpresa) {
    this.nombre_empresa = nombreEmpresa;
  }

  public EmpresaPrestadora() {

  }

  public String getNombreEmpresa() {
    return nombre_empresa;
  }

  public void designarUsuario(Miembro usuario){
    this.usuario_designado = usuario;
  }

  public List<Entidad> getEntidades() {
    return entidades;
  }

  public Miembro getUsuarioDesignado() {
    return usuario_designado;
  }

}
