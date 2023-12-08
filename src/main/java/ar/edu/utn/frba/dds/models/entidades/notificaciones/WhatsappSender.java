package ar.edu.utn.frba.dds.models.entidades.notificaciones;

public interface WhatsappSender {
  void enviarNotificacion(String celular, Notificacion notificacion);
}
