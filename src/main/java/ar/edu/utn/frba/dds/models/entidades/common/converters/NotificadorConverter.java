package ar.edu.utn.frba.dds.models.entidades.common.converters;

import ar.edu.utn.frba.dds.models.entidades.notificaciones.MailNotifier;
import ar.edu.utn.frba.dds.models.entidades.notificaciones.Notificador;
import ar.edu.utn.frba.dds.models.entidades.notificaciones.WhatsappNotifier;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = true)
public class NotificadorConverter implements AttributeConverter<Notificador, String> {
    @Override
    public String convertToDatabaseColumn(Notificador modoNotificacion) {
      String modo = null;

      if(modoNotificacion.getClass().getName().equals("ar.edu.utn.frba.dds.notificaciones.MailNotifier")) {
        return "email";
      }
        else
        {
          return "wpp";
        }
    }


  @Override
    public Notificador convertToEntityAttribute(String s) {
      Notificador notificador = null;


      if(s.equals("wpp")) {
        notificador = new WhatsappNotifier();
      }
      else if(s.equals("email")) {
        notificador = new MailNotifier();
      }
      return notificador;
    }


  }