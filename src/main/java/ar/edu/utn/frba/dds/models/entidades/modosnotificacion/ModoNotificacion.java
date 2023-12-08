package ar.edu.utn.frba.dds.models.entidades.modosnotificacion;

import java.time.LocalDateTime;

public interface ModoNotificacion {
    Boolean estaEnRangoHorario(LocalDateTime fecha);
}
