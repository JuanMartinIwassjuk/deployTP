package ar.edu.utn.frba.dds.models.entidades.servicios;

import ar.edu.utn.frba.dds.models.entidades.common.Tramo;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("escalera_mecanica")

public class EscaleraMecanica extends Servicio {

  @ManyToOne
  @JoinColumn()
  private Tramo calle_acceso;

  @ManyToOne
  @JoinColumn()
  private Tramo acceso_anden;


  public EscaleraMecanica(Tramo calleAcceso, Tramo accesoAnden) {
    this.calle_acceso = calleAcceso;
    this.acceso_anden = accesoAnden;
  }

  public EscaleraMecanica() {

  }

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
