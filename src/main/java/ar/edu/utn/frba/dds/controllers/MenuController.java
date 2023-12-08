package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.common.TipoRol;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuController extends Controller implements ICrudViewsHandler {


  @Override
  public void index(Context context) {
    Map<String, Object> model = new HashMap<>();
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }
    else if(usuarioLogueado(context).getRol().getTipo().equals(TipoRol.ADMINISTRADOR)){
      model.put("rolAdmin", true);
      context.render("menuOpcionesV2.hbs", model);
    }
    else {
      model.put("rolMiembro", true);
      context.render("menuOpcionesV2.hbs", model);
    }
  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {

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
