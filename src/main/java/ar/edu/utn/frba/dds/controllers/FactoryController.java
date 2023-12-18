package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.rankings.GeneradorDeRanking;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioEntidades;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioMiembros;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioOrganismosControl;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioPrestaciones;

public class FactoryController {

  public static Object controller(String nombre) {
    Object controller = null;
    switch (nombre) {
      case "Menu" : controller = new MenuController(); break;
      case "Incidentes" : controller = new IncidenteController(RepositorioPrestaciones.getInstance(),RepositorioComunidades.getInstance(), RepositorioIncidentes.getInstance(), RepositorioMiembros.getInstance(), RepositorioEntidades.getInstance()); break;
      case "Sesiones" : controller = new SesionController(RepositorioIncidentes.getInstance()); break;
      case "Organismos" : controller = new OrganismosController(RepositorioOrganismosControl.getInstance()); break;
      case "Rankings" : controller = new EntidadesController(new GeneradorDeRanking()); break;
      case "Miembros" : controller = new MiembrosController(RepositorioMiembros.getInstance()); break;
      case "Usuarios" : controller = new UsuarioController(); break;
    }
    return controller;
  }
}
