package ar.edu.utn.frba.dds.models.entidades.rankings;

import ar.edu.utn.frba.dds.models.repositorios.RepositorioEntidades;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Entidad;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GeneradorDeRanking {
  public List<Entidad> entidadesConMayorPromedioDeCierre(){
    List<Entidad> entidadesOrdenadas = RepositorioEntidades.getInstance().getEntidades().stream()
        .sorted(Comparator.comparingDouble(Entidad::promedioCierreIncidentesEnMinutos)) // Ordenar por cantidad de incidentes
        .collect(Collectors.toList());
    return entidadesOrdenadas;
  }

  public List<Entidad> entidadesConMayorCantidadDeIncidentes(){
    List<Entidad> entidadesOrdenadas = RepositorioEntidades.getInstance().getEntidades().stream()
        .sorted(Comparator.comparingInt(Entidad::cantidadIncidentesEnLaSemana)) // Ordenar por cantidad de incidentes
        .collect(Collectors.toList());
    return entidadesOrdenadas;

  }

  public List<Entidad> entidadesConMayorImpacto(){

    List<Entidad> entidadesOrdenadas = RepositorioEntidades.getInstance().getEntidades().stream()
        .sorted(Comparator.comparingInt(Entidad::gradoDeProblematica)) // Ordenar por problematica
        .collect(Collectors.toList());
    return entidadesOrdenadas;

  }
}
