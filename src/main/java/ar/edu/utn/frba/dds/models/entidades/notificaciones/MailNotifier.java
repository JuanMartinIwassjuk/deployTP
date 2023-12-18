package ar.edu.utn.frba.dds.models.entidades.notificaciones;

import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import java.util.Objects;

public class MailNotifier implements Notificador {

  MailSender mailSender;

  @Override
  public void enviarNotificacion(Usuario usuario, Notificacion notificacion) {
   mailSender.enviarNotificacion(usuario.getMail(), notificacion);
  }

  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

 /*
  MailSender mailSender;

  @Override
  public void enviarNotificacion(Usuario usuario, Notificacion notificacion) {
    mailSender.enviarNotificacion(usuario.getMail(), notificacion);
  }

  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MailNotifier that = (MailNotifier) o;
    return Objects.equals(mailSender, that.mailSender);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mailSender);
  }

  */
}
