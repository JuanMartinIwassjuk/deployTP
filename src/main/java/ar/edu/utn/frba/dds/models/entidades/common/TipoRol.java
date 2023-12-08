package ar.edu.utn.frba.dds.models.entidades.common;

import io.javalin.security.RouteRole;

public enum TipoRol implements RouteRole {
  ADMINISTRADOR,
  MIEMBRO,
}
