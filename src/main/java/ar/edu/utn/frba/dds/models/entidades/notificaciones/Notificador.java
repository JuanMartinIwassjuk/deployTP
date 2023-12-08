package ar.edu.utn.frba.dds.models.entidades.notificaciones;

import ar.edu.utn.frba.dds.models.entidades.common.Usuario;

public interface Notificador {
  //void enviarNotificacion(Usuario usuario, Notificacion notificacion);

  void enviarNotificacion(Usuario usuario, Notificacion notificacion);
  @Override
  boolean equals(Object o);

  @Override
  int hashCode();
}
