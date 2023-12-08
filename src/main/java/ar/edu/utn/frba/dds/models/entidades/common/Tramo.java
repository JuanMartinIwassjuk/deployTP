package ar.edu.utn.frba.dds.models.entidades.common;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "tramo")

public class Tramo extends Persistente {

  @ElementCollection
  @CollectionTable(name = "tramo_obstaculo", joinColumns = @JoinColumn(name = "tramo_id", referencedColumnName = "id"))
  @Column(name = "obstaculo")
  private List<Obstaculo> obstaculos;

  public Tramo(List<Obstaculo> obstaculos) {
    this.obstaculos = obstaculos;
  }

  public Tramo() {

  }

  public List<Obstaculo> getObstaculos() {
    return obstaculos;
  }

  public void setObstaculos(List<Obstaculo> obstaculos) {
    this.obstaculos = obstaculos;
  }
}
