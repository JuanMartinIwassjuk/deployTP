package ar.edu.utn.frba.dds.models.entidades.common.converters;

import ar.edu.utn.frba.dds.models.entidades.modosnotificacion.ModoNotificacion;
import ar.edu.utn.frba.dds.models.entidades.modosnotificacion.NotificadorEnInstante;
import ar.edu.utn.frba.dds.models.entidades.modosnotificacion.NotificadorSinApuros;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ModoNotificacionConverter implements AttributeConverter<ModoNotificacion, String> {
    @Override
    public String convertToDatabaseColumn(ModoNotificacion modoNotificacion) {
      String modo = null;

      if(modoNotificacion.getClass().getName().equals("ar.edu.utn.frba.dds.modosnotificacion.NotificadorEnInstante")) {
        return  "instante";
      }
      else
      {
        return "sinApuros";
      }
    }



    @Override
    public ModoNotificacion convertToEntityAttribute(String s) {
      ModoNotificacion modoNoti = null;


      if(s.equals("instante")) {
        modoNoti = new NotificadorEnInstante();
      }
      else if(s.equals("sinApuros")) {
        modoNoti = new NotificadorSinApuros();
      }
      return modoNoti;
    }

  }
