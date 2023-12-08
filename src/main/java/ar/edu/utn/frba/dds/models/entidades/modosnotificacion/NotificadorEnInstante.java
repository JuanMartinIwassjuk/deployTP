package ar.edu.utn.frba.dds.models.entidades.modosnotificacion;

import java.time.LocalDateTime;

public class NotificadorEnInstante implements ModoNotificacion {


  @Override
  public Boolean estaEnRangoHorario(LocalDateTime fecha) {
    return true;
  }
}
