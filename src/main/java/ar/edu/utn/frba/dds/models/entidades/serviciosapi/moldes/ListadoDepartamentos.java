package ar.edu.utn.frba.dds.models.entidades.serviciosapi.moldes;

import java.util.List;

public class ListadoDepartamentos {

  public int cantidad;
  public int total;
  public int inicio;
  public Parametro parametros;
  public List<Departamento> departamentos;

  private static class Parametro {
    public List<String> campos;
    public int max;
    public List<String> provincia;
  }

}
