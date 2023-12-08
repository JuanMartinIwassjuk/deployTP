package ar.edu.utn.frba.dds.server.handlers;

import io.javalin.Javalin;
import java.nio.file.AccessDeniedException;

public class AccessDeniedHandler implements IHandler {

  @Override
  public void setHandle(Javalin app) {
    app.exception(AccessDeniedException.class, (e, context) -> {
      context.render("401.hbs");
    });
  }
}
