package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.common.Rol;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.server.exceptions.AccessDeniedException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

public abstract class Controller implements WithSimplePersistenceUnit {

  protected Usuario usuarioLogueado(Context ctx) {
    if(ctx.sessionAttribute("user_id") == null)
      ctx.redirect("/iniciarsesion");
    return entityManager()
        .find(Usuario.class, Long.parseLong(ctx.sessionAttribute("user_id")));
  }

}