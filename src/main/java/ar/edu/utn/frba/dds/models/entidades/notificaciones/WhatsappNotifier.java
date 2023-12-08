package ar.edu.utn.frba.dds.models.entidades.notificaciones;

import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import java.util.Objects;

public class WhatsappNotifier implements Notificador {
/*
  WhatsappSender whatsappSender;

  @Override
  public void enviarNotificacion(Usuario usuario, Notificacion notificacion) {
      whatsappSender.enviarNotificacion(usuario.getCelular(), notificacion);
  }

 */

  WhatsappSender whatsappSender;

  @Override
  public void enviarNotificacion(Usuario usuario, Notificacion notificacion) {
    whatsappSender.enviarNotificacion(usuario.getCelular(), notificacion);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    WhatsappNotifier that = (WhatsappNotifier) o;
    return Objects.equals(whatsappSender, that.whatsappSender);
  }

  @Override
  public int hashCode() {
    return Objects.hash(whatsappSender);
  }
}
