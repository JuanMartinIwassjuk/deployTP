package ar.edu.utn.frba.dds.models.entidades.servicios;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("banio")

public class Banio extends Servicio {

  @Enumerated(EnumType.STRING)
  public TipoBanio sexo_banio;


  public Banio(TipoBanio sexoBanio) {
    this.sexo_banio = sexoBanio;
  }

  public Banio() {

  }

  public TipoBanio getSexoBanio() {
    return sexo_banio;
  }

  public void setSexoBanio(TipoBanio sexoBanio) {
    this.sexo_banio = sexoBanio;
  }

}
