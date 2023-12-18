package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioUsuarios;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class UsuarioController implements ICrudViewsHandler {


  @Override
  public void index(Context context) {

  }

  @Override
  public void show(Context context) {

    if (context.sessionAttribute("user_id") != null) {
      context.redirect("/");
    }

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("SesionIniciada", false);
    modelo.put("RolAdministrador", false);
    modelo.put("RolMiembro", false);

    context.render("IniciarSesion.hbs", modelo);
    System.out.println("Mostro la pantalla login");

  }


  public void cerrarLogin(Context context) {

    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }

    context.render("Menu_Inicio.hbs");

  }

  @Override
  public void create(Context context) {

    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }

      context.sessionAttribute("user_rol", null);
      context.sessionAttribute("tipo_rol", null);
      context.sessionAttribute("user_id", null);
      context.sessionAttribute("comunidad_id",null);
      context.redirect("/iniciarsesion");


  }


  @Override
  public void save(Context context) {

  }

  @Override
  public void edit(Context context) {

  }

  @Override
  public void update(Context context) {

  }

  @Override
  public void delete(Context context) {

  }
}

