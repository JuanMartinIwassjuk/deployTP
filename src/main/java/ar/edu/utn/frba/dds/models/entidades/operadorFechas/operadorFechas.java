package ar.edu.utn.frba.dds.models.entidades.operadorFechas;

import ar.edu.utn.frba.dds.models.entidades.incidentes.Incidente;
import java.time.LocalDateTime;

import java.util.List;


public class operadorFechas {
  public double promedioDias(LocalDateTime fecha1,LocalDateTime fecha2){
    return (double) (fecha2.getDayOfMonth() - fecha1.getDayOfMonth()) /2;
  }
  public double promedioHoras(LocalDateTime fecha1,LocalDateTime fecha2){
    return Math.abs((fecha2.getHour()-fecha1.getHour())/2);
  }
  public double promedioMinutos(LocalDateTime fecha1,LocalDateTime fecha2){
    return Math.abs((fecha2.getMinute()-fecha1.getMinute())/2);
  }

  public double promedioDiasTotales(List<Incidente> incidentes){
    double diasTotales=incidentes.stream().mapToDouble(i->this.promedioDias(i.getFechaApertura(),i.getFechaCierre())).sum();
    return diasTotales/incidentes.size();
  }
  public double promedioHorasTotales(List<Incidente> incidentes){
    double horasTotales=incidentes.stream().mapToDouble(i->this.promedioHoras(i.getFechaApertura(),i.getFechaCierre())).sum();
    return horasTotales/incidentes.size();
  }
  public double promedioMinutosTotales(List<Incidente> incidentes){
    double minutosTotales=incidentes.stream().mapToDouble(i->this.promedioMinutos(i.getFechaApertura(),i.getFechaCierre())).sum();
    return minutosTotales/incidentes.size();
  }



}
