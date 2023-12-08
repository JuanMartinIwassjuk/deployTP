package ar.edu.utn.frba.dds.models.entidades.modosnotificacion;

import java.time.LocalDateTime;

public class Horario {
  LocalDateTime horaInicio;
  LocalDateTime horaFin;

  public LocalDateTime getHoraInicio(){
    return this.horaInicio;
  }

  public LocalDateTime getHoraFin(){
    return this.horaFin;
  }

}
