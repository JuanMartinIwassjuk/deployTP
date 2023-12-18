package ar.edu.utn.frba.dds.server;


import static io.javalin.apibuilder.ApiBuilder.*;

import ar.edu.utn.frba.dds.controllers.FactoryController;
import ar.edu.utn.frba.dds.controllers.IncidenteController;
import ar.edu.utn.frba.dds.controllers.MenuController;
import ar.edu.utn.frba.dds.controllers.MiembrosController;
import ar.edu.utn.frba.dds.controllers.OrganismosController;
import ar.edu.utn.frba.dds.controllers.SesionController;
import ar.edu.utn.frba.dds.controllers.EntidadesController;
import ar.edu.utn.frba.dds.controllers.UsuarioController;
import ar.edu.utn.frba.dds.models.entidades.common.TipoRol;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import io.javalin.Javalin;

public class Router {

  public static void init() {


    Server.app().routes(() -> {
      get("/",((MenuController) FactoryController.controller("Menu"))::index);
      get("/menu", ((UsuarioController) FactoryController.controller("Usuarios"))::cerrarLogin);
      get("/iniciarsesion", ((SesionController) FactoryController.controller("Sesiones"))::show);
      post("/iniciarsesion", ((SesionController) FactoryController.controller("Sesiones"))::create);
      get("/incidentesenlocalizacion", ((IncidenteController) FactoryController.controller("Incidentes"))::incidentesPorLocalizacion);
      get("/incidentes/nuevo",((IncidenteController) FactoryController.controller("Incidentes"))::show);
      post("/incidentes/nuevo",((IncidenteController) FactoryController.controller("Incidentes"))::create);
      get("/cargaOrganismos", ((OrganismosController) FactoryController.controller("Organismos"))::show);
      post("/cargaOrganismos", ((OrganismosController) FactoryController.controller("Organismos"))::cargaCSV);
      get("/organismos", ((OrganismosController) FactoryController.controller("Organismos"))::listadoOrganismos);
      get("/rankings", ((EntidadesController) FactoryController.controller("Rankings"))::index);
      get("/miembros", ((MiembrosController) FactoryController.controller("Miembros"))::show);
      post("/menu", ((UsuarioController) FactoryController.controller("Usuarios"))::create);
      get("/comunidades",((MenuController) FactoryController.controller("Menu"))::mostrarComunidades);
      post("/comunidades",((MenuController) FactoryController.controller("Menu"))::create);
      get("/incidentes", ((IncidenteController) FactoryController.controller("Incidentes"))::listadoIncidentes);
      post("/incidentes", ((IncidenteController) FactoryController.controller("Incidentes"))::detalleIncidente);
      get("/incidentes/{id}/detalle", ((IncidenteController) FactoryController.controller("Incidentes"))::detalleIncidente);
      post("/incidentes/{id}/detalle", ((IncidenteController) FactoryController.controller("Incidentes"))::cerrarIncidente);


    });
  }
}