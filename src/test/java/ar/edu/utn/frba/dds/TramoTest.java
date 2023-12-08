package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.models.entidades.common.Obstaculo;
import ar.edu.utn.frba.dds.models.entidades.common.Tramo;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TramoTest {

  private List<Obstaculo> obstaculos;
  private Tramo tramo;

  @BeforeEach
  public void init() {
    obstaculos = new ArrayList<>();
    this.obstaculos.add(Obstaculo.BARRICADA);
    this.tramo = new Tramo(obstaculos);
  }


  @DisplayName("Se crea un tramo valido")
  @Test

  public void seCreaUnTramoYSusUbicacionesNoEstanVacias() {
    Assertions.assertNotNull(this.tramo.getObstaculos());
  }
}
