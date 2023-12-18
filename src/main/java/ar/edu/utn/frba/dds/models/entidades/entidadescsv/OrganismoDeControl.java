package ar.edu.utn.frba.dds.models.entidades.entidadescsv;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "organismo_de_control")
public class OrganismoDeControl extends Persistente {

  @Column(name = "nombre_organismo")
  private String nombre_organismo;

  //@OneToOne(cascade = CascadeType.ALL)
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "empresa_prestadora_id", referencedColumnName = "id")
  private EmpresaPrestadora empresa_prestadora;

  //@OneToOne(cascade = CascadeType.ALL)
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "miembro_id", referencedColumnName = "id")
  private Miembro usuario_designado;

  public OrganismoDeControl(String nombre) {
    this.nombre_organismo = nombre;
  }

  public OrganismoDeControl() {

  }

  public String getNombreOrganismo() {
    return nombre_organismo;
  }

  public void designarUsuario(Miembro usuario){
    this.usuario_designado = usuario;
  }

  public void setEmpresaPrestadora(EmpresaPrestadora empresaPrestadora) {
    this.empresa_prestadora = empresaPrestadora;
  }

  public EmpresaPrestadora getEmpresaPrestadora() {
    return empresa_prestadora;
  }

  public Miembro getUsuarioDesignado() {
    return usuario_designado;
  }


}
