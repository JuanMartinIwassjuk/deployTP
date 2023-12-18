package ar.edu.utn.frba.dds.models.entidades.servicios;

import ar.edu.utn.frba.dds.models.entidades.common.Tramo;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ascensor")

public class Ascensor extends Servicio {

  //@ManyToOne(cascade = CascadeType.ALL)
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn()
  private Tramo calle_acceso;

  //@ManyToOne(cascade = CascadeType.ALL)
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn()
  private Tramo acceso_anden;


  public Tramo getCalleAcceso() {
    return calle_acceso;
  }

  public Tramo getAccesoAnden() {
    return acceso_anden;
  }

  public void setCalleAcceso(Tramo calleAcceso) {
    this.calle_acceso = calleAcceso;
  }

  public void setAccesoAnden(Tramo accesoAnden) {
    this.acceso_anden = accesoAnden;
  }
}
