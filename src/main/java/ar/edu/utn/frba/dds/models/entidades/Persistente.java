package ar.edu.utn.frba.dds.models.entidades;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

  @MappedSuperclass
  public abstract class Persistente {
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
      return id;
    }
  }

