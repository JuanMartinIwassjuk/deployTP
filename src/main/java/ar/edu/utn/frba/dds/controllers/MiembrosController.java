package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.common.TipoRol;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioMiembros;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioPrestaciones;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MiembrosController extends Controller implements ICrudViewsHandler {

  private RepositorioMiembros repositorioMiembros;

  public MiembrosController(RepositorioMiembros repositorioMiembros){
    this.repositorioMiembros = repositorioMiembros;
  }

  @Override
  public void index(Context context) {
  }

  @Override
  public void show(Context context) {

    Map<String, Object> model = new HashMap<>();
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }
    else if (context.sessionAttribute("comunidad_id") == null){
      context.redirect("/comunidades");
    }
    else if(context.sessionAttribute("roluser_id").equals(TipoRol.ADMINISTRADOR)){

      List<Comunidad> comunidades = RepositorioComunidades.getInstance().buscarTodos();
      Comunidad comunidadObjetivo = comunidades.stream().filter(comunidad -> comunidad.getId().equals(Long.parseLong(context.sessionAttribute("comunidad_id")))).toList().get(0);
      List<Miembro> miembrosComunidad = comunidadObjetivo.getMiembros();
      model.put("miembros",miembrosComunidad);
      context.render("miembros.hbs", model);
    }
    else {
      context.render("401v2.hbs");
    }
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