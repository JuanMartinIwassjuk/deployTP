package ar.edu.utn.frba.dds.models.entidades.notificaciones;

public interface MailSender {
  void enviarNotificacion(String mail, Notificacion notificacion);
}
