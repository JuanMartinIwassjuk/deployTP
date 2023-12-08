package ar.edu.utn.frba.dds.models.entidades.notificaciones;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class NotificacionTwilio implements WhatsappSender{

  public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
  public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");

  @Override
  public void enviarNotificacion(String celular, Notificacion notificacion) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message = Message.creator(
            new com.twilio.type.PhoneNumber("whatsapp:+15005550006"),
            new com.twilio.type.PhoneNumber(celular),
            notificacion.getDescripcion())
        .create();
  }
}
