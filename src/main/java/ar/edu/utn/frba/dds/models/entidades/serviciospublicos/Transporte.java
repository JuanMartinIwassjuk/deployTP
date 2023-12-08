package ar.edu.utn.frba.dds.models.entidades.serviciospublicos;


import ar.edu.utn.frba.dds.models.entidades.Persistente;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transporte")

public class Transporte extends Persistente {

  @Enumerated(EnumType.STRING)
  private TipoTransporte tipo_transporte;

  @OneToOne
  @JoinColumn(name = "linea_ida_id", referencedColumnName = "id")
  private Entidad linea_ida;

  @OneToOne
  @JoinColumn(name = "linea_vuelta_id", referencedColumnName = "id")
  private Entidad linea_vuelta;

  public Transporte(TipoTransporte tipoTransporte, Entidad lineaIda, Entidad lineaVuelta) {
    this.tipo_transporte = tipoTransporte;
    this.linea_ida = lineaIda;
    this.linea_vuelta = lineaVuelta;
  }

  public Transporte() {

  }

  public TipoTransporte getTipoTransporte() {
    return tipo_transporte;
  }

  public Entidad getLineaIda() {
    return linea_ida;
  }

  public Entidad getLineaVuelta() {
    return linea_vuelta;
  }


}
