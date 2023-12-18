package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.common.TipoRol;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Prestacion;
import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioMiembros;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuController extends Controller implements ICrudViewsHandler {

  @Override
  public void index(Context context) {
    Map<String, Object> model = new HashMap<>();
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }
    else if (context.sessionAttribute("comunidad_id") == null){
      context.redirect("/comunidades");
    }
    else if(context.sessionAttribute("roluser_id").equals(TipoRol.ADMINISTRADOR)){
      model.put("rolAdmin", true);
      context.render("menuOpcionesV2.hbs", model);
    }
    else {
      model.put("rolMiembro", true);
      context.render("menuOpcionesV2.hbs", model);
    }

  }

  public void mostrarComunidades(Context context){
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }
    else{
      Map<String, Object> model = new HashMap<>();
      List<Miembro> todosLosMiembros = RepositorioMiembros.getInstance().buscarTodos().stream().toList();
      System.out.println(context.sessionAttribute("user_id").toString());
      List<Miembro> miembrosFiltrados = todosLosMiembros.stream().filter(miembro -> miembro.getUsuario().getId().toString().equals(context.sessionAttribute("user_id").toString())).toList();
      List<Comunidad> comunidades = miembrosFiltrados.stream().map(Miembro::getComunidad_que_es_miembro).toList();
      System.out.println(comunidades.get(0).getNombre());
      System.out.println(comunidades.size());
      System.out.println(miembrosFiltrados.size());

      model.put("comunidades",comunidades);
      context.render("misComunidades.hbs",model);
    }

  }


  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {
    Map<String, Object> model = new HashMap<>();
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    } else {
      System.out.println("USUARIO EN SESION" + context.sessionAttribute("user_id"));
      String idComunidad = context.formParam("comunidadBuscada");
      Long idParseadoComunidad = Long.parseLong(idComunidad);
      context.sessionAttribute("comunidad_id", idComunidad);
      System.out.println("ID COMUNIDAD SELECCIONADA" + context.sessionAttribute("comunidad_id"));
      List<Miembro> todosLosMiembros = RepositorioMiembros.getInstance().buscarTodos();
      List<Miembro> miembrosObjetivos = todosLosMiembros.stream().filter(miembro -> miembro.getUsuarioId().equals(Long.parseLong(context.sessionAttribute("user_id")))).toList();
      Miembro miembroObjetivo = miembrosObjetivos.stream().filter(miembro -> miembro.getComunidad_que_es_miembro().getId().equals(idParseadoComunidad)).toList().get(0);
      System.out.println(miembroObjetivo.getId() + "ESTE ES EL ID DEL MIEMBRO QUE CLICKEO");
      List<Comunidad> comunidades = RepositorioComunidades.getInstance().buscarTodos();
      Comunidad comunidadObjetivo = comunidades.stream().filter(comunidad -> comunidad.getId().equals(idParseadoComunidad)).toList().get(0);
      System.out.println("LA COMUNIDAD SELECCIONADA ES " + comunidadObjetivo.getNombre());
      List<Long> idsAdmins = comunidadObjetivo.getAdministradores().stream().map(Miembro::getUsuarioId).toList();
      System.out.println(miembroObjetivo.getUsuarioId() + "EL ID DEL MIEMBRO SELECCIONADO");
      System.out.println(idsAdmins.size());
      System.out.println(idsAdmins.get(0) + "UN ADMIN");
      System.out.println(idsAdmins.get(1) + "UN ADMIN");
      if(idsAdmins.contains(miembroObjetivo.getUsuarioId())){
        context.sessionAttribute("roluser_id",TipoRol.ADMINISTRADOR);
        model.put("rolAdmin", true);
        context.render("menuOpcionesV2.hbs", model);
      }
      else {
        context.sessionAttribute("roluser_id",TipoRol.MIEMBRO);
        model.put("rolMiembro", true);
        context.render("menuOpcionesV2.hbs", model);
      }
    }
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
