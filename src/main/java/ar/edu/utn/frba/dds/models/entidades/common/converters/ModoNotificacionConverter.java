package ar.edu.utn.frba.dds.models.entidades.common.converters;

import ar.edu.utn.frba.dds.models.entidades.modosnotificacion.ModoNotificacion;
import ar.edu.utn.frba.dds.models.entidades.modosnotificacion.NotificadorEnInstante;
import ar.edu.utn.frba.dds.models.entidades.modosnotificacion.NotificadorSinApuros;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;
/*
@Converter(autoApply = true)
public class ModoNotificacionConverter implements AttributeConverter<ModoNotificacion, String> {
  @Override
  public String convertToDatabaseColumn(ModoNotificacion modoDeNotificacion) {
    String nombreDelModo = "";

    switch (modoDeNotificacion.getClass().getName()) {
      case "NotificadorEnInstante":
        nombreDelModo = "instante";
        break;
      case "NotificadorSinApuros":
        nombreDelModo = "sinApuros";
        break;
    }
    return nombreDelModo;
  }

  @Override
  public ModoNotificacion convertToEntityAttribute(String s) {
    ModoNotificacion medio = null;

    if (Objects.equals(s, "instante"))
      medio = new NotificadorEnInstante();

    if (Objects.equals(s, "sinApuros"))
      medio = new NotificadorSinApuros();

    return medio;
  }
}
*/

@Converter(autoApply = true)
public class ModoNotificacionConverter implements AttributeConverter<ModoNotificacion, String> {
    @Override
    public String convertToDatabaseColumn(ModoNotificacion modoNotificacion) {
      String modo = null;

      if(modoNotificacion.getClass().getName().equals("ar.edu.utn.frba.dds.models.entidades.modosnotificacion.NotificadorEnInstante")) {
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
